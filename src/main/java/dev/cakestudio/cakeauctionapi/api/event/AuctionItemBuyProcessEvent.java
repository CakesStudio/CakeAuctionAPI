package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuctionItemBuyProcessEvent extends AuctionCancellableEvent {
    private final Player buyer;
    private final IAuctionItem item;

    public AuctionItemBuyProcessEvent(@NotNull Player buyer, @NotNull IAuctionItem item) {
        this.buyer = buyer;
        this.item = item;
    }

    public Player getBuyer() {
        return buyer;
    }

    public IAuctionItem getItem() {
        return item;
    }
}
