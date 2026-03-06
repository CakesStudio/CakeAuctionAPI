package dev.cakestudio.cakeauctionapi.api.manager.ui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.NonNull;

/**
 * Manages plugin GUI menus and player interactions with them.
 */
public interface IMenuManager {

    /**
     * Opens a specific menu for a player by its identifier.
     *
     * @param player  The player to open the menu for.
     * @param menuId  The unique ID of the menu (defined in configuration).
     */
    void openMenu(@NonNull Player player, String menuId);

    /**
     * Opens a custom menu provider for a player.
     *
     * @param player   The player.
     * @param provider The custom menu provider.
     */
    void openMenu(@NonNull Player player, IMenuProvider provider);

    /**
     * Registers a menu layout from a YAML configuration.
     * Allows developers to load their menu files into the auction system.
     *
     * @param name   The name of the menu.
     * @param config The YAML configuration of the menu.
     */
    void registerMenuTemplate(String name, FileConfiguration config);

    /**
     * Returns the currently active menu instance for the player.
     *
     * @param player The player.
     * @return The active menu instance or null.
     */
    Object getActiveMenu(@NonNull Player player);

    /**
     * Manually registers an active menu for a player.
     * Useful for addons using custom menu libraries.
     *
     * @param player The player.
     * @param menu   The menu instance.
     */
    void registerMenu(Player player, @NonNull Object menu);

    /**
     * Unregisters the currently active menu for a player.
     *
     * @param player The player.
     */
    void unregisterMenu(Player player);

}