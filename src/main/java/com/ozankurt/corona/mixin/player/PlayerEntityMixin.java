package com.ozankurt.corona.mixin.player;

import com.ozankurt.corona.interfaces.PlayerEntityMixinInterface;
import com.ozankurt.corona.utils.CoughManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinInterface {

    @Shadow public abstract Text getName();

    @Shadow public abstract void writeCustomDataToTag(CompoundTag tag);

    protected boolean hasCorona = false;
    public CoughManager coughManager = new CoughManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    public void startCoughing(int ticks) {
        if (hasCorona) {
            coughManager.start(getBlockPos(), ticks);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    protected void onTick(CallbackInfo ci) {
        if (! hasCorona) {
            return;
        }

        if (! coughManager.isCoughing) {
            return;
        }

        BlockPos currentBlocPos = getBlockPos();

        if (coughManager.hasChangedPosition(currentBlocPos)) {
            coughManager.reset();

            sendMessage(new LiteralText("You must NOT move while coughing."));

            return;
        }

        MinecraftServer server = getServer();

        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();

        if (playerList.size() == 1) {
            coughManager.reset();

            sendMessage(new LiteralText("There are no other players to spread Corona."));

            return;
        }

        int currentTicks = server.getTicks();

        if (coughManager.shouldSpread(currentBlocPos, currentTicks)) {

            for (int i = 0; i < playerList.size(); i++) {
                ServerPlayerEntity serverPlayerEntity = playerList.get(i);

                // Skip the same player
                if (serverPlayerEntity.getUuid() == getUuid()) {
                    continue;
                }

                // Get the other players BlockPos
                BlockPos blockPos = serverPlayerEntity.getBlockPos();

                // If the other player is within 2 blocks of the coughing player
                if (blockPos.isWithinDistance(currentBlocPos, 2)) {
                    PlayerEntityMixinInterface playerEntityMixin = (PlayerEntityMixinInterface) serverPlayerEntity;

                    double random = Math.random();

                    if (random > 0.5) {
                        playerEntityMixin.setHasCorona(true);

                        serverPlayerEntity.sendMessage(
                            new LiteralText("You have been plagued with the Corona by " + getName().asString())
                        );

                        sendMessage(
                            new LiteralText("You have successfully spread Corona to " + serverPlayerEntity.getName().asString())
                        );
                    } else {
                        serverPlayerEntity.sendMessage(
                            new LiteralText(getName().asString() + "tried to spread Corona to you but failed. Be careful you might not have a second chance.")
                        );

                        sendMessage(
                            new LiteralText("You couldn't spread Corona to " + serverPlayerEntity.getName().asString())
                        );
                    }
                }
            }

            coughManager.reset();
        }

        return;
    }

    @Inject(at = @At(value = "INVOKE"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void fromTag(CompoundTag tag, CallbackInfo ci) {
        if (! tag.contains("hasCorona")) {
            tag.putBoolean("hasCorona", false);

            return;
        }

        this.hasCorona = tag.getBoolean("hasCorona");
    }

    @Inject(at = @At(value = "INVOKE"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void toTag(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("hasCorona", true);
    }
    public boolean hasCorona() {
        return hasCorona;
    }

    public void setHasCorona(boolean hasCorona) {
        this.hasCorona = hasCorona;
    }

    public CoughManager getCoughManager() {
        return coughManager;
    }

    public void setCoughManager(CoughManager coughManager) {
        this.coughManager = coughManager;
    }
}
