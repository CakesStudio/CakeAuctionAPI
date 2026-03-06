package dev.cakestudio.cakeauctionapi.api.manager.user;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Manager for handling user-related data, statistics, and historical records.
 * Integrates various user-specific subsystems like limits, passes, and stats.
 */
public interface IUserManager {

    /**
     * Checks if a player has reached their active auction limit.
     *
     * @param player The player to check.
     * @return true if the limit has been reached, false otherwise.
     */
    boolean isLimitReached(Player player);

    /**
     * Gets the current number of items a player has listed on the auction.
     *
     * @param player The player to query.
     * @return The number of active items.
     */
    int getActiveItemCount(Player player);

    /**
     * Increments the player's total sales count.
     *
     * @param uuid  The UUID of the player.
     * @param count The number of sales to add.
     */
    void addSale(UUID uuid, int count);

    /**
     * Adds an entry to the player's transaction history.
     *
     * @param uuid    The player UUID.
     * @param message The localized history message.
     * @param icon    The serialized ItemStack icon for the entry.
     */
    void addHistory(UUID uuid, String message, String icon);

    /**
     * Checks if a player has an active auction pass.
     *
     * @param uuid The player UUID.
     * @return true if the pass is active, false otherwise.
     */
    boolean hasActivePass(UUID uuid);

    /**
     * Gets the player's subscription level or type name.
     *
     * @param uuid The player UUID.
     * @return The subscription name, or "none" if not subscribed.
     */
    String getSubscriptionName(UUID uuid);

}