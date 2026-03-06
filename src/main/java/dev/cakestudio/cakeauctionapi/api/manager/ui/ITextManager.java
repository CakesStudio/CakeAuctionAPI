package dev.cakestudio.cakeauctionapi.api.manager.ui;

import net.kyori.adventure.text.Component;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Manages text parsing, coloring, and messaging.
 * Supports HEX, MiniMessage, and PlaceholderAPI.
 */
public interface ITextManager {

    /**
     * Parses a string into an Adventure Component.
     * Handles HEX patterns and PlaceholderAPI if available.
     *
     * @param text   The raw text to parse.
     * @param target The player for placeholder context (optional).
     * @return The parsed {@link Component}.
     */
    @NonNull
    Component parse(@NonNull String text, @Nullable OfflinePlayer target);

    /**
     * Parses a list of strings into a list of Adventure Components.
     *
     * @param text   The raw text list.
     * @param target The player for placeholder context (optional).
     * @return A list of parsed {@link Component}s.
     */
    @NonNull
    List<Component> parseList(@NonNull List<String> text, @Nullable OfflinePlayer target);

    /**
     * Sends a parsed message to a player.
     *
     * @param target The recipient.
     * @param msg    The raw message string.
     */
    void sendMessage(@NonNull Player target, @NonNull String msg);

    /**
     * Sends a parsed message to a command sender (player or console).
     *
     * @param target The recipient.
     * @param msg    The raw message string.
     */
    void sendMessage(@NonNull CommandSender target, @NonNull String msg);

    /**
     * Broadcasts a message to all online players.
     *
     * @param msg The raw message string.
     */
    void broadcast(@NonNull String msg);

    /**
     * Sends a colored message to the console.
     *
     * @param msg The raw message string.
     */
    void sendConsole(@NonNull String msg);

    /**
     * Strips all color codes and formatting from a string.
     *
     * @param text The text to strip.
     * @return The plain text string.
     */
    @NonNull
    String stripColor(@NonNull String text);

}