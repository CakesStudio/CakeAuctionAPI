package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionItemTakeUnsoldProcessEvent extends AuctionCancellableEvent {
    private final Player user;
    private final IAuctionItem item;

    public AuctionItemTakeUnsoldProcessEvent(@NonNull Player user, @NonNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

}