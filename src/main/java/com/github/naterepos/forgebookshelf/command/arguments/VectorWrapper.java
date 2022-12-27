package com.github.naterepos.forgebookshelf.command.arguments;

import com.github.naterepos.forgebookshelf.location.Vector;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ILocationArgument;

public class VectorWrapper {

    private ILocationArgument input;

    public VectorWrapper(ILocationArgument input) {
        this.input = input;
    }

    public Vector unwrapped(CommandSource source) {
        return new Vector(input.getPosition(source));
    }
}
