package com.ozankurt.corona.commands;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ozankurt.corona.interfaces.PlayerEntityMixinInterface;
import com.ozankurt.corona.mixin.player.PlayerEntityMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.Set;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class InfectCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("infect")
            .then(
                argument("player", word())
                    .suggests( (c, b) -> CommandSource.suggestMatching(getPlayers(c.getSource()), b))
                    .executes(InfectCommand::infect)
            );

        dispatcher.register(builder);
    }

    private static Set<String> getPlayers(ServerCommandSource source) {
        Set<String> players = Sets.newLinkedHashSet();

        players.addAll(source.getPlayerNames());

        return players;
    }

    private static int infect(CommandContext<ServerCommandSource> context) {
        MinecraftServer server = context.getSource().getMinecraftServer();
        String playerName = getString(context, "player");

        ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(playerName);
        PlayerEntityMixinInterface playerEntityMixin = (PlayerEntityMixinInterface) serverPlayerEntity;

        serverPlayerEntity.sendMessage(
            new LiteralText("You have been infected with the Corona Virus.")
        );

        playerEntityMixin.setHasCorona(true);

        return 1;
    }
}
