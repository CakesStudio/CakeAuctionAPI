package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemBuyProcessEvent extends AuctionCancellableEvent {
    private final Player buyer;
    private final IAuctionItem item;

    public AuctionItemBuyProcessEvent(@NonNull Player buyer, @NonNull IAuctionItem item) {
        this.buyer = buyer;
        this.item = item;
    }

}