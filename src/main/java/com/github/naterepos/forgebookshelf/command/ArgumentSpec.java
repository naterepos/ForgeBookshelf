package com.github.naterepos.forgebookshelf.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

public class ArgumentSpec<T,W extends ArgumentType<T>> {

    private final String id;
    private final W type;

    public ArgumentSpec(String id, W type) {
        this.id = id;
        this.type = type;
    }

    public RequiredArgumentBuilder<CommandSource, T> toBrigadier() {
        return RequiredArgumentBuilder.argument(id, type);
    }

    public String id() {
        return id;
    }
}
