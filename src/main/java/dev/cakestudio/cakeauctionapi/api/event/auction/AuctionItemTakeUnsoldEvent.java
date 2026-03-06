package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired after a player collects their unsold/expired auction item.
 */
@Getter
public class AuctionItemTakeUnsoldEvent extends AuctionEvent {
    private final Player user;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param user The player collecting the item.
     * @param item The unsold auction item.
     */
    public AuctionItemTakeUnsoldEvent(@NonNull Player user, @NonNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

}