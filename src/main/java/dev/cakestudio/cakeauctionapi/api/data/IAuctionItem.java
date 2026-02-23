package dev.cakestudio.cakeauctionapi.api.data;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface IAuctionItem {

    UUID getId();

    String getShortId();

    UUID getSellerUuid();

    String getSellerName();

    ItemStack getItemStack();

    double getPrice();

    long getCreatedAt();

    long getExpiresAt();

    String getOriginServer();

    boolean isSold();

    boolean isExpired();

    String getBuyerName();

    boolean isOnlyFull();

    boolean isActive();

}