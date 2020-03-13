package com.ozankurt.corona.commands;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.ozankurt.corona.interfaces.PlayerEntityMixinInterface;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Set;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CureCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("cure")
            .then(
                argument("player", word())
                    .suggests( (c, b) -> CommandSource.suggestMatching(getPlayers(c.getSource()), b))
                    .executes(CureCommand::infect)
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
            new LiteralText("You have been cured from the Corona Virus.")
        );

        playerEntityMixin.setHasCorona(false);

        return 1;
    }
}
