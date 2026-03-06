package dev.cakestudio.cakeauctionapi.api.manager.util;

import java.util.UUID;

/**
 * Manager for generating and handling unique identifiers.
 * Provides support for UUID v7 (time-ordered) and short IDs.
 */
public interface IUUIDManager {

    /**
     * Generates a standard UUID v7 (Time-ordered).
     *
     * @return A new time-ordered {@link UUID}.
     */
    UUID random();

    /**
     * Generates a custom UUID v7 with an embedded server identifier.
     * Useful for cross-server auctions to prevent collisions.
     *
     * @param serverIdentifier A unique string identifying the server.
     * @return A new {@link UUID} containing the server hash.
     */
    UUID generate(String serverIdentifier);

    /**
     * Converts a UUID into a short, URL-friendly Base62 string.
     *
     * @param uuid The UUID to convert.
     * @return A short Base62 representation of the UUID.
     */
    String toShortId(UUID uuid);

}