package dev.cakestudio.cakeauctionapi.api.event;

import lombok.Getter;

/**
 * Event called when all auction items have been asynchronously loaded from the database into the cache.
 * This event is fired asynchronously.
 */
@Getter
public class AuctionItemsLoadedEvent extends AuctionEvent {

    private final int totalItems;
    private final long loadTimeMs;

    public AuctionItemsLoadedEvent(int totalItems, long loadTimeMs) {
        super(true); // Is async
        this.totalItems = totalItems;
        this.loadTimeMs = loadTimeMs;
    }

}