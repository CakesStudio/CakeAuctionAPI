package dev.cakestudio.cakeauctionapi.api.manager.system;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manager for system monitoring, performance metrics, and safety checks.
 * Provides tools for logging, detecting resource leaks, and analyzing performance stress.
 */
public interface IMonitorManager {

    /**
     * Checks if the system is currently under high stress.
     *
     * @return true if the server is experiencing high performance load, false otherwise.
     */
    boolean isUnderStress();

    /**
     * Gets the current server performance rating (TPS/MSPT based).
     *
     * @return A performance score (0.0 to 1.0).
     */
    double getPerformanceScore();

    /**
     * Adds a custom entry to the internal system log.
     *
     * @param module  The name of the module generating the log.
     * @param message The log message to record.
     */
    void log(String module, String message);

    /**
     * Triggers a leak detection check for common resources (DB connections, listeners).
     */
    void runLeakCheck();

    /**
     * Checks if a specific action or operation is considered "safe" to run under current conditions.
     *
     * @param context A string describing the operation.
     * @return true if the operation is safe to proceed, false if it should be delayed or cancelled.
     */
    boolean isSafe(String context);

    /**
     * Opens the interactive system status GUI menu for a player.
     *
     * @param player The player.
     */
    default void openStatusMenu(Player player) {}

    /**
     * Sends the text-based status report to a command sender or console.
     *
     * @param sender The sender.
     */
    default void sendStatusReport(CommandSender sender) {}

    /**
     * Checks if a stress test is currently running.
     *
     * @return true if running, false otherwise.
     */
    default boolean isStressTestRunning() {
        return false;
    }

    /**
     * Stops the currently running stress test if one is active.
     */
    default void stopStressTest() {}

}