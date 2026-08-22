package dev.cakestudio.cakeauctionapi.api.manager.economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import lombok.NonNull;

/**
 * Interface for managing auction taxation, commission rates, and schedules.
 */
public interface ITaxManager {

    /**
     * Checks if the tax system is globally enabled.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isTaxEnabled();

    /**
     * Gets the name of the current taxation mode (e.g. NONE, SELLER, BUYER, BOTH).
     *
     * @return The tax mode name.
     */
    @NonNull
    String getTaxModeName();

    /**
     * Gets the effective tax rate percentage for a player using the default currency.
     *
     * @param player The player.
     * @return The tax rate percentage (e.g. 5.0 for 5%).
     */
    double getTaxRate(@NonNull OfflinePlayer player);

    /**
     * Gets the effective tax rate percentage for a player in a specific currency.
     *
     * @param player     The player.
     * @param currencyId The currency identifier (e.g. "vault", "playerpoints").
     * @return The tax rate percentage.
     */
    double getTaxRate(@NonNull OfflinePlayer player, String currencyId);

    /**
     * Gets the seller tax rate percentage for a player in a specific currency.
     *
     * @param player     The seller player.
     * @param currencyId The currency identifier.
     * @return The seller tax rate percentage.
     */
    double getSellerTaxRate(@NonNull OfflinePlayer player, String currencyId);

    /**
     * Gets the buyer tax rate percentage for a player in a specific currency.
     *
     * @param player     The buyer player.
     * @param currencyId The currency identifier.
     * @return The buyer tax rate percentage.
     */
    double getBuyerTaxRate(@NonNull OfflinePlayer player, String currencyId);

    /**
     * Calculates the tax amount for a given price and currency.
     *
     * @param player     The player.
     * @param price      The lot price.
     * @param currencyId The currency identifier.
     * @return The calculated tax amount.
     */
    default double calculateTax(@NonNull Player player, double price, String currencyId) {
        return price * (getTaxRate(player, currencyId) / 100.0);
    }

    /**
     * Gets the identifier of the currently active tax schedule for a player, or "none".
     *
     * @param player The player.
     * @return The schedule ID or "none".
     */
    default String getActiveScheduleId(OfflinePlayer player) {
        return "none";
    }

}