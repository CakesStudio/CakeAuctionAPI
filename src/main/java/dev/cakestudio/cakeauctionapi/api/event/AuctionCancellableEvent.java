package dev.cakestudio.cakeauctionapi.api.event;

import org.bukkit.event.Cancellable;

import org.jetbrains.annotations.Nullable;

public abstract class AuctionCancellableEvent extends AuctionEvent implements Cancellable {
    private boolean cancelled;
    private String reason;

    public AuctionCancellableEvent() {}

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

    @Nullable
    public String getReason() {
        return reason;
    }

    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

}