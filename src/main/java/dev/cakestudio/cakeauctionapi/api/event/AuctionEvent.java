package dev.cakestudio.cakeauctionapi.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.NonNull;

/**
 * Base class for all auction-related events in CakeAuction.
 */
public abstract class AuctionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new synchronous auction event.
     */
    public AuctionEvent() {}

    /**
     * Constructs a new auction event with specified synchronicity.
     *
     * @param isAsync true if the event is fired asynchronously.
     */
    public AuctionEvent(boolean isAsync) {
        super(isAsync);
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the handler list for this event.
     *
     * @return The static handler list.
     */
    @NonNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}