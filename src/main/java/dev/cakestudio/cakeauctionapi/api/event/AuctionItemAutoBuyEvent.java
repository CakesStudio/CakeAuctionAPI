package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

/**
 * Fired when an auction item is automatically purchased (e.g. by a system or bot).
 */
@Getter
public class AuctionItemAutoBuyEvent extends AuctionEvent {
    private final IAuctionItem item;
    private final double payout;

    /**
     * Constructs the event.
     *
     * @param item   The auction item purchased.
     * @param payout The amount paid for the item.
     */
    public AuctionItemAutoBuyEvent(@NonNull IAuctionItem item, double payout) {
        this.item = item;
        this.payout = payout;
    }

}