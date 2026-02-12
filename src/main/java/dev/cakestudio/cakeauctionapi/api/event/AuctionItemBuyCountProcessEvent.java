package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuctionItemBuyCountProcessEvent extends AuctionCancellableEvent {
    private final Player buyer;
    private final IAuctionItem item;
    private final int count;

    public AuctionItemBuyCountProcessEvent(@NotNull Player buyer, @NotNull IAuctionItem item, int count) {
        this.buyer = buyer;
        this.item = item;
        this.count = count;
    }

    public Player getBuyer() {
        return buyer;
    }

    public IAuctionItem getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
}
