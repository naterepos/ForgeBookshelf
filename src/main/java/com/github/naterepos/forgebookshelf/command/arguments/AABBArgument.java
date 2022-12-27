package com.github.naterepos.forgebookshelf.command.arguments;

import com.github.naterepos.forgebookshelf.location.AABB;
import com.github.naterepos.forgebookshelf.location.Vector;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class AABBArgument implements ArgumentType<AABB> {

    private static final String COMMAND_ERROR = "That is an invalid bounding box! Use the format: \"<xMin> <yMin> <zMin> <xMax> <yMax> <zM\"";

    @Override
    public AABB parse(StringReader reader) throws CommandSyntaxException {
        int[] numbers = new int[6];
        for(int i = 0; i < 6; i++) {
            numbers[i] = reader.readInt();
            reader.skipWhitespace();
        }

        try {
            return new AABB(new Vector(numbers[0], numbers[1], numbers[2]),
                    new Vector(numbers[3], numbers[4], numbers[5]));
        } catch(NumberFormatException e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralMessage(COMMAND_ERROR)), new LiteralMessage(COMMAND_ERROR));
        }
    }

    public static AABBArgument arg() {
        return new AABBArgument();
    }
}
