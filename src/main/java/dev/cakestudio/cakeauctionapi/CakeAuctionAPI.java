package dev.cakestudio.cakeauctionapi;

import dev.cakestudio.cakeauctionapi.api.ICakeAuctionAPI;

import lombok.NonNull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main API entry point for CakeAuction.
 * This class provides access to the API implementation and handles its registration.
 */
public final class CakeAuctionAPI extends JavaPlugin {

    private static ICakeAuctionAPI implementation;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("CakeAuctionAPI successfully enabled");
    }

    /**
     * Registers the API implementation.
     * Internal use only (called by the main plugin).
     *
     * @param api The implementation instance.
     */
    public static void registerImplementation(@NonNull final ICakeAuctionAPI api) {
        if (implementation != null) return;
        implementation = api;
    }

    /**
     * Unregisters the API implementation.
     * Internal use only (called by the main plugin).
     */
    public static void unregisterImplementation() {
        implementation = null;
    }

    /**
     * Gets the current CakeAuction API instance.
     *
     * @return The active {@link ICakeAuctionAPI}.
     * @throws IllegalStateException if the plugin is not loaded yet.
     */
    public static ICakeAuctionAPI getApi() {
        if (implementation == null) {
            throw new IllegalStateException("CakeAuction is not loaded.");
        }
        return implementation;
    }

}