package dev.cakestudio.cakeauctionapi.api.manager.economy;

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

    /**
     * Checks if this economy manager implementation supports multi-currency operations.
     *
     * @return true if multi-currency operations are supported, false otherwise.
     */
    default boolean supportsMultiCurrency() {
        return false;
    }

    /**
     * Checks if a specific currency is supported and registered.
     *
     * @param currencyId The currency identifier.
     * @return true if the currency is supported.
     */
    default boolean isCurrencySupported(@NonNull String currencyId) {
        return false;
    }

    /**
     * Gets the current balance of a player in a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @return The balance amount.
     * @throws UnsupportedOperationException if multi-currency operations are not supported by this implementation.
     */
    default double getBalance(@NonNull OfflinePlayer player, @NonNull String currencyId) {
        throw new UnsupportedOperationException("Multi-currency getBalance for '" + currencyId + "' is not supported by " + getClass().getName());
    }

    /**
     * Checks if a player has at least a specific amount of money in a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @param amount     The amount to check.
     * @return true if the balance is sufficient, false if insufficient or if the currency is unsupported.
     */
    default boolean has(@NonNull OfflinePlayer player, @NonNull String currencyId, double amount) {
        return false;
    }

    /**
     * Adds money to a player's balance in a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @param amount     The amount to add.
     * @throws UnsupportedOperationException if multi-currency operations are not supported by this implementation.
     */
    default void deposit(@NonNull OfflinePlayer player, @NonNull String currencyId, double amount) {
        throw new UnsupportedOperationException("Multi-currency deposit for '" + currencyId + "' is not supported by " + getClass().getName());
    }

    /**
     * Removes money from a player's balance in a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier.
     * @param amount     The amount to withdraw.
     * @return true if the transaction was successful, false if unsupported or insufficient funds.
     */
    default boolean withdraw(@NonNull OfflinePlayer player, @NonNull String currencyId, double amount) {
        return false;
    }

    /**
     * Formats a monetary amount for a specific currency into a human-readable string.
     *
     * @param currencyId The currency identifier.
     * @param amount     The amount to format.
     * @return The formatted currency string.
     */
    @NonNull
    default String format(@NonNull String currencyId, double amount) {
        return amount + " " + currencyId;
    }

    /**
     * Registers a new custom economy provider to be used by the auction.
     * After registration, this provider can be selected in the main config or used programmatically.
     *
     * @param provider The provider implementation.
     */
    void registerProvider(IEconomyProvider provider);

}