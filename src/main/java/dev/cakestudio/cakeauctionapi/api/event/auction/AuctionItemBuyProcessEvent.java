package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionCancellableEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player attempts to purchase a full auction lot.
 * Can be cancelled to prevent the purchase.
 */
@Getter
public class AuctionItemBuyProcessEvent extends AuctionCancellableEvent {
    private final Player buyer;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param buyer The player attempting the purchase.
     * @param item  The auction item.
     */
    public AuctionItemBuyProcessEvent(@NonNull Player buyer, @NonNull IAuctionItem item) {
        this.buyer = buyer;
        this.item = item;
    }

}