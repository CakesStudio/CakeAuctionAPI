package dev.cakestudio.cakeauctionapi.api.event;

import org.bukkit.event.Cancellable;

import org.jetbrains.annotations.Nullable;

/**
 * Base class for auction events that can be cancelled by listeners.
 * Provides an optional reason for cancellation.
 */
public abstract class AuctionCancellableEvent extends AuctionEvent implements Cancellable {
    private boolean cancelled;
    private String reason;

    /**
     * Constructs a new synchronous cancellable auction event.
     */
    public AuctionCancellableEvent() {}

    /**
     * Constructs a new cancellable auction event with specified synchronicity.
     *
     * @param isAsync true if the event is fired asynchronously.
     */
    public AuctionCancellableEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the reason why the event was cancelled, if provided.
     *
     * @return The cancellation reason or null.
     */
    @Nullable
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for cancellation. 
     * Usually displayed to the player.
     *
     * @param reason The reason string.
     */
    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

}