package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionCancellableEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player attempts to collect an unsold or expired auction item.
 * Can be cancelled to prevent the item from being returned to the player.
 */
@Getter
public class AuctionItemTakeUnsoldProcessEvent extends AuctionCancellableEvent {
    private final Player user;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param user The player attempting the collection.
     * @param item The unsold auction item.
     */
    public AuctionItemTakeUnsoldProcessEvent(@NonNull Player user, @NonNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

}