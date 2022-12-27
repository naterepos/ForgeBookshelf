package com.github.naterepos.forgebookshelf.command;

import com.github.naterepos.forgebookshelf.Text;
import com.github.naterepos.forgebookshelf.utility.PermissionUtilities;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CommandSpec {

    private final LiteralArgumentBuilder<CommandSource> base;
    private final List<Predicate<CommandSource>> predicates;
    private List<ArgumentSpec<?,?>> arguments;
    private List<CommandSpec> children;
    private Command<CommandSource> executor;

    public CommandSpec(String baseName) {
        this.base = LiteralArgumentBuilder.literal(baseName);
        this.predicates = new ArrayList<>();
        this.children = new ArrayList<>();
        this.arguments = new ArrayList<>();
    }

    public CommandSpec children(CommandSpec... children) {
        this.children = Arrays.asList(children);
        return this;
    }

    public CommandSpec child(CommandSpec child) {
        this.children.add(child);
        return this;
    }

    public CommandSpec permission(String permission) {
        this.predicates.add(src -> PermissionUtilities.hasPermission(src, permission));
        return this;
    }

    public CommandSpec arguments(ArgumentSpec<?,?>... args) {
        this.arguments = Arrays.asList(args);
        return this;
    }

    public CommandSpec requires(Predicate<CommandSource> requirement) {
        this.predicates.add(requirement);
        return this;
    }

    public CommandSpec executor(Command<CommandSource> executor) {
        this.executor = executor;
        return this;
    }

    public LiteralArgumentBuilder<CommandSource> toBrigadier() {
        if(children != null) {
            for(CommandSpec child : children) {
                base.then(child.toBrigadier());
            }
        }

        // This is ugly, I know
        // This version is more reliable and somehow less code than the looped version with how annoying brigadier is
        if(arguments.size() == 0) {
            return getExecutable(base);
        } else if (arguments.size() == 1) {
            return base.then(getExecutable(arguments.get(0).toBrigadier()));
        } else if (arguments.size() == 2) {
            return base.then(arguments.get(0).toBrigadier().then(getExecutable(arguments.get(1).toBrigadier())));
        } else if (arguments.size() == 3) {
            return base.then(arguments.get(0).toBrigadier()
                    .then(arguments.get(1).toBrigadier()
                    .then(getExecutable(arguments.get(2).toBrigadier()))));
        } else if (arguments.size() == 4) {
            return base.then(arguments.get(0).toBrigadier()
                    .then(arguments.get(1).toBrigadier()
                    .then(arguments.get(2).toBrigadier()
                    .then(getExecutable(arguments.get(3).toBrigadier())))));
        } else if (arguments.size() == 5) {
            return base.then(arguments.get(0).toBrigadier()
                    .then(arguments.get(1).toBrigadier()
                    .then(arguments.get(2).toBrigadier()
                    .then(arguments.get(3).toBrigadier()
                    .then(getExecutable(arguments.get(4).toBrigadier()))))));
        } else if (arguments.size() == 6) {
            return base.then(arguments.get(0).toBrigadier()
                    .then(arguments.get(1).toBrigadier()
                    .then(arguments.get(2).toBrigadier()
                    .then(arguments.get(3).toBrigadier()
                    .then(arguments.get(4).toBrigadier()
                    .then(getExecutable(arguments.get(5).toBrigadier())))))));
        } else {
            throw new CommandException(Text.of("&cToo many arguments registered for command: \"" + base.getLiteral() + "\n"));
        }
    }

    // These two can be generified with some casting. I am tired. It works for now
    private LiteralArgumentBuilder<CommandSource> getExecutable(LiteralArgumentBuilder<CommandSource> base) {
        for(Predicate<CommandSource> predicate : predicates) {
            base.requires(base.getRequirement().and(predicate));
        }

        if(executor != null) {
            return base.executes(executor);
        } else {
            return base;
        }
    }

    private ArgumentBuilder<CommandSource,?> getExecutable(ArgumentBuilder<CommandSource, ?> executeClass) {
        if(executor != null) {
            return executeClass.executes(executor);
        }
        for(Predicate<CommandSource> predicate : predicates) {
            executeClass.requires(executeClass.getRequirement().and(predicate));
        }
        return executeClass;
    }
}
