package dev.cakestudio.cakeauctionapi.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.NonNull;

public abstract class AuctionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public AuctionEvent() {}

    public AuctionEvent(boolean isAsync) {
        super(isAsync);
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NonNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}