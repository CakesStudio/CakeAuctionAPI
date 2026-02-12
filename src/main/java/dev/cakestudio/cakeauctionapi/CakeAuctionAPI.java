package dev.cakestudio.cakeauctionapi;

import dev.cakestudio.cakeauctionapi.api.ICakeAuctionAPI;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CakeAuctionAPI extends JavaPlugin {

    private static ICakeAuctionAPI implementation;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("CakeAuctionAPI successfully enabled");
    }

    public static void registerImplementation(@NonNull final ICakeAuctionAPI api) {
        if (implementation != null) return;
        implementation = api;
    }

    public static ICakeAuctionAPI getApi() {
        if (implementation == null) {
            throw new IllegalStateException("CakeAuction is not loaded.");
        }
        return implementation;
    }
}
