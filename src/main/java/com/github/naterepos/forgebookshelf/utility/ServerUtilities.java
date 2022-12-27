package com.github.naterepos.forgebookshelf.utility;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;

public class ServerUtilities {

    public static List<ServerPlayerEntity> getPlayers() {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
    }

    public static ServerPlayerEntity getPlayer(UUID player) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(player);
    }

    public static void runPlayerCommand(CommandSource source, String command) {
        ServerLifecycleHooks.getCurrentServer().getCommandManager().handleCommand(source, command);
    }

    public static void runServerCommand(String command) {
        ServerLifecycleHooks.getCurrentServer().getCommandManager().handleCommand(ServerLifecycleHooks.getCurrentServer().getCommandSource(), command);
    }

    public static void runServerCommandToTarget(String command, String playerName) {
        ServerLifecycleHooks.getCurrentServer().getCommandManager().handleCommand(ServerLifecycleHooks.getCurrentServer().getCommandSource(), command.replace("%player%", playerName));
    }

    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static ServerWorld getOverWorld() {
        return ServerLifecycleHooks.getCurrentServer().func_241755_D_();
    }

    public static String getWorldName(ServerWorld world) {
        return ((IServerWorldInfo) world.getWorldInfo()).getWorldName();
    }

    public static ServerWorld getWorld(String world) {
        for(ServerWorld otherWorld : getServer().getWorlds()) {
            if(((IServerWorldInfo) otherWorld.getWorldInfo()).getWorldName().equals(world)) {
                return otherWorld;
            }
        }
        return getOverWorld();
    }

    public static ServerPlayerEntity getPlayer(String username) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(username);
    }
}
