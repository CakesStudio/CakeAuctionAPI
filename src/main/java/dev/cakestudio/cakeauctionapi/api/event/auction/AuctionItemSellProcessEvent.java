package dev.cakestudio.cakeauctionapi.api.event.auction;

import dev.cakestudio.cakeauctionapi.api.event.AuctionCancellableEvent;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player attempts to list an item for sale on the auction house.
 * Can be cancelled to prevent the listing.
 */
@Getter
public class AuctionItemSellProcessEvent extends AuctionCancellableEvent {
    private final Player seller;
    private final IAuctionItem item;

    /**
     * Constructs the event.
     *
     * @param seller The player attempting to sell.
     * @param item   The auction item to be created.
     */
    public AuctionItemSellProcessEvent(@NonNull Player seller, @NonNull IAuctionItem item) {
        this.seller = seller;
        this.item = item;
    }

}