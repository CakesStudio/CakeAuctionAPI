package dev.cakestudio.cakeauctionapi.api.event;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

@Getter
public class AuctionPassUseEvent extends AuctionEvent {
    private final Player player;
    private final double percent;
    private final long duration;

    public AuctionPassUseEvent(@NonNull Player player, double percent, long duration) {
        this.player = player;
        this.percent = percent;
        this.duration = duration;
    }

}