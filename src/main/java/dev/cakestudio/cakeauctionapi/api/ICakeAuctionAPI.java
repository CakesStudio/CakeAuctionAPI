package dev.cakestudio.cakeauctionapi.api;

import com.tcoded.folialib.FoliaLib;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import dev.cakestudio.cakeauctionapi.api.manager.action.*;
import dev.cakestudio.cakeauctionapi.api.manager.auction.*;
import dev.cakestudio.cakeauctionapi.api.manager.economy.*;
import dev.cakestudio.cakeauctionapi.api.manager.system.*;
import dev.cakestudio.cakeauctionapi.api.manager.ui.*;
import dev.cakestudio.cakeauctionapi.api.manager.user.*;
import dev.cakestudio.cakeauctionapi.api.manager.util.*;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Main interface for interacting with the CakeAuction system.
 * Provides access to various managers for managing auctions, economy, users, and core components.
 */
public interface ICakeAuctionAPI {

    /**
     * Returns the manager responsible for auction logic, searching, and categories.
     *
     * @return The {@link IAuctionManager} instance.
     */
    IAuctionManager getAuctionManager();

    /**
     * Returns the manager responsible for UUID v7 generation and short IDs.
     *
     * @return The {@link IUUIDManager} instance.
     */
    IUUIDManager getUUIDManager();

    /**
     * Returns the manager for cross-threaded operations and database tasks.
     *
     * @return The {@link IThreadManager} instance.
     */
    IThreadManager getThreadManager();

    /**
     * Returns the manager for ItemStack serialization and unique signatures.
     *
     * @return The {@link IItemManager} instance.
     */
    IItemManager getItemManager();

    /**
     * Returns the manager for player statistics, limits, and history.
     *
     * @return The {@link IUserManager} instance.
     */
    IUserManager getUserManager();

    /**
     * Returns the manager for networking operations and packets.
     *
     * @return The {@link INetworkManager} instance.
     */
    INetworkManager getNetworkManager();

    /**
     * Returns the manager for monitoring system stress and performance.
     *
     * @return The {@link IMonitorManager} instance.
     */
    IMonitorManager getMonitorManager();

    /**
     * Returns the manager for database connections and migrations.
     *
     * @return The {@link IDatabaseManager} instance.
     */
    IDatabaseManager getDatabaseManager();

    /**
     * Returns the manager for plugin configuration and cached settings.
     *
     * @return The {@link IConfigManager} instance.
     */
    IConfigManager getConfigManager();

    /**
     * Returns the manager for plugin assets and menu layouts.
     *
     * @return The {@link IAssetManager} instance.
     */
    IAssetManager getAssetManager();

    /**
     * Retrieves all currently active auction items.
     *
     * @return A collection of active {@link IAuctionItem} instances.
     */
    default Collection<IAuctionItem> getActiveAuctions() {
        return getAuctionManager().getActiveAuctions();
    }

    /**
     * Finds an active auction item by its unique identifier.
     *
     * @param id The UUID of the auction item.
     * @return An {@link Optional} containing the item if found, otherwise empty.
     */
    default Optional<IAuctionItem> getAuctionById(UUID id) {
        return getAuctionManager().getAuctionById(id);
    }

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
     * Returns the manager responsible for text formatting and messaging.
     *
     * @return The {@link ITextManager} instance.
     */
    ITextManager getTextManager();

    /**
     * Returns the manager responsible for economy operations.
     *
     * @return The {@link IEconomyManager} instance.
     */
    IEconomyManager getEconomyManager();

    /**
     * Returns the manager responsible for external plugin hooks.
     *
     * @return The {@link IHookManager} instance.
     */
    IHookManager getHookManager();

    /**
     * Checks if the auction items have been loaded from the database into the cache.
     *
     * @return true if the auction is ready to use, false otherwise.
     */
    boolean isAuctionLoaded();

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