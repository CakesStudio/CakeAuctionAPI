package dev.cakestudio.cakeauctionapi.api;

import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ICakeAuctionAPI {
    Collection<IAuctionItem> getActiveAuctions();
    Optional<IAuctionItem> getAuctionById(UUID id);
    void createAuction(Player seller, ItemStack item, double price, long durationSeconds, boolean onlyFull);
    void buyItem(Player buyer, IAuctionItem item);
    void buyItemPartial(Player buyer, IAuctionItem item, int count, double priceForCount);
    void cancelAuction(Player seller, IAuctionItem item);
}
