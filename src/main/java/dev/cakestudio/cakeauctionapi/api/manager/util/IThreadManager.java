package dev.cakestudio.cakeauctionapi.api.manager.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Manager for handling multi-threaded operations and asynchronous tasks.
 * Provides wrappers for running tasks sync/async or for database operations.
 */
public interface IThreadManager {

    /**
     * Executes an operation on the main Bukkit thread.
     * If the current thread is the main thread, the operation runs immediately.
     *
     * @param operation The task to execute.
     */
    void runSync(Runnable operation);

    /**
     * Executes an operation asynchronously.
     *
     * @param operation The task to execute.
     */
    void runAsync(Runnable operation);

    /**
     * Executes a database-related operation asynchronously.
     *
     * @param dbOperation The database task to execute.
     * @param uiCallback  Optional callback to be executed on the main thread after the database task finishes.
     */
    void runDatabaseOperation(Runnable dbOperation, Runnable uiCallback);

    /**
     * Executes a database-related operation asynchronously and returns a result.
     *
     * @param dbOperation The database task that returns a value.
     * @param uiCallback  Callback to be executed on the main thread with the result from the database task.
     * @param <T>         The result type.
     */
    <T> void runDatabaseOperation(Supplier<T> dbOperation, Consumer<T> uiCallback);

    /**
     * Checks if the current thread is the main Bukkit thread.
     *
     * @return true if the current thread is the main thread, false otherwise.
     */
    boolean isMainThread();

}