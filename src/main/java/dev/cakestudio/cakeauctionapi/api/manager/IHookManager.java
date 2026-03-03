package dev.cakestudio.cakeauctionapi.api.manager;

import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for interacting with external plugin hooks (ItemsAdder, etc.).
 */
public interface IHookManager {

    /**
     * Gets an ItemStack by its identifier (e.g., "itemsadder:ruby" or "DIAMOND").
     *
     * @param id The identifier.
     * @return The {@link ItemStack} if found, otherwise null.
     */
    @Nullable
    ItemStack getItem(@Nullable String id);

    /**
     * Gets the identifier for a specific ItemStack.
     *
     * @param item The item stack.
     * @return The unique ID string or null.
     */
    @Nullable
    String getItemId(@Nullable ItemStack item);

}