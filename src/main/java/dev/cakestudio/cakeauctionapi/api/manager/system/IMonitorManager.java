package dev.cakestudio.cakeauctionapi.api.manager.system;

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

}