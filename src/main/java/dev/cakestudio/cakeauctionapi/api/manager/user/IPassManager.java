package dev.cakestudio.cakeauctionapi.api.manager.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Manager for creating, managing, and querying auction discount passes.
 */
public interface IPassManager {

    /**
     * Creates a pass item stack based on the pass identifier in passes.yml.
     *
     * @param key The pass identifier (e.g. "example_pass", "vip_pass").
     * @return The created {@link ItemStack}, or null if the key was not found.
     */
    @Nullable
    ItemStack createPass(String key);

    /**
     * Checks whether a pass with the specified key exists in configuration.
     *
     * @param key The pass identifier.
     * @return true if exists, false otherwise.
     */
    boolean hasPass(String key);

    /**
     * Gets the active tax discount percentage for the player from passes.
     *
     * @param player The player to check.
     * @return Discount percentage (0.0 if none).
     */
    double getDiscount(Player player);

    /**
     * Checks whether the player currently has an active pass running.
     *
     * @param player The player to check.
     * @return true if active pass exists, false otherwise.
     */
    boolean hasActivePass(Player player);

    /**
     * Gets the remaining time in milliseconds for the player's active pass.
     *
     * @param player The player to check.
     * @return Remaining time in millis, or 0 if expired.
     */
    long getRemainingTime(Player player);

}