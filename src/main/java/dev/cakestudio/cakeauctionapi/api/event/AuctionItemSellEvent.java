package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuctionItemSellEvent extends AuctionEvent {
    private final Player seller;
    private final IAuctionItem item;

    public AuctionItemSellEvent(@NotNull Player seller, @NotNull IAuctionItem item) {
        this.seller = seller;
        this.item = item;
    }

    public Player getSeller() {
        return seller;
    }

    public IAuctionItem getItem() {
        return item;
    }
}
