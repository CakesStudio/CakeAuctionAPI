package dev.cakestudio.aucaddon;

import dev.cakestudio.cakeauctionapi.addon.AbstractAddon;
import dev.cakestudio.cakeauctionapi.api.ICakeAuctionAPI;
import dev.cakestudio.cakeauctionapi.api.data.IAuctionItem;
import dev.cakestudio.cakeauctionapi.api.manager.ITextManager;
import dev.cakestudio.cakeauctionapi.api.manager.auction.IItemHookProvider;

import lombok.Getter;
import lombok.NonNull;

import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;

@Getter
public final class AucAddon extends AbstractAddon {

    private ITextManager textManager;
    private IItemHookProvider myItemProvider;

    @Override
    protected void onEnable() {
        saveDefaultConfig();

        ICakeAuctionAPI cakeAuctionAPI = getApi();
        textManager = cakeAuctionAPI.getTextManager();

        textManager.sendConsole("<green>AucAddon v" + getDescription().version() + " enabled!</green>");

        registerListener(new AucListener(this));

        if (cakeAuctionAPI.isAuctionLoaded()) {
            int activeLots = cakeAuctionAPI.getActiveAuctions().size();
            textManager.sendConsole("<yellow>Current active auction lots: " + activeLots + "</yellow>");
        }

        myItemProvider = new IItemHookProvider() {
            @Override
            public @Nullable ItemStack getItem(@NonNull String id) {
                if (id.equalsIgnoreCase("mythic_sword")) {
                    ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                    ItemMeta meta = sword.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName("Mythic Sword of Souls");
                        meta.getPersistentDataContainer().set(
                                new NamespacedKey("aucaddon", "item_tier"),
                                PersistentDataType.STRING,
                                "mythic");
                        sword.setItemMeta(meta);
                    }
                    return sword;
                }
                return null;
            }

            @Override
            public @Nullable String getItemId(@NonNull ItemStack item) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(
                        new NamespacedKey("aucaddon", "item_tier"),
                        PersistentDataType.STRING)) {
                    String tier = meta.getPersistentDataContainer().get(
                            new NamespacedKey("aucaddon", "item_tier"),
                            PersistentDataType.STRING);
                    if ("mythic".equals(tier)) {
                        return "mythic_sword";
                    }
                }
                return null;
            }

            @Override
            public @NonNull String getPrefix() {
                return "mycustomitems";
            }
        };
        cakeAuctionAPI.getHookManager().registerProvider(myItemProvider);
        textManager.sendConsole(
                "<green>[AucAddon] Registered item hook provider: " + myItemProvider.getPrefix() + "</green>");

        ItemStack categoryIcon = new ItemStack(Material.NETHERITE_SWORD);
        cakeAuctionAPI.getAuctionManager().registerCategory(
                "mythic_gear",
                "<gradient:#ffd700:#ff4500>Mythic Gear</gradient>",
                categoryIcon,
                itemStack -> {
                    if (itemStack == null || !itemStack.hasItemMeta())
                        return false;
                    ItemMeta meta = itemStack.getItemMeta();
                    return meta != null && meta.getPersistentDataContainer().has(
                            new NamespacedKey("aucaddon", "item_tier"),
                            PersistentDataType.STRING);
                });
        textManager.sendConsole("<green>[AucAddon] Registered custom category 'mythic_gear'!</green>");
        cakeAuctionAPI.getAuctionManager().registerSortingType(
                "seller_name_length",
                "<aqua>Seller Name Length</aqua>",
                Comparator.comparingInt(item -> item.getSellerName().length()));
        textManager.sendConsole("<green>[AucAddon] Registered custom sorting 'seller_name_length'!</green>");
    }

    @Override
    protected void onDisable() {
        textManager.sendConsole("<red>AucAddon disabled. Cleaning up...</red>");

        ICakeAuctionAPI cakeAuctionAPI = getApi();
        if (cakeAuctionAPI != null) {
            if (myItemProvider != null && cakeAuctionAPI.getHookManager() != null) {
                cakeAuctionAPI.getHookManager().unregisterProvider(myItemProvider);
            }

            if (cakeAuctionAPI.getAuctionManager() != null) {
                cakeAuctionAPI.getAuctionManager().unregisterCategory("mythic_gear");
                cakeAuctionAPI.getAuctionManager().unregisterSortingType("seller_name_length");
            }
        }
    }

    public String getMessage(String path) {
        return getConfig().getString("messages." + path, "Message not found: " + path);
    }

}