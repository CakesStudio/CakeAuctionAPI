package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemBuyEvent extends AuctionEvent {
    private final Player buyer;
    private final IAuctionItem item;

    public AuctionItemBuyEvent(@NonNull Player buyer, @NonNull IAuctionItem item) {
        this.buyer = buyer;
        this.item = item;
    }

}