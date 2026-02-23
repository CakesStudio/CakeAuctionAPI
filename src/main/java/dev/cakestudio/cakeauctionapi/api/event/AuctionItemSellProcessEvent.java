package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemSellProcessEvent extends AuctionCancellableEvent {
    private final Player seller;
    private final IAuctionItem item;

    public AuctionItemSellProcessEvent(@NonNull Player seller, @NonNull IAuctionItem item) {
        this.seller = seller;
        this.item = item;
    }

}