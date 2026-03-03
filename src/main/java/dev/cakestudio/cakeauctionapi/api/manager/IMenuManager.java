package dev.cakestudio.cakeauctionapi.api.manager;

import org.bukkit.entity.Player;

import lombok.NonNull;

import org.jetbrains.annotations.Nullable;

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
    void registerMenu(@NonNull Player player, @NonNull Object menu);

    /**
     * Unregisters the currently active menu for a player.
     *
     * @param player The player.
     */
    void unregisterMenu(@NonNull Player player);

    }