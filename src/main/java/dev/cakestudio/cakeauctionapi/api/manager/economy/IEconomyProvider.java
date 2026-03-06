package dev.cakestudio.cakeauctionapi.api.manager.economy;

import org.bukkit.OfflinePlayer;

/**
 * Interface for implementing custom economy providers.
 * Allows addons to register new currencies or integrate external economy plugins.
 */
public interface IEconomyProvider {

    /**
     * Gets the unique identifier for this economy provider.
     *
     * @return The provider name (e.g., "my_custom_gems").
     */
    String getName();

    /**
     * Gets the current balance of a player.
     *
     * @param player The player.
     * @return The balance amount.
     */
    double getBalance(OfflinePlayer player);

    /**
     * Checks if a player has a certain amount of currency.
     *
     * @param player The player to check.
     * @param amount The amount required.
     * @return true if the player has enough, false otherwise.
     */
    boolean has(OfflinePlayer player, double amount);

    /**
     * Withdraws a certain amount of currency from a player.
     *
     * @param player The player to withdraw from.
     * @param amount The amount to withdraw.
     * @return true if successful, false otherwise.
     */
    boolean withdraw(OfflinePlayer player, double amount);

    /**
     * Deposits a certain amount of currency to a player.
     *
     * @param player The player to deposit to.
     * @param amount The amount to deposit.
     * @return true if successful, false otherwise.
     */
    boolean deposit(OfflinePlayer player, double amount);

    /**
     * Formats a double value into a human-readable currency string.
     *
     * @param amount The amount to format.
     * @return The formatted string (e.g., "100 Gems").
     */
    String format(double amount);

}