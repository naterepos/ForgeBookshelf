package com.github.naterepos.forgebookshelf.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.Vec3Argument;

public class VectorArgument implements ArgumentType<VectorWrapper> {

    private static final String COMMAND_ERROR = "That is an invalid vector! Use the format: \"<x> <y> <z>\"";

    @Override
    public VectorWrapper parse(StringReader reader) throws CommandSyntaxException {
        return new VectorWrapper(Vec3Argument.vec3().parse(reader));
    }

    public static VectorArgument arg() {
        return new VectorArgument();
    }
}

