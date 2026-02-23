package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemSellEvent extends AuctionEvent {
    private final Player seller;
    private final IAuctionItem item;

    public AuctionItemSellEvent(@NonNull Player seller, @NonNull IAuctionItem item) {
        this.seller = seller;
        this.item = item;
    }

}