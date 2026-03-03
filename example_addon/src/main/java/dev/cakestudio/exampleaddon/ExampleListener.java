package dev.cakestudio.exampleaddon;

import dev.cakestudio.cakeauctionapi.api.event.AuctionItemBuyEvent;

import lombok.RequiredArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Listens for CakeAuction API events.
 */
@RequiredArgsConstructor
public class ExampleListener implements Listener {

    private final ExampleAddon addon;

    @EventHandler
    public void onAuctionBuy(AuctionItemBuyEvent event) {
        if (!addon.getConfig().getBoolean("enabled", true)) return;

        String player = event.getBuyer().getName();
        String itemName = event.getItem().getItemStack().getType().name();

        String message = addon.getMessage("broadcast-buy")
                .replace("{player}", player)
                .replace("{item}", itemName);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}