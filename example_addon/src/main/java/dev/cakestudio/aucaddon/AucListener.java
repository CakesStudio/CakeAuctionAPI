package dev.cakestudio.aucaddon;

import dev.cakestudio.cakeauctionapi.api.event.AuctionItemSellEvent;

import lombok.RequiredArgsConstructor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AucListener implements Listener {

    private final AucAddon addon;

    @EventHandler
    public void onAuctionSell(AuctionItemSellEvent event) {
        if (!addon.getConfig().getBoolean("enabled", true)) return;

        String player = event.getSeller().getName();
        String itemName = event.getItem().getItemStack().getType().name();

        String message = addon.getMessage("broadcast-sell")
                .replace("{player}", player)
                .replace("{item}", itemName);

        addon.getTextManager().broadcast(message);
    }

}