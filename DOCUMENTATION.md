# CakeAuction API Detailed Documentation

This document is the exhaustive reference for **CakeAuction** developers. It covers the managed addon system, every manager, the event system, and technical requirements.

---

## 🛠️ Core API Access
The entry point for all API interactions. Access it via `CakeAuctionAPI.getApi()`.

```java
ICakeAuctionAPI api = CakeAuctionAPI.getApi();

// Check if the auction database is fully loaded into cache
if (api.isAuctionLoaded()) {
    // Safe to perform bulk operations or analytics
}
```

---

## 🧩 Managed Addon System

### 1. Metadata (`addon.yml`)
Addons require an `addon.yml` file in the resources directory.

```yaml
name: MyAwesomeAddon
main: com.example.myaddon.MyAddon
version: 1.0.0
api-version: '1.3.4'       # Minimum CakeAuctionAPI version required
folia-supported: true    # Enable Folia support
description: "Example description"
authors: [ "Developer" ]
depend: []               # Hard dependencies
soft-depend: []          # Optional dependencies
```

### 2. Implementation
Extend `AbstractAddon` to hook into the managed lifecycle.

```java
public class MyAddon extends AbstractAddon {
    @Override
    protected void onEnable() {
        saveDefaultConfig(); // Config will be in /plugins/CakeAuction/addons/MyAddon/
        
        // Automated resource management
        registerListener(new MyListener());
        registerCommand(new MyCommand());
    }

    @Override
    protected void onDisable() {
        // disable
    }
}
```

> [!IMPORTANT]
> **Automated Cleanup:** Upon disabling, CakeAuction automatically:
> - Unregisters all listeners registered via `registerListener`.
> - Removes all commands registered via `registerCommand`.
> - Cancels all tasks scheduled via `runTask*` methods.
> - Closes all GUI menus opened by this addon.

<br/>

## ⚡ Concurrency & Schedulers
CakeAuction provides a wrapper for **FoliaLib**. It is mandatory to use the internal scheduler methods to ensure safety across Paper, Spigot, and Folia.

```java
// Safe asynchronous execution (Auto-cleanup on disable)
runTaskAsync(() -> {
    // Background logic
});

// Region-aware tasks
runTaskLaterAsync(() -> { ... }, 20L);
runTaskTimerAsync(() -> { ... }, 0L, 100L);
```

<br/>

## 🛠️ Core API Usage

Access the API via `CakeAuctionAPI.getApi()`.

### ⚖️ Auction Manager (`IAuctionManager`)
Core auction logic, searching, and categories.
```java
ICakeAuctionAPI api = CakeAuctionAPI.getApi();
IAuctionManager auction = api.getAuctionManager();

// Get active auctions
Collection<IAuctionItem> items = auction.getActiveAuctions();

// Advanced Search (Query, Category, Sort)
Collection<IAuctionItem> results = auction.search("Diamond", "Resources", "price_asc");

// Check if auction items are fully loaded from database
if (api.isAuctionLoaded()) {
    // Safe to perform analytics or bulk operations
}

// Calculate tax based on player permissions and price
double tax = auction.calculateTax(player, 1000.0);

// Check if an item is forbidden from being sold
if (auction.isBlacklisted(itemStack)) { ... }

// Create a new auction lot
api.createAuction(player, itemStack, 500.0, 3600L, false);

// Buy an item programmatically
api.buyItem(player, auctionItem);
```

### 🆔 UUID & Identification API
Managed UUID v7 generation for time-ordered identifiers and short Base62 IDs.
```java
IUUIDManager uuid = api.getUUIDManager();

// Generate a standard UUID v7 (Time-ordered)
UUID random = uuid.random();

// Generate a custom UUID v7 with server identifier (prevent collisions)
UUID serverId = uuid.generate("lobby-1");

// Convert UUID to a short 10-11 character ID
String shortId = uuid.toShortId(random); // e.g., "7x2K9mPq1z"
```

### 🧵 Threading & Async API
Wrappers for safe multi-threaded operations, including dedicated database executors.
```java
IThreadManager threads = api.getThreadManager();

// Run on main thread (Region-aware on Folia)
threads.runSync(() -> player.sendMessage("Sync!"));

// Run database operation with UI callback
threads.runDatabaseOperation(
    () -> someHeavyDbQuery(), // Async
    (result) -> player.sendMessage("Data: " + result) // Sync callback
);
```

### 📦 Item Serialization & Signatures
High-level ItemStack handling for persistence and unique identification.
```java
IItemManager itemManager = api.getItemManager();

// Serialize/Deserialize Base64
String base64 = itemManager.serialize(itemStack);
ItemStack restored = itemManager.deserialize(base64);

// Generate unique digital signatures (for dupe detection)
String signature = itemManager.getSignature(itemStack);

// Generate items from config material/lore with placeholders
ItemStack generated = itemManager.generateItem("DIAMOND_SWORD", "&bExcalibur", List.of("&7Owner: {player}"), Map.of("{player}", player.getName()));
```

### 👤 User Data API
Access to player statistics, limits, and historical records.
```java
IUserManager userManager = api.getUserManager();

// Check listing limits
if (userManager.isLimitReached(player)) {
    player.sendMessage("Limit reached!");
}

// Access stats and passes
boolean hasPass = userManager.hasActivePass(player.getUniqueId());
String subscription = userManager.getSubscriptionName(player.getUniqueId());

// Log history entry
userManager.addHistory(player.getUniqueId(), "Bought a Diamond", itemManager.serialize(diamond));
```

### 📊 System Monitoring API
Tools for performance metrics, stress detection, and resource safety.
```java
IMonitorManager monitor = api.getMonitorManager();

// Check if server is under high stress
if (monitor.isUnderStress()) {
    // Reduce heavy tasks
}

// Log to internal system log
monitor.log("MyAddon", "Performing heavy migration...");

// Check if an operation is safe to run
if (monitor.isSafe("database_backup")) {
    // Proceed
}
```

### 🗄️ Database & Migration API
Direct access to database connections for custom queries and migrations.
```java
IDatabaseManager db = api.getDatabaseManager();

// Execute raw SQL or custom migrations safely
db.useConnection(conn -> {
    try (var stmt = conn.prepareStatement("SELECT * FROM my_addon_table")) {
        // ...
    } catch (SQLException e) { ... }
});

// Check server status in network
boolean isHead = db.isHead();
```

### 📂 Config & Assets API
Access to cached settings and plugin resources.
```java
IConfigManager config = api.getConfigManager();
IAssetManager assets = api.getAssetManager();

// Get cached config files
FileConfiguration dbConfig = config.getConfig("database");

// Get settings from main config with default
double tax = config.getSetting("auction.default-tax", 5.0);

// Load resources from plugin JAR
InputStream is = assets.getResource("custom_menu.yml");

// Register new assets from addon
assets.registerAsset("menus/addons/my_menu.yml", myInputStream);
```

### Menu Integration
Any external GUI library can be integrated while maintaining the auto-close safety feature.
```java
// Register external menus for managed cleanup
getMenuManager().registerMenu(player, externalMenuInstance);
```

<br/>

## 🎭 Custom Actions API
CakeAuction allows addons to register their own action tags (e.g., `[MY_ACTION]`), which can be used in any configuration file with built-in support for `<delay=...>` and `<chance=...>`.

### 1. Registering an Action
Implement the `IAction` functional interface and register it via the `ActionManager`.

```java
IActionManager actionManager = CakeAuctionAPI.getApi().getActionManager();

// Register a custom action tag: [GIVE_REWARD]
actionManager.registerAction("GIVE_REWARD", (player, location, parsedText) -> {
    // parsedText is the string after the tag in the config
    player.sendMessage("§aYou received a reward: §f" + parsedText);
});
```

### 2. Using in Config
Administrators can now use your custom action in `config.yml` or any menu:
```yaml
actions:
  - "[GIVE_REWARD] Super Diamond <chance=50> <delay=20>"
  - "[CONSOLE_COMMAND] effect give {player} speed 10 1"
```

> [!TIP]
> **Automatic Formatting:** All placeholders (like `{player}`) are automatically parsed by the core plugin before your action's `run` method is called.

### 💰 Economy API & Custom Providers
CakeAuction abstracts economy operations, allowing you to interact with the server's economy or register your own provider.

```java
IEconomyManager economy = api.getEconomyManager();

// Register a custom economy (e.g., Gems from another plugin)
economy.registerProvider(new IEconomyProvider() {
    @Override public String getName() { return "my_gems"; }
    @Override public double getBalance(OfflinePlayer p) { return ...; }
    @Override public boolean has(OfflinePlayer p, double a) { return ...; }
    @Override public boolean withdraw(OfflinePlayer p, double a) { ... }
    @Override public boolean deposit(OfflinePlayer p, double a) { ... }
    @Override public String format(double a) { return a + " Gems"; }
});

// Regular usage
if (economy.has(player, 100.0)) {
    economy.withdraw(player, 100.0);
}
```

### 📡 Network API & Custom Channels
Send and receive data across your server network (Bungee/Redis) using custom channels.

```java
INetworkManager network = api.getNetworkManager();

// Send a custom packet to all servers
network.sendCustomPacket("my_addon:sync", dataBytes);

// Register a listener for incoming packets
network.registerCustomPacketHandler("my_addon:sync", (originServer, data) -> {
    player.sendMessage("Received data from: " + originServer);
});
```

### 🖥️ Custom GUI Menus
Create managed GUI menus that benefit from automated cleanup and stress protection.

```java
IMenuManager menus = api.getMenuManager();

// Open a custom menu provider
menus.openMenu(player, new IMenuProvider() {
    @Override
    public Inventory createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Custom Menu");
        inv.setItem(13, new ItemStack(Material.GOLD_INGOT));
        return inv;
    }

    @Override
    public boolean handleUpdate(Player player, int slot, String action) {
        if (slot == 13) {
            player.sendMessage("Clicked Gold!");
            return true; // Event cancelled
        }
        return false;
    }

    @Override
    public void onClose(Player player) {
        // Cleanup logic
    }

// Register an external GUI library object (like Triumph-GUI) for managed cleanup
menus.registerMenu(player, externalGuiObject);
});
```

---

## 📅 Event API
Comprehensive event system with process (cancellable) and result events.

### Auction Events (`dev.cakestudio.cakeauctionapi.api.event.auction`)
| Event | Description | Cancellable |
| :--- | :--- | :---: |
| `AuctionItemBuyProcessEvent` | Fires before a purchase is finalized. | ✅ |
| `AuctionItemBuyCountProcessEvent` | Fires before a partial purchase (buy-count). | ✅ |
| `AuctionItemSellProcessEvent` | Fires before a new lot is published. | ✅ |
| `AuctionItemTakeProcessEvent` | Fires before a seller cancels their auction. | ✅ |
| `AuctionItemTakeUnsoldProcessEvent` | Fires before claiming an expired item. | ✅ |
| `AuctionItemBuyEvent` | Fired when a purchase is successful. | ❌ |
| `AuctionItemBuyCountEvent` | When a partial purchase is successful. | ❌ |
| `AuctionItemSellEvent` | When a new lot is published. | ❌ |
| `AuctionItemTakeEvent` | When an item is reclaimed by the seller. | ❌ |
| `AuctionItemTakeUnsoldEvent` | When an expired item is reclaimed. | ❌ |
| `AuctionItemsLoadedEvent` | Fired when all items are loaded from DB to cache. | ❌ |
| `AuctionItemAutoBuyEvent` | Fired when an item is bought via auto-buy. | ❌ |

### User Events (`dev.cakestudio.cakeauctionapi.api.event.user`)
- `AuctionPassUseEvent`: Fired when a player uses an auction pass.

---

## 🔗 Hook API
The Hook API provides a unified way to interact with external plugins like **ItemsAdder** without direct dependencies.

```java
IHookManager hooks = CakeAuctionAPI.getApi().getHookManager();

// Get a custom item by its identifier
ItemStack customItem = hooks.getItem("itemsadder:ruby");

// Get the unique ID of an item
String id = hooks.getItemId(someItemStack);
```

<br/>

## 📝 Text & Formatting API
The Text Manager handles parsing, coloring, and messaging with built-in support for **MiniMessage**, **HEX**, and **PlaceholderAPI**.

```java
ITextManager text = CakeAuctionAPI.getApi().getTextManager();

// Parse text to Adventure Component
Component component = text.parse("<red>Hello <yellow>{player}", player);

// Send message to player
text.sendMessage(player, "&aWelcome to the auction!");

// Broadcast to all players
text.broadcast("<rainbow>Special Event Started!");
```

<br/>

## ⚠️ Technical Guidelines

1. **Static Access:** Always use `CakeAuctionAPI.getApi()` to access the implementation.
2. **ClassLoader Safety:** The managed system handles most cleanup, but avoid static references to addon classes in the core plugin.
3. **Folia threading:** Never use `Bukkit.getScheduler()` within an addon; regional threading errors may occur on Folia servers. Always use the provided `runTask*` methods.

<br/>