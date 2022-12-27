package com.github.naterepos.forgebookshelf;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigPairing<T> {

    private final Path directory;
    private final Path path;
    private final Class<T> clazz;
    private T object;
    private CommentedConfigurationNode node;
    private HoconConfigurationLoader loader;

    public ConfigPairing(String plugin, String fileName, Class<T> clazz) {
        this.directory = Paths.get((new File("").getAbsolutePath() + "/config/" + plugin + "/"));
        this.path = directory.resolve(fileName);
        this.clazz = clazz;
    }

    public void reload() {
        try {
            if(Files.notExists(directory)) Files.createDirectory(directory);
            if(Files.notExists(path)) Files.createFile(path);
            loader = HoconConfigurationLoader.builder().path(path).build();
            node = loader.load();
            object = node.get(clazz);
            node.set(clazz, object);
            loader.save(node);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        loader = HoconConfigurationLoader.builder().path(path).build();
        try {
            node.set(clazz, object);
            loader.save(node);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return path;
    }

    public CommentedConfigurationNode getNode() {
        return node;
    }

    public HoconConfigurationLoader getLoader() {
        return loader;
    }

    public T getMappedObject() {
        return object;
    }

    public Class<T> getToken() {
        return clazz;
    }
}
