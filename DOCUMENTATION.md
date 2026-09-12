# CakeAuction API Reference

Technical documentation and developer reference for **CakeAuction** (Minecraft Paper and Folia).

---

## API Entry Point

The entry point for all API interactions is `CakeAuctionAPI.getApi()`.

```java
ICakeAuctionAPI api = CakeAuctionAPI.getApi();

// Check if auction database cache is loaded
if (api.isAuctionLoaded()) {
    // Safe to perform bulk operations and queries
}

// Create auction lot with specified currency
api.createAuction(player, itemStack, 1500.0, "vault", 86400L, false);

// Purchase an active auction item
api.buyItem(buyer, auctionItem);

// Reclaim or cancel an active lot
api.cancelAuction(seller, auctionItem);
```

---

## Addon System

CakeAuction loads and manages addons located in `plugins/CakeAuction/addons/`.

### Metadata (addon.yml)
Place `addon.yml` in `src/main/resources`:

```yaml
name: CustomAuctionAddon
main: com.example.addon.CustomAddon
version: 1.0.0
api-version: '1.6.1'
folia-supported: true
description: "Custom CakeAuction addon"
authors: [ "Developer" ]
depend: []
soft-depend: []
```

### Main Class (AbstractAddon)
Addon main classes must extend `AbstractAddon`. Listeners, commands, tasks, and menus registered through it are automatically cleaned up when the addon is disabled.

```java
public class CustomAddon extends AbstractAddon {
    @Override
    protected void onEnable() {
        saveDefaultConfig();
        registerListener(new MyListener());
        registerCommand(new MyCommand());
    }

    @Override
    protected void onDisable() {
        // Optional custom cleanup logic
    }
}
```

---

## Concurrency and Schedulers

CakeAuction bundles `FoliaLib` for compatibility with Paper and Folia regional threading. Use `AbstractAddon` scheduler methods rather than `Bukkit.getScheduler()`:

```java
// Asynchronous task
runTaskAsync(() -> {
    // Background execution
});

// Asynchronous task with delay
runTaskLaterAsync(() -> {
    // Execution after 20 ticks
}, 20L);

// Asynchronous repeating timer
runTaskTimerAsync(() -> {
    // Repeated execution every 100 ticks
}, 0L, 100L);
```

---

## API Managers

### IAuctionManager
Manages active lots, categories, and custom sorting algorithms.

```java
IAuctionManager auction = api.getAuctionManager();

// Retrieve all active auction items
Collection<IAuctionItem> active = auction.getActiveAuctions();

// Search items with query, category filter, currency, and sort algorithm
Collection<IAuctionItem> results = auction.search("Netherite", "Weapons", "vault", "price_asc");

// Check blacklist status
if (auction.isBlacklisted(itemStack)) {
    // Item cannot be sold on auction
}
```

#### Dynamic Categories
Register custom categories with predicate-based filters:

```java
ItemStack icon = new ItemStack(Material.NETHERITE_SWORD);
auction.registerCategory(
    "legendary_weapons",
    "<gradient:#ff0000:#ff8800>Legendary</gradient>",
    icon,
    itemStack -> {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getDisplayName().contains("Legendary");
    }
);

// Unregister on addon disable
auction.unregisterCategory("legendary_weapons");
```

#### Custom Sorting
Register custom sorting criteria using `Comparator<IAuctionItem>`:

```java
auction.registerSortingType(
    "seller_name_length",
    "<aqua>Seller Name Length</aqua>",
    Comparator.comparingInt(item -> item.getSellerName().length())
);

// Unregister on addon disable
auction.unregisterSortingType("seller_name_length");
```

---

### IEconomyManager
Manages multiple currencies and allows custom economy provider registration.

```java
IEconomyManager economy = api.getEconomyManager();

// Check multi-currency support
if (economy.supportsMultiCurrency()) {
    double balance = economy.getBalance(player, "playerpoints");

    if (economy.has(player, "playerpoints", 250.0)) {
        economy.withdraw(player, "playerpoints", 250.0);
        economy.deposit(recipient, "playerpoints", 250.0);
    }

    String formatted = economy.format("playerpoints", 250.0);
}

// Register custom economy provider
economy.registerProvider(new IEconomyProvider() {
    @Override public String getName() { return "gems"; }
    @Override public double getBalance(OfflinePlayer p) { return ...; }
    @Override public boolean has(OfflinePlayer p, double amount) { return ...; }
    @Override public boolean withdraw(OfflinePlayer p, double amount) { return ...; }
    @Override public boolean deposit(OfflinePlayer p, double amount) { return ...; }
    @Override public String format(double amount) { return amount + " Gems"; }
});
```

---

### ITaxManager
Calculates commission rates, seller/buyer shares, and active discount schedules.

```java
ITaxManager tax = api.getTaxManager();

if (tax.isTaxEnabled()) {
    // Total tax percentage for player and currency
    double rate = tax.getTaxRate(player, "vault"); // e.g. 5.0 (5%)

    // Separate seller and buyer tax rates
    double sellerRate = tax.getSellerTaxRate(player, "vault");
    double buyerRate = tax.getBuyerTaxRate(player, "vault");

    // Active discount schedule identifier ("none", "night_discount", etc.)
    String scheduleId = tax.getActiveScheduleId(player);
}
```

---

### IUserManager
Manages player listing limits, durations, and sales history.

```java
IUserManager users = api.getUserManager();

// Check if player listing limit is reached for a currency
if (users.isLimitReached(player, "playerpoints")) {
    player.sendMessage("Listing limit reached.");
}

int maxSlots = users.getMaxSlots(player);
int currencyMaxSlots = users.getMaxSlots(player, "playerpoints");
long duration = users.getSellDuration(player, "playerpoints");

// Record completed transaction in player history
users.addSale(player.getUniqueId(), 1);
```

---

### IBonusLimitManager
Manages permanent bonus listing slots stored in the database.

```java
IBonusLimitManager bonusLimits = api.getBonusLimitManager();

// Get current bonus slots
int bonus = bonusLimits.getBonus(player.getUniqueId());

// Add slots (e.g. from donation store or quest reward)
bonusLimits.addBonus(player.getUniqueId(), 3);

// Deduct or set absolute slots
bonusLimits.takeBonus(player.getUniqueId(), 1);
bonusLimits.setBonus(player.getUniqueId(), 10);

// Reset bonus slots to zero
bonusLimits.resetBonus(player.getUniqueId());
```

---

### IPassManager
Manages trade passes that reduce auction tax commissions.

```java
IPassManager passes = api.getPassManager();

// Create ItemStack pass configured in passes.yml
if (passes.hasPass("vip_pass")) {
    ItemStack passItem = passes.createPass("vip_pass");
    player.getInventory().addItem(passItem);
}

// Check active pass status and remaining duration
if (passes.hasActivePass(player)) {
    double discount = passes.getDiscount(player); // percentage (e.g. 50.0)
    long remainingMs = passes.getRemainingTime(player);
}
```

---

### IUUIDManager
Generates UUID v7 (time-ordered) and Base62 short identifiers.

```java
IUUIDManager uuid = api.getUUIDManager();

// Generate standard UUID v7
UUID randomUuid = uuid.random();

// Generate UUID v7 with server name hash (prevents collision across nodes)
UUID serverScoped = uuid.generate("server-lobby-1");

// Convert UUID to compact 10-11 character Base62 ID
String shortId = uuid.toShortId(randomUuid); // e.g. "7x2K9mPq1z"
```

---

### IItemManager
Serializes items to Base64 and generates unique item signatures.

```java
IItemManager itemManager = api.getItemManager();

// Base64 serialization
String base64 = itemManager.serialize(itemStack);
ItemStack deserialized = itemManager.deserialize(base64);

// Digital signature for duplicate detection
String signature = itemManager.getSignature(itemStack);

// Item generation with placeholder parsing
ItemStack item = itemManager.generateItem(
    "DIAMOND_SWORD",
    "&bExcalibur",
    List.of("&7Owner: {player}"),
    Map.of("{player}", player.getName())
);
```

---

### IHookManager
Handles custom item integration for ItemsAdder, Oraxen, Nexo, and custom plugins.

```java
IHookManager hooks = api.getHookManager();

// Resolve item by external ID
ItemStack item = hooks.getItem("itemsadder:ruby");
String itemId = hooks.getItemId(itemStack);

// Register custom item hook provider
IItemHookProvider provider = new IItemHookProvider() {
    @Override
    public @Nullable ItemStack getItem(@NonNull String id) {
        if (id.equalsIgnoreCase("ruby_gem")) {
            return new ItemStack(Material.EMERALD);
        }
        return null;
    }

    @Override
    public @Nullable String getItemId(@NonNull ItemStack item) {
        if (item.getType() == Material.EMERALD) {
            return "ruby_gem";
        }
        return null;
    }

    @Override
    public @NonNull String getPrefix() {
        return "myplugin"; // Resolves "myplugin:ruby_gem"
    }
};

hooks.registerProvider(provider);
```

---

### IActionManager
Registers custom action tags parsed in configuration files and GUI menus.

```java
IActionManager actions = api.getActionManager();

// Register action tag: [GIVE_REWARD]
actions.registerAction("GIVE_REWARD", (player, location, text) -> {
    player.sendMessage("Reward: " + text);
});
```

Usage in configurations:
```yaml
actions:
  - "[GIVE_REWARD] 100_COINS <chance=50> <delay=10>"
  - "[CONSOLE_COMMAND] give {player} diamond 1"
```

---

### IMonitorManager
Monitors performance, stress conditions, and displays status reports.

```java
IMonitorManager monitor = api.getMonitorManager();

// Open interactive status GUI to player (/ah admin status)
monitor.openStatusMenu(player);

// Send text-based status report to CommandSender
monitor.sendStatusReport(sender);

// Check server load and stop running stress tests
if (monitor.isUnderStress() || monitor.isStressTestRunning()) {
    monitor.stopStressTest();
}
```

---

### IDatabaseManager
Provides database diagnostics, connection access, and head-node verification.

```java
IDatabaseManager db = api.getDatabaseManager();

// Storage mode: "HYBRID" (Redis + SQL) or "SQL"
boolean isHybrid = db.isHybridStorage();
String mode = db.getStorageMode();

// Head node verification (handles scheduled cleanup and expiration)
boolean isHead = db.isHead();

// Execute queries using managed connection
db.useConnection(conn -> {
    try (var stmt = conn.prepareStatement("SELECT COUNT(*) FROM cakeauction_items")) {
        // Execute query
    } catch (SQLException e) {
        // Handle error
    }
});
```

---

### INetworkManager
Handles inter-server network synchronization and custom packets.

```java
INetworkManager network = api.getNetworkManager();

// Check synchronization mode ("DIRECT_TCP", "REDIS", "BUNGEE", "NONE")
String netType = network.getNetworkType();
boolean isDirectTcp = network.isDirectTcpEnabled();

// Broadcast custom packet to other nodes
network.sendCustomPacket("addon_channel:sync", payloadBytes);

// Register incoming packet listener
network.registerCustomPacketHandler("addon_channel:sync", (originServer, data) -> {
    // Process packet from originServer
});
```

---

### IAIManager
Provides asynchronous access to AI services.

```java
IAIManager ai = api.getAiManager();

if (ai != null && ai.isEnabled()) {
    // Price advisory
    ai.advisePrice(player.getInventory().getItemInMainHand(), "vault").thenAccept(advice -> {
        if (advice.isSuccess()) {
            double fair = advice.getFairPrice();
            String reasoning = advice.getAdvice();
        }
    });

    // Semantic query search
    ai.parseSearchQuery("netherite armor set").thenAccept(result -> {
        String keyword = result.getKeyword();
        String category = result.getCategory();
    });

    // Market summary digest
    ai.generateMarketDigest().thenAccept(digest -> {
        // Process text digest
    });
}
```

---

### IMenuManager
Manages custom GUI menus with automatic cleanup on addon disable.

```java
IMenuManager menus = api.getMenuManager();

// Register external GUI instance for managed cleanup
menus.registerMenu(player, externalGuiObject);
```

---

### ITextManager
Handles MiniMessage, Legacy formatting, HEX color codes, and PlaceholderAPI.

```java
ITextManager text = api.getTextManager();

// Parse MiniMessage into Adventure Component
Component component = text.parse("<gradient:#ff8800:#ffd700>Auction Lot</gradient>");

// Send message to player
text.sendMessage(player, "<green>Item purchased successfully.");

// Broadcast message across server
text.broadcast("<yellow>New high-value lot published.");
```

---

## Events

### Auction Events (`dev.cakestudio.cakeauctionapi.api.event.auction`)

| Event | Description | Cancellable |
| :--- | :--- | :---: |
| `AuctionItemBuyProcessEvent` | Before item purchase transaction | Yes |
| `AuctionItemBuyCountProcessEvent` | Before partial purchase (buy-count) | Yes |
| `AuctionItemSellProcessEvent` | Before listing a lot | Yes |
| `AuctionItemTakeProcessEvent` | Before seller cancels listing | Yes |
| `AuctionItemTakeUnsoldProcessEvent` | Before claiming expired lot | Yes |
| `AuctionItemBuyEvent` | After successful purchase | No |
| `AuctionItemBuyCountEvent` | After successful partial purchase | No |
| `AuctionItemSellEvent` | After lot publication | No |
| `AuctionItemTakeEvent` | After lot reclaimed by seller | No |
| `AuctionItemTakeUnsoldEvent` | After expired lot reclaimed | No |
| `AuctionItemsLoadedEvent` | After items loaded from DB to cache | No |
| `AuctionItemAutoBuyEvent` | After server auto-buys item | No |

### User Events (`dev.cakestudio.cakeauctionapi.api.event.user`)

| Event | Description | Cancellable |
| :--- | :--- | :---: |
| `AuctionPassUseEvent` | When player uses an auction discount pass | No |

```java
@EventHandler
public void onPassUse(AuctionPassUseEvent event) {
    Player player = event.getPlayer();
    double discountPercent = event.getPercent();
    long durationMillis = event.getDuration();
}
```

---

## Technical Guidelines

1. **Access Point:** Always obtain instances via `CakeAuctionAPI.getApi()`.
2. **Resource Management:** Use `AbstractAddon` registration helpers (`registerListener`, `registerCommand`) for automatic lifecycle cleanup.
3. **Scheduler Compatibility:** Do not invoke `Bukkit.getScheduler()` directly. Use `AbstractAddon` scheduler methods or `api.getFoliaLib()` for compatibility with Folia regional threads.