package dev.cakestudio.cakeauctionapi.api.manager.system;

import java.util.function.BiConsumer;

/**
 * Manager for handling network packets and cross-server communication.
 * Supports standard auction packets and custom developer-defined channels.
 */
public interface INetworkManager {

    /**
     * Sends a built-in internal packet to other servers.
     *
     * @param packet The internal packet object.
     */
    void sendPacket(Object packet);

    /**
     * Sends a custom message to a specific channel across the network.
     *
     * @param channel The channel name (e.g., "my_addon:global_update").
     * @param data    The raw data to send as a byte array.
     */
    void sendCustomPacket(String channel, byte[] data);

    /**
     * Registers a listener for custom network packets on a specific channel.
     *
     * @param channel  The channel name to listen to.
     * @param listener A consumer that receives (originServerName, dataBytes).
     */
    void registerCustomPacketHandler(String channel, BiConsumer<String, byte[]> listener);

    /**
     * Checks if Direct TCP socket synchronization is enabled.
     *
     * @return true if Direct TCP transport is active, false otherwise.
     */
    default boolean isDirectTcpEnabled() {
        return false;
    }

    /**
     * Gets the active network transport type (e.g. "DIRECT_TCP", "REDIS", "BUNGEE", or "NONE").
     *
     * @return The transport type identifier.
     */
    default String getNetworkType() {
        return "NONE";
    }

}