package dev.cakestudio.cakeauctionapi.api.manager.ai;

/**
 * Represents the result of an AI price valuation for an item.
 */
public interface IAiPriceAdvice {

    /**
     * Checks if the AI evaluation was successful.
     *
     * @return true if successful, false if an error occurred.
     */
    boolean isSuccess();

    /**
     * Gets the name of the evaluated item.
     *
     * @return The item display name or material name.
     */
    String getItemName();

    /**
     * Gets the suggested fast sell price (~70-80% of fair value for quick liquidity).
     *
     * @return The fast sell price.
     */
    double getFastPrice();

    /**
     * Gets the optimal fair market price calculated by the AI.
     *
     * @return The fair market price.
     */
    double getFairPrice();

    /**
     * Gets the optimistic top sale price.
     *
     * @return The maximum recommended price.
     */
    double getMaxPrice();

    /**
     * Gets the confidence level of the prediction (0 to 100).
     *
     * @return Confidence percentage.
     */
    int getConfidence();

    /**
     * Gets concise economic advice or recommendations from the AI.
     *
     * @return The advice string.
     */
    String getAdvice();

    /**
     * Gets the error message if the evaluation failed.
     *
     * @return Error message, or null if successful.
     */
    String getErrorMessage();

}