package com.github.naterepos.forgebookshelf.utility;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraftforge.server.permission.PermissionAPI;

public class PermissionUtilities {

    public static boolean hasPermission(CommandSource sender, String permission) {
        try {
            return PermissionAPI.hasPermission(sender.asPlayer(), permission);
        } catch (CommandSyntaxException e) {
            return true;
        }
    }
}
