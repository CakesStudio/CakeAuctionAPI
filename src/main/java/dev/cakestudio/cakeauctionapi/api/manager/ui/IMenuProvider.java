package dev.cakestudio.cakeauctionapi.api.manager.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Interface for implementing custom GUI menus within the CakeAuction framework.
 * Menus using this interface benefit from automated cleanup, stress protection, and session management.
 */
public interface IMenuProvider {

    /**
     * Called when the menu is being initialized for a player.
     *
     * @param player The player for whom the menu is created.
     * @return The Bukkit Inventory to display.
     */
    Inventory createInventory(Player player);

    /**
     * Called when a player clicks an item in the menu.
     *
     * @param player The player who clicked.
     * @param slot   The slot index that was clicked.
     * @param action The type of click action (e.g., LEFT, RIGHT).
     * @return true if the event should be cancelled (preventing taking the item), false otherwise.
     */
    boolean handleUpdate(Player player, int slot, String action);

    /**
     * Called when the menu is closed by the player or the system.
     *
     * @param player The player who closed the menu.
     */
    void onClose(Player player);

}