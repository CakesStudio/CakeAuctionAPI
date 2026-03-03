package dev.cakestudio.cakeauctionapi.api.event;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player or administrator attempts to take an auction item.
 * Can be cancelled to prevent removal from the auction house.
 */
@Getter
public class AuctionItemTakeProcessEvent extends AuctionCancellableEvent {
    private final Player user;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param user The player attempting to take the item.
     * @param item The auction item.
     */
    public AuctionItemTakeProcessEvent(@NonNull Player user, @NonNull IAuctionItem item) {
        this.user = user;
        this.item = item;
    }

}