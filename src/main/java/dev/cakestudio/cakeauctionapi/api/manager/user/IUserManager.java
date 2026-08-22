package dev.cakestudio.cakeauctionapi.api.manager.user;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Manager for handling user-related data, statistics, limits, and historical records.
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
     * Checks if a player has reached their active auction limit for a specific currency.
     *
     * @param player     The player to check.
     * @param currencyId The currency identifier (e.g. "playerpoints").
     * @return true if the limit has been reached, false otherwise.
     */
    default boolean isLimitReached(Player player, String currencyId) {
        return isLimitReached(player);
    }

    /**
     * Gets the maximum number of auction slots available to the player.
     *
     * @param player The player.
     * @return The maximum slot count.
     */
    default int getMaxSlots(Player player) {
        return 0;
    }

    /**
     * Gets the maximum number of auction slots available to the player for a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @return The maximum slot count for the currency.
     */
    default int getMaxSlots(Player player, String currencyId) {
        return getMaxSlots(player);
    }

    /**
     * Gets the default sell duration in seconds for a player.
     *
     * @param player The player.
     * @return The sell duration in seconds.
     */
    default long getSellDuration(Player player) {
        return 86400L;
    }

    /**
     * Gets the sell duration in seconds for a player for a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @return The sell duration in seconds.
     */
    default long getSellDuration(Player player, String currencyId) {
        return getSellDuration(player);
    }

    /**
     * Gets the current number of items a player has listed on the auction.
     *
     * @param player The player to query.
     * @return The number of active items.
     */
    int getActiveItemCount(Player player);

    /**
     * Gets the current number of items a player has listed for a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @return The number of active items for the currency.
     */
    default int getActiveItemCount(Player player, String currencyId) {
        return getActiveItemCount(player);
    }

    /**
     * Increments the player's total sales count.
     *
     * @param uuid  The UUID of the player.
     * @param count The number of sales to add.
     */
    void addSale(UUID uuid, int count);

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