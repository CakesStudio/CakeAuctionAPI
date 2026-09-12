package dev.cakestudio.cakeauctionapi.api.manager.user;

import java.util.UUID;

/**
 * Manager for managing bonus auction lot slots for players.
 */
public interface IBonusLimitManager {

    /**
     * Gets the amount of bonus slots assigned to the player.
     *
     * @param uuid The UUID of the player.
     * @return The bonus slot count.
     */
    int getBonus(UUID uuid);

    /**
     * Sets the exact amount of bonus slots for the player.
     *
     * @param uuid  The UUID of the player.
     * @param bonus The bonus slot count.
     */
    void setBonus(UUID uuid, int bonus);

    /**
     * Adds bonus slots to the player's current total.
     *
     * @param uuid   The UUID of the player.
     * @param amount The amount to add.
     */
    default void addBonus(UUID uuid, int amount) {
        setBonus(uuid, getBonus(uuid) + amount);
    }

    /**
     * Takes bonus slots from the player's current total.
     *
     * @param uuid   The UUID of the player.
     * @param amount The amount to take.
     */
    default void takeBonus(UUID uuid, int amount) {
        setBonus(uuid, Math.max(0, getBonus(uuid) - amount));
    }

    /**
     * Resets the player's bonus slots to zero.
     *
     * @param uuid The UUID of the player.
     */
    default void resetBonus(UUID uuid) {
        setBonus(uuid, 0);
    }

    /**
     * Gets the active pass discount percentage for the player.
     *
     * @param uuid The UUID of the player.
     * @return The discount percentage (e.g. 10.0 for 10%), or 0.0 if no active pass.
     */
    double getPassDiscount(UUID uuid);

    /**
     * Gets the epoch timestamp in milliseconds when the player's pass expires.
     *
     * @param uuid The UUID of the player.
     * @return Expiry timestamp in millis, or 0 if expired/inactive.
     */
    long getPassExpiry(UUID uuid);

    /**
     * Activates a pass discount for the player.
     *
     * @param uuid           The UUID of the player.
     * @param discount       The discount percentage.
     * @param durationMillis Duration of the pass in milliseconds.
     */
    void activatePass(UUID uuid, double discount, long durationMillis);

}