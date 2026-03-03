package dev.cakestudio.exampleaddon;

import dev.cakestudio.cakeauctionapi.addon.AbstractAddon;

import dev.cakestudio.exampleaddon.ExampleListener;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Main class for the ExampleAddon.
 */
public class ExampleAddon extends AbstractAddon {

    @Override
    protected void onEnable() {
        // Automatically creates the data folder and copies config.yml from jar resources
        saveDefaultConfig();

        getLogger().info("ExampleAddon v" + getDescription().version() + " enabled!");

        // Register a Bukkit listener (auto-unregistered on disable)
        registerListener(new ExampleListener(this));

        // Using ICakeAuctionAPI
        int activeLots = getApi().getActiveAuctions().size();
        getLogger().info("Current active auction lots: " + activeLots);
    }

    @Override
    protected void onDisable() {
        getLogger().info("ExampleAddon disabled. Cleaning up...");
        // Commands and listeners are cleared automatically by AbstractAddon
    }

    public String getMessage(String path) {
        return getConfig().getString("messages." + path, "Message not found: " + path);
    }

}