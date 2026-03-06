package dev.cakestudio.cakeauctionapi.api.manager.system;

import java.sql.Connection;
import java.util.function.Consumer;

/**
 * Manager for handling database connections and low-level data operations.
 * Allows addons to perform migrations, run custom queries, and interact with the data source.
 */
public interface IDatabaseManager {

    /**
     * Executes a consumer with a valid database connection.
     * The connection is automatically closed after the consumer finishes.
     *
     * @param connectionConsumer The action to perform with the connection.
     */
    void useConnection(Consumer<Connection> connectionConsumer);

    /**
     * Checks if the database is using a remote driver (MySQL, MariaDB, etc.).
     *
     * @return true if remote, false if local (SQLite).
     */
    boolean isRemote();

    /**
     * Gets the unique identifier for this server instance in the database.
     *
     * @return The server identifier string.
     */
    String getServerIdentifier();

    /**
     * Checks if this instance is the "head" node in a multi-server setup.
     *
     * @return true if it's the head node.
     */
    boolean isHead();

}