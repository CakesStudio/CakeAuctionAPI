package dev.cakestudio.cakeauctionapi.api.manager;

import dev.cakestudio.cakeauctionapi.api.action.IAction;

import lombok.NonNull;

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

}