package dev.cakestudio.cakeauctionapi.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class AuctionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public AuctionEvent() {}

    public AuctionEvent(boolean isAsync) {
        super(isAsync);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
