package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired after a player successfully lists an item for sale.
 */
@Getter
public class AuctionItemSellEvent extends AuctionEvent {
    private final Player seller;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param seller The player who listed the item.
     * @param item   The created auction item.
     */
    public AuctionItemSellEvent(@NonNull Player seller, @NonNull IAuctionItem item) {
        this.seller = seller;
        this.item = item;
    }

}