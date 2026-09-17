package dev.cakestudio.cakeauctionapi.api.manager.user;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * Manager for handling temporary auction lot slot rentals (/ah rent).
 */
public interface IRentManager {

    /**
     * Checks if slot rental is enabled on the server.
     *
     * @return true if rental is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Checks if LuckPerms temporary permission integration is enabled.
     *
     * @return true if temporary LuckPerms nodes are granted on rental.
     */
    boolean isUseLuckPerms();

    /**
     * Gets the number of currently active rented slots for the specified player.
     *
     * @param uuid The player UUID.
     * @return The active rented slot count, or 0 if expired or none.
     */
    int getActiveRentSlots(UUID uuid);

    /**
     * Gets the epoch timestamp in milliseconds when the player's rental expires.
     *
     * @param uuid The player UUID.
     * @return Expiry timestamp in milliseconds, or 0 if expired or none.
     */
    long getRentExpiry(UUID uuid);

    /**
     * Attempts to rent slots for a player using a configured tier key.
     *
     * @param player            The player renting slots.
     * @param tierKey           The tier identifier (e.g. "tier_1").
     * @param preferredCurrency Preferred currency ID to pay with, or null for tier default.
     * @return true if rental succeeded, false otherwise.
     */
    boolean rent(Player player, String tierKey, @Nullable String preferredCurrency);

    /**
     * Gets all configured rental tiers.
     *
     * @return Unmodifiable collection of {@link IRentTier}.
     */
    Collection<? extends IRentTier> getTiers();

    /**
     * Gets a specific rental tier by its configuration key.
     *
     * @param key The tier key.
     * @return The {@link IRentTier} instance, or null if not found.
     */
    @Nullable IRentTier getTier(String key);

    /**
     * Represents a single slot rental tier.
     */
    interface IRentTier {

        /**
         * Gets the configuration key of the tier.
         *
         * @return The tier key.
         */
        String getKey();

        /**
         * Gets the number of additional slots granted by this tier.
         *
         * @return The slot count.
         */
        int getSlots();

        /**
         * Gets the duration of the rental in milliseconds.
         *
         * @return The duration in milliseconds.
         */
        long getDurationMillis();

        /**
         * Gets the human-readable duration string (e.g. "24h", "7d").
         *
         * @return The duration string.
         */
        String getDurationString();

        /**
         * Gets the default currency identifier for this tier.
         *
         * @return The currency identifier.
         */
        String getCurrency();

        /**
         * Gets the price of the rental tier.
         *
         * @return The rental price.
         */
        double getPrice();

        /**
         * Gets the formatted display name of the tier.
         *
         * @return The display name.
         */
        String getDisplayName();
    }

}