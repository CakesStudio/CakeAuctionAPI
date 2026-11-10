package dev.cakestudio.cakeauctionapi.api.data;

import java.util.UUID;

/**
 * Represents a historical transaction or audit record for an auction item.
 */
public interface IHistoryRecord {

    /**
     * Gets the database auto-increment ID of the record.
     *
     * @return The record ID.
     */
    long getId();

    /**
     * Gets the UUID of the auction item.
     *
     * @return The item UUID.
     */
    UUID getItemId();

    /**
     * Gets the UUID of the seller.
     *
     * @return The seller UUID.
     */
    UUID getSellerUuid();

    /**
     * Gets the name of the seller.
     *
     * @return The seller name.
     */
    String getSellerName();

    /**
     * Gets the UUID of the buyer, if any.
     *
     * @return The buyer UUID or null.
     */
    UUID getBuyerUuid();

    /**
     * Gets the name of the buyer, if any.
     *
     * @return The buyer name or null.
     */
    String getBuyerName();

    /**
     * Gets the item material or display name string.
     *
     * @return The material name.
     */
    String getMaterial();

    /**
     * Gets the quantity of items in the transaction.
     *
     * @return The item amount.
     */
    int getAmount();

    /**
     * Gets the transaction price.
     *
     * @return The price.
     */
    double getPrice();

    /**
     * Gets the epoch timestamp in milliseconds when the transaction occurred.
     *
     * @return The timestamp.
     */
    long getTimestamp();

    /**
     * Gets the type of transaction (e.g., "SOLD", "SOLD_PARTIAL", "AUTO_BUY", "CANCELLED_REMOTE").
     *
     * @return The transaction type string.
     */
    String getType();

    /**
     * Gets the origin server identifier.
     *
     * @return The server origin string.
     */
    String getServerOrigin();

    /**
     * Gets the economy or currency identifier used for this transaction.
     *
     * @return The economy ID.
     */
    String getEconomyId();

    /**
     * Gets the tax amount deducted from the seller during this transaction.
     *
     * @return The tax amount.
     */
    double getTaxAmount();

}