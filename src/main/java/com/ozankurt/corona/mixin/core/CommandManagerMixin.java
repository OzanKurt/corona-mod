package com.ozankurt.corona.mixin.core;

import com.mojang.brigadier.CommandDispatcher;
import com.ozankurt.corona.Corona;
import com.ozankurt.corona.commands.CoughCommand;
import com.ozankurt.corona.commands.CureCommand;
import com.ozankurt.corona.commands.InfectCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(boolean boolean_1, CallbackInfo ci) {
        CoughCommand.register(this.dispatcher);
        InfectCommand.register(this.dispatcher);
        CureCommand.register(this.dispatcher);
    }
}
