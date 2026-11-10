package dev.cakestudio.cakeauctionapi.api.manager.history;

import dev.cakestudio.cakeauctionapi.api.data.IHistoryRecord;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Manager for handling historical auction transactions, sales records, and statistics.
 */
public interface IHistoryManager {

    /**
     * Gets paginated sales history records for a player.
     *
     * @param playerUuid The UUID of the seller.
     * @param offset     The zero-based record offset.
     * @param limit      The maximum number of records to return.
     * @return A list of history records.
     */
    default List<IHistoryRecord> getSalesHistory(UUID playerUuid, int offset, int limit) {
        return Collections.emptyList();
    }

    /**
     * Gets paginated purchase history records for a player.
     *
     * @param playerUuid The UUID of the buyer.
     * @param offset     The zero-based record offset.
     * @param limit      The maximum number of records to return.
     * @return A list of history records.
     */
    default List<IHistoryRecord> getPurchasesHistory(UUID playerUuid, int offset, int limit) {
        return Collections.emptyList();
    }

    /**
     * Gets all sales records for a player that occurred on or after a given timestamp.
     *
     * @param playerUuid     The UUID of the seller.
     * @param sinceTimestamp The epoch timestamp in milliseconds.
     * @return A list of matching history records.
     */
    default List<IHistoryRecord> getSalesSince(UUID playerUuid, long sinceTimestamp) {
        return Collections.emptyList();
    }

    /**
     * Gets all purchase records for a player that occurred on or after a given timestamp.
     *
     * @param playerUuid     The UUID of the buyer.
     * @param sinceTimestamp The epoch timestamp in milliseconds.
     * @return A list of matching history records.
     */
    default List<IHistoryRecord> getPurchasesSince(UUID playerUuid, long sinceTimestamp) {
        return Collections.emptyList();
    }

}