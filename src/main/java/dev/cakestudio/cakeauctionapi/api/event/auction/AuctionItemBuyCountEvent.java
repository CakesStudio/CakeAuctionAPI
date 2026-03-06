package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired after a player successfully completes a partial purchase of an auction lot.
 */
@Getter
public class AuctionItemBuyCountEvent extends AuctionEvent {
    private final Player buyer;
    private final IAuctionItem item;
    private final int count;

    /**
     * Constructs the event.
     *
     * @param buyer The player who bought the items.
     * @param item  The auction lot.
     * @param count The number of items purchased.
     */
    public AuctionItemBuyCountEvent(@NonNull Player buyer, @NonNull IAuctionItem item, int count) {
        this.buyer = buyer;
        this.item = item;
        this.count = count;
    }

}