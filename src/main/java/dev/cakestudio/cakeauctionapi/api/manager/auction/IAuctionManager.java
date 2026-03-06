package dev.cakestudio.cakeauctionapi.api.manager.auction;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Manager for advanced auction operations, searching, filtering, and category management.
 */
public interface IAuctionManager {

    /**
     * Gets all active auction items.
     *
     * @return A collection of items currently on sale.
     */
    Collection<IAuctionItem> getActiveAuctions();

    /**
     * Gets an auction item by its ID.
     *
     * @param id The item UUID.
     * @return An Optional containing the item if found.
     */
    Optional<IAuctionItem> getAuctionById(UUID id);

    /**
     * Searches for auction items using a text query and filters.
     *
     * @param query       The search text (e.g., item name).
     * @param category    The category name filter, or null for all categories.
     * @param sortType    The sorting algorithm name (e.g., "price_asc", "newest").
     * @return A collection of matching items.
     */
    Collection<IAuctionItem> search(String query, String category, String sortType);

    /**
     * Gets a list of all registered auction categories.
     *
     * @return A collection of category internal names.
     */
    Collection<String> getCategories();

    /**
     * Gets the display name of a category.
     *
     * @param category The category internal name.
     * @return The localized category name.
     */
    String getCategoryDisplayName(String category);

    /**
     * Checks if an item is blacklisted from being sold on the auction.
     *
     * @param item The item to check.
     * @return true if blacklisted.
     */
    boolean isBlacklisted(ItemStack item);

    /**
     * Calculates the tax for a given price.
     *
     * @param player The seller player (for permission-based tax).
     * @param price  The sale price.
     * @return The calculated tax amount.
     */
    double calculateTax(Player player, double price);

}