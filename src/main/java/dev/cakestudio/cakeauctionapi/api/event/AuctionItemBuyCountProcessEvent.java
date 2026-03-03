package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player attempts to partially buy items from an auction lot.
 * Can be cancelled to prevent the transaction.
 */
@Getter
public class AuctionItemBuyCountProcessEvent extends AuctionCancellableEvent {
    private final Player buyer;
    private final IAuctionItem item;
    private final int count;

    /**
     * Constructs the event.
     *
     * @param buyer The player attempting the purchase.
     * @param item  The auction lot.
     * @param count The number of items to buy.
     */
    public AuctionItemBuyCountProcessEvent(@NonNull Player buyer, @NonNull IAuctionItem item, int count) {
        this.buyer = buyer;
        this.item = item;
        this.count = count;
    }

}