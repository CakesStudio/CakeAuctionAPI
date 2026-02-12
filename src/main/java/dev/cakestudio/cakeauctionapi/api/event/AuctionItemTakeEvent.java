package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AuctionItemTakeEvent extends AuctionEvent {
    private final Player user;
    private final IAuctionItem item;

    public AuctionItemTakeEvent(@NotNull Player user, @NotNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

    public Player getUser() {
        return user;
    }

    public IAuctionItem getItem() {
        return item;
    }
}
