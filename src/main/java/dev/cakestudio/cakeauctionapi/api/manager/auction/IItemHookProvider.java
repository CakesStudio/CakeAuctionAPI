package dev.cakestudio.cakeauctionapi.api.manager.auction;

import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * Interface representing a custom item hook provider.
 * Allows addons to integrate third-party item plugins (like Oraxen, ItemsAdder,
 * etc.) into CakeAuction.
 */
public interface IItemHookProvider {

    /**
     * Retrieves an ItemStack by its unique identifier.
     *
     * @param id The custom item identifier (without the prefix, e.g., "ruby").
     * @return The {@link ItemStack} if found, otherwise null.
     */
    @Nullable
    ItemStack getItem(@NonNull String id);

    /**
     * Gets the unique identifier for a specific ItemStack if it belongs to this
     * provider.
     *
     * @param item The item stack to check.
     * @return The identifier string (without prefix, e.g., "ruby"), or null if it
     *         doesn't belong to this provider.
     */
    @Nullable
    String getItemId(@NonNull ItemStack item);

    /**
     * Returns the unique prefix for this provider (e.g., "oraxen", "itemsadder").
     * Used to avoid identifier collisions (e.g., "oraxen:ruby").
     *
     * @return The prefix string.
     */
    @NonNull
    String getPrefix();

}
