package com.ozankurt.corona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ozankurt.corona.interfaces.PlayerEntityMixinInterface;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;

public class CoughCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("cough").executes(CoughCommand::cough);

        dispatcher.register(builder);
    }

    private static int cough(CommandContext<ServerCommandSource> context) {
        MinecraftServer server = context.getSource().getMinecraftServer();

        ServerPlayerEntity serverPlayerEntity;
        PlayerEntityMixinInterface playerEntityMixin;
        try {
            serverPlayerEntity = context.getSource().getPlayer();
            playerEntityMixin = (PlayerEntityMixinInterface) serverPlayerEntity;
        } catch (CommandSyntaxException e) {
            System.out.println("This command requires to be run by a player.");
            return 1;
        }

        if (playerEntityMixin.hasCorona()) {
            playerEntityMixin.startCoughing(server.getTicks());
            serverPlayerEntity.sendMessage(
                new LiteralText("You started coughing, don't move for 3 seconds to infect nearby players.")
            );
        } else {
            serverPlayerEntity.sendMessage(
                new LiteralText("You don't have corona, you can't cough.")
            );
        }

        return 1;
    }
}
