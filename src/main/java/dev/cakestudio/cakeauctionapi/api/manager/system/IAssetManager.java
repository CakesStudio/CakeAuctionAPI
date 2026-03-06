package dev.cakestudio.cakeauctionapi.api.manager.system;

import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

/**
 * Manager for handling static assets and external resources like textures, icons, and menu layouts.
 */
public interface IAssetManager {

    /**
     * Loads a resource file from the plugin's JAR as an InputStream.
     *
     * @param fileName The path to the file inside the JAR.
     * @return The InputStream if found, null otherwise.
     */
    InputStream getResource(String fileName);

    /**
     * Gets a list of available custom asset files (e.g., custom menu YAMLs).
     *
     * @param folder The folder to scan (relative to data folder).
     * @return A collection of File objects.
     */
    Collection<File> getAssetFiles(String folder);

    /**
     * Registers a new asset file provided by an addon.
     *
     * @param targetPath The destination path in the plugin's data folder.
     * @param content    The InputStream content.
     */
    void registerAsset(String targetPath, InputStream content);

    /**
     * Gets the localized name of an ItemStack based on the plugin's language assets.
     * Supports custom item names from ItemsAdder, if configured.
     *
     * @param item The ItemStack to translate.
     * @return The localized name string.
     */
    String getLanguageName(ItemStack item);

}