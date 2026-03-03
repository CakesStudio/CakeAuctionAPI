package dev.cakestudio.cakeauctionapi.api;

import com.tcoded.folialib.FoliaLib;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import dev.cakestudio.cakeauctionapi.api.manager.IMenuManager;
import dev.cakestudio.cakeauctionapi.api.manager.IActionManager;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Main interface for interacting with the CakeAuction system.
 * Provides methods for managing auctions, economy transactions, and plugin components.
 */
public interface ICakeAuctionAPI {

    /**
     * Retrieves all currently active auction items.
     *
     * @return A collection of active {@link IAuctionItem} instances.
     */
    Collection<IAuctionItem> getActiveAuctions();

    /**
     * Finds an active auction item by its unique identifier.
     *
     * @param id The UUID of the auction item.
     * @return An {@link Optional} containing the item if found, otherwise empty.
     */
    Optional<IAuctionItem> getAuctionById(UUID id);

    /**
     * Creates and publishes a new auction lot.
     *
     * @param seller          The player selling the item.
     * @param item            The ItemStack to be sold.
     * @param price           The total price of the lot.
     * @param durationSeconds The duration of the auction in seconds.
     * @param onlyFull        Whether the item can only be bought as a whole stack.
     */
    void createAuction(Player seller, ItemStack item, double price, long durationSeconds, boolean onlyFull);

    /**
     * Performs a full purchase of an auction item.
     *
     * @param buyer The player purchasing the item.
     * @param item  The auction item to buy.
     */
    void buyItem(Player buyer, IAuctionItem item);

    /**
     * Performs a partial purchase of a stackable auction item.
     *
     * @param buyer         The player purchasing the items.
     * @param item          The auction item lot.
     * @param count         The number of items to buy from the stack.
     * @param priceForCount The calculated price for the specified amount.
     */
    void buyItemPartial(Player buyer, IAuctionItem item, int count, double priceForCount);

    /**
     * Cancels an active auction and returns the item to the seller.
     *
     * @param seller The player who owns the auction.
     * @param item   The auction item to cancel.
     */
    void cancelAuction(Player seller, IAuctionItem item);

    /**
     * Returns the main CakeAuction plugin instance.
     *
     * @return The {@link JavaPlugin} instance.
     */
    JavaPlugin getPlugin();

    /**
     * Returns the FoliaLib instance for task management.
     *
     * @return The {@link FoliaLib} instance.
     */
    FoliaLib getFoliaLib();

    /**
     * Returns the root data folder of the plugin.
     *
     * @return The plugin's data {@link File} directory.
     */
    File getDataFolder();

    /**
     * Returns the manager responsible for GUI menus.
     *
     * @return The {@link IMenuManager} instance.
     */
    IMenuManager getMenuManager();

    /**
     * Returns the manager responsible for custom actions.
     *
     * @return The {@link IActionManager} instance.
     */
    IActionManager getActionManager();

    /**
     * Registers a command object using the internal TriumphTeam Command library.
     * Integrated into the plugin's main command manager.
     *
     * @param command The command object to register.
     */
    void registerCommand(Object command);

    /**
     * Unregisters a command object and removes it from the Bukkit command map.
     *
     * @param command The command object to unregister.
     */
    void unregisterCommand(Object command);

}