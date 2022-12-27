package com.github.naterepos.forgebookshelf.command;

import com.github.naterepos.forgebookshelf.command.arguments.AABBArgument;
import com.github.naterepos.forgebookshelf.command.arguments.VectorArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;

import java.util.function.Supplier;

public class CommandParser {

    private final CommandDispatcher<CommandSource> dispatcher;

    public CommandParser(CommandDispatcher<CommandSource> dispatcher) {
        this.dispatcher = dispatcher;
        registerArgument("vector", VectorArgument.class, VectorArgument::new);
        registerArgument("aabb", AABBArgument.class, AABBArgument::new);
    }

    public <T extends ArgumentType<?>> void registerArgument(String id, Class<T> clazz, Supplier<T> argument) {
        ArgumentTypes.register(id, clazz, new ArgumentSerializer<>(argument));
    }

    public final void registerCommand(CommandSpec command) {
        dispatcher.register(command.toBrigadier());
    }
}
