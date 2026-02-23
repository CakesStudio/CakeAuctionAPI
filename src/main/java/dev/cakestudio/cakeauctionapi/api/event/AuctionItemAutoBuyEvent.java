package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class AuctionItemAutoBuyEvent extends AuctionEvent {
    private final IAuctionItem item;
    private final double payout;

    public AuctionItemAutoBuyEvent(@NonNull IAuctionItem item, double payout) {
        this.item = item;
        this.payout = payout;
    }

}