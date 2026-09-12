package dev.cakestudio.cakeauctionapi.api.manager.ai;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for interacting with the AI intelligence subsystem of CakeAuction.
 * Provides access to price advisory, smart semantic search, market digest, and safety features.
 */
public interface IAIManager {

    /**
     * Checks if AI integration is globally enabled in settings/ai.yml.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Checks if a player has permissions and is not on cooldown or daily limit for an AI feature.
     *
     * @param player      The player to check.
     * @param featureName The feature identifier (e.g. "price_advisor", "smart_search", "deal_finder").
     * @return true if the player can use the feature right now.
     */
    boolean canUseFeature(Player player, String featureName);

    /**
     * Evaluates an item to determine suggested market prices (fast, fair, max) and trading advice.
     *
     * @param item       The item to analyze.
     * @param currencyId The currency identifier (e.g. "vault", "playerpoints").
     * @return A CompletableFuture containing the price valuation result.
     */
    CompletableFuture<IAiPriceAdvice> advisePrice(ItemStack item, String currencyId);

    /**
     * Translates a natural language search query (in Russian, English, or slang) into concise keywords and categories.
     *
     * @param query The natural language user query.
     * @return A CompletableFuture containing the parsed search result.
     */
    CompletableFuture<IAiSearchResult> parseSearchQuery(String query);

    /**
     * Generates a market digest/news summary for the past 24 hours based on server trading activity.
     *
     * @return A CompletableFuture containing the text digest.
     */
    CompletableFuture<String> generateMarketDigest();

}