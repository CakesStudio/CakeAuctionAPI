package dev.cakestudio.cakeauctionapi.api.manager.system;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manager for handling plugin configurations and cached settings.
 */
public interface IConfigManager {

    /**
     * Gets a specific cached configuration file by its type name.
     *
     * @param name The configuration identifier (e.g., "config", "messages", "database").
     * @return The cached {@link FileConfiguration}, or null if not found.
     */
    FileConfiguration getConfig(String name);

    /**
     * Gets a value from the main configuration, with a fallback.
     *
     * @param path         The config path.
     * @param defaultValue The default value if the path is missing.
     * @param <T>          The value type.
     * @return The config value.
     */
    <T> T getSetting(String path, T defaultValue);

    /**
     * Reloads all plugin configurations.
     */
    void reload();

}