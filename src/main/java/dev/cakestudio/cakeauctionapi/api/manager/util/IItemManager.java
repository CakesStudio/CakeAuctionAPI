package dev.cakestudio.cakeauctionapi.api.manager.util;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Manager for handling ItemStack serialization, generation, and unique signatures.
 * Essential for storing and identifying items in the auction database.
 */
public interface IItemManager {

    /**
     * Serializes an ItemStack into a base64-encoded string.
     *
     * @param item The ItemStack to serialize.
     * @return The serialized base64 string.
     */
    String serialize(ItemStack item);

    /**
     * Deserializes an ItemStack from a base64-encoded string.
     *
     * @param base64 The serialized string.
     * @return The deserialized ItemStack.
     */
    ItemStack deserialize(String base64);

    /**
     * Serializes a collection of ItemStacks into a base64-encoded string.
     *
     * @param items The items to serialize.
     * @return The serialized base64 string.
     */
    String serializeCollection(Collection<ItemStack> items);

    /**
     * Deserializes a collection of ItemStacks from a base64-encoded string.
     *
     * @param base64 The serialized string.
     * @return The deserialized collection of ItemStacks.
     */
    Collection<ItemStack> deserializeCollection(String base64);

    /**
     * Generates a unique digital signature for an ItemStack.
     * Signatures are used to identify items across servers and detect duplicates.
     *
     * @param item The item to sign.
     * @return A unique hexadecimal signature string.
     */
    String getSignature(ItemStack item);

    /**
     * Creates a new ItemStack for stress testing using the internal item generator.
     *
     * @return The generated random ItemStack.
     */
    ItemStack generateRandomItem();

}