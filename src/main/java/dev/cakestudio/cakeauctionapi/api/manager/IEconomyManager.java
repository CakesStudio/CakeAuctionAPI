package dev.cakestudio.cakeauctionapi.api.manager;

import org.bukkit.OfflinePlayer;

import lombok.NonNull;

/**
 * Interface for economy management, abstracting the underlying provider (Vault, etc.).
 */
public interface IEconomyManager {

    /**
     * Gets the current balance of a player.
     *
     * @param player The player.
     * @return The balance amount.
     */
    double getBalance(@NonNull OfflinePlayer player);

    /**
     * Checks if a player has at least a specific amount of money.
     *
     * @param player The player.
     * @param amount The amount to check.
     * @return true if the balance is sufficient.
     */
    boolean has(@NonNull OfflinePlayer player, double amount);

    /**
     * Adds money to a player's balance.
     *
     * @param player The player.
     * @param amount The amount to add.
     */
    void deposit(@NonNull OfflinePlayer player, double amount);

    /**
     * Removes money from a player's balance.
     *
     * @param player The player.
     * @param amount The amount to withdraw.
     * @return true if the transaction was successful.
     */
    boolean withdraw(@NonNull OfflinePlayer player, double amount);

    /**
     * Formats a monetary amount into a human-readable string based on plugin settings.
     *
     * @param amount The amount to format.
     * @return The formatted currency string.
     */
    @NonNull
    String format(double amount);

}