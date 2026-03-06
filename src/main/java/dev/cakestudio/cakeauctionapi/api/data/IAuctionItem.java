package dev.cakestudio.cakeauctionapi.api.data;

import net.kyori.adventure.text.Component;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Represents an item currently listed or historical on the auction house.
 */
public interface IAuctionItem {

    /**
     * Gets the unique internal identifier of the auction.
     *
     * @return The UUID of the auction lot.
     */
    UUID getId();

    /**
     * Gets a shortened, human-readable version of the auction identifier.
     *
     * @return A short ID string.
     */
    String getShortId();

    /**
     * Gets the unique identifier of the player who listed the item.
     *
     * @return The seller's UUID.
     */
    UUID getSellerUuid();

    /**
     * Gets the name of the player who listed the item.
     *
     * @return The seller's name.
     */
    String getSellerName();

    /**
     * Gets the actual Minecraft item being sold.
     *
     * @return The {@link ItemStack} of the auction item.
     */
    ItemStack getItemStack();

    /**
     * Gets the current price of the auction lot.
     *
     * @return The price as a double.
     */
    double getPrice();

    /**
     * Gets the timestamp when the auction was created.
     *
     * @return Creation timestamp in milliseconds.
     */
    long getCreatedAt();

    /**
     * Gets the timestamp when the auction will expire.
     *
     * @return Expiration timestamp in milliseconds.
     */
    long getExpiresAt();

    /**
     * Gets the identifier of the server where the auction originated.
     * Useful for cross-server synchronization.
     *
     * @return The origin server name or ID.
     */
    String getOriginServer();

    /**
     * Checks if the item has been sold.
     *
     * @return true if sold, false otherwise.
     */
    boolean isSold();

    /**
     * Checks if the auction has expired without being sold.
     *
     * @return true if expired, false otherwise.
     */
    boolean isExpired();

    /**
     * Gets the name of the player who purchased the item, if sold.
     *
     * @return The buyer's name or null if not sold.
     */
    String getBuyerName();

    /**
     * Gets the unique hash of the item data (SHA-1).
     * Useful for deduplication and fast item comparison.
     *
     * @return The item data hash string.
     */
    String getItemHash();

    /**
     * Gets the pre-formatted display name as an Adventure Component.
     * Utilizes internal caching for maximum performance.
     *
     * @return The formatted display name component.
     */
    Component getDisplayNameComponent();

    /**
     * Gets the pre-formatted lore as a list of Adventure Components.
     * Utilizes internal caching for maximum performance.
     *
     * @return An unmodifiable list of formatted lore components.
     */
    List<Component> getLoreComponents();

    /**
     * Checks if the item can only be purchased as a full stack.
     *
     * @return true if partial purchase is disabled, false otherwise.
     */
    boolean isOnlyFull();

    /**
     * Checks if the auction is currently active and visible to players.
     *
     * @return true if active, false otherwise.
     */
    boolean isActive();

}