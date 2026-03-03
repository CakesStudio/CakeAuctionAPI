package dev.cakestudio.cakeauctionapi.api.event;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.entity.Player;

/**
 * Fired when a player uses an auction pass item (for tax reduction or duration increase).
 */
@Getter
public class AuctionPassUseEvent extends AuctionEvent {
    private final Player player;
    private final double percent;
    private final long duration;

    /**
     * Constructs the event.
     *
     * @param player   The player who used the pass.
     * @param percent  The percentage bonus/reduction from the pass.
     * @param duration The duration increase provided by the pass.
     */
    public AuctionPassUseEvent(@NonNull Player player, double percent, long duration) {
        this.player = player;
        this.percent = percent;
        this.duration = duration;
    }

}