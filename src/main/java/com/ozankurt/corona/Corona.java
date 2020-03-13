package com.ozankurt.corona;

import com.mojang.brigadier.CommandDispatcher;
import com.ozankurt.corona.commands.CoughCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public class Corona implements ModInitializer {

	private static Corona instance = new Corona();

	public Corona() {
		instance = this;
	}

	@Override
	public void onInitialize() {
		System.out.println("Corona Mod: onInitialize");
	}

	public static Corona getInstance() {
		return instance;
	}
}
