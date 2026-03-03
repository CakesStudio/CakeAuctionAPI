package dev.cakestudio.cakeauctionapi.api.action;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import lombok.NonNull;

/**
 * Represents a functional action that can be executed for a player.
 */
@FunctionalInterface
public interface IAction {

    /**
     * Executes the action.
     *
     * @param player         The player who triggered the action.
     * @param actionLocation The location where the action occurs.
     * @param parsedText     The text associated with the action, with placeholders already parsed.
     */
    void run(@NonNull Player player, @NonNull Location actionLocation, @NonNull String parsedText);

}