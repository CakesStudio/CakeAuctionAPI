package dev.cakestudio.cakeauctionapi.api.manager.ai;

/**
 * Represents the translated semantic search query result from AI.
 */
public interface IAiSearchResult {

    /**
     * Gets the extracted keyword or material name to search for.
     *
     * @return The keyword (usually uppercase material name or search term).
     */
    String getKeyword();

    /**
     * Gets the suggested auction category (e.g. ALL, WEAPONS, ARMOR, TOOLS, BLOCKS, CONSUMABLES).
     *
     * @return The category name, or "ALL" if not category-specific.
     */
    String getCategory();

}