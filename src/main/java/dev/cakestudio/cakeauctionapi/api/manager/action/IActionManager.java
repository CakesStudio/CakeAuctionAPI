package dev.cakestudio.cakeauctionapi.api.manager.action;

import dev.cakestudio.cakeauctionapi.api.action.IAction;

import lombok.NonNull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Manages custom actions that can be triggered from configuration files.
 */
public interface IActionManager {

    /**
     * Registers a new action with a specific tag.
     * <p>
     * Example: if tag is "MESSAGE", it can be used in config as [MESSAGE] Hello!
     *
     * @param tag    The tag for the action (without brackets).
     * @param action The action implementation.
     */
    void registerAction(@NonNull String tag, @NonNull IAction action);

    /**
     * Unregisters an action by its tag.
     *
     * @param tag The tag.
     */
    void unregisterAction(@NonNull String tag);

    /**
     * Runs an action for a player.
     *
     * @param player   The player who triggers the action.
     * @param location The location context for the action (e.g., sound source).
     * @param tag      The action tag (e.g., "OPEN_MENU").
     * @param data     The data for the action (e.g., "main-menu").
     */
    void runAction(@NonNull Player player, @NonNull Location location, @NonNull String tag, @NonNull String data);

}