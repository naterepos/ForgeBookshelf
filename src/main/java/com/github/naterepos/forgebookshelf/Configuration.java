package com.github.naterepos.forgebookshelf;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Configuration {

    private final Map<String, ConfigPairing<?>> files;

    public Configuration() {
        this.files = new HashMap<>();
    }

    public <T> ConfigPairing<T> getFile(String file) {
        return (ConfigPairing<T>) files.get(file);
    }

    public void register(String plugin, String fileName, Class<?> token) {
        files.put(fileName, new ConfigPairing<>(plugin, fileName, token));
        files.get(fileName).reload();
    }

    public <T> T getConfig(String name) {
        return (T) files.get(name);
    }

    public void save() {
        for (ConfigPairing<?> file : files.values()) {
            file.save();
        }
    }

    public void save(String config) {
        files.get(config).save();
    }

    public void reload(String config) {
        files.get(config).reload();
    }

    public void reload() {
        for (ConfigPairing<?> file : files.values()) {
            file.reload();
        }
    }
}
