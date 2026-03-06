package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired after a player successfully purchases a full auction lot.
 */
@Getter
public class AuctionItemBuyEvent extends AuctionEvent {
    private final Player buyer;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param buyer The player who purchased the item.
     * @param item  The auction item.
     */
    public AuctionItemBuyEvent(@NonNull Player buyer, @NonNull IAuctionItem item) {
        this.buyer = buyer;
        this.item = item;
    }

}