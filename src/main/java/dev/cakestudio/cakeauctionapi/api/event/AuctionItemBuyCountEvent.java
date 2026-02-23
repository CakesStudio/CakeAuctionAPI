package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemBuyCountEvent extends AuctionEvent {
    private final Player buyer;
    private final IAuctionItem item;
    private final int count;

    public AuctionItemBuyCountEvent(@NonNull Player buyer, @NonNull IAuctionItem item, int count) {
        this.buyer = buyer;
        this.item = item;
        this.count = count;
    }

}