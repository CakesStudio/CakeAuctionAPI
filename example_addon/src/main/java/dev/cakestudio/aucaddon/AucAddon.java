package dev.cakestudio.aucaddon;

import dev.cakestudio.cakeauctionapi.addon.AbstractAddon;
import dev.cakestudio.cakeauctionapi.api.ICakeAuctionAPI;
import dev.cakestudio.cakeauctionapi.api.manager.ITextManager;

import lombok.Getter;

@Getter
public final class AucAddon extends AbstractAddon {

    private ITextManager textManager;

    @Override
    protected void onEnable() {
        saveDefaultConfig();

        ICakeAuctionAPI cakeAuctionAPI = getApi();
        textManager = cakeAuctionAPI.getTextManager();

        textManager.sendConsole("<green>AucAddon v" + getDescription().version() + " enabled!</green>");

        registerListener(new AucListener(this));

        if (cakeAuctionAPI.isAuctionLoaded()) {
            int activeLots = cakeAuctionAPI.getActiveAuctions().size();
            textManager.sendConsole("<yellow>Current active auction lots: " + activeLots);
        }
    }

    @Override
    protected void onDisable() {
        textManager.sendConsole("<red>AucAddon disabled. Cleaning up...</red>");
    }

    public String getMessage(String path) {
        return getConfig().getString("messages." + path, "Message not found: " + path);
    }

}