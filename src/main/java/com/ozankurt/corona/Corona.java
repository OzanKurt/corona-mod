package com.ozankurt.corona;

import com.mojang.brigadier.CommandDispatcher;
import com.ozankurt.corona.commands.CoughCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public class Corona implements ModInitializer, ServerEventListener {

	private static Corona instance = new Corona();
	private CommandDispatcher<ServerCommandSource> dispatcher;

	public Corona() {
		instance = this;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Corona Mod: onInitialize");
	}

	public static Corona getInstance() {
		return instance;
	}

	@Override
	public void onServerLoaded(MinecraftServer server) {
		registerCommands(dispatcher);
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		CoughCommand.register(dispatcher);
	}

	public void setCommandDispatcher(CommandDispatcher<ServerCommandSource> dispatcher) {
		this.dispatcher = dispatcher;
	}
}
