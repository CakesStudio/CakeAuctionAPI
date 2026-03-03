package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired after an auction item has been taken back by a player or administrator.
 */
@Getter
public class AuctionItemTakeEvent extends AuctionEvent {
    private final Player user;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param user The player who took the item.
     * @param item The auction item taken.
     */
    public AuctionItemTakeEvent(@NonNull Player user, @NonNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

}