# CakeAuction API &nbsp; &nbsp; [![GitHub Release](https://img.shields.io/github/release/cakesstudio/CakeAuctionAPI.svg?style=flat)](https://github.com/CakesStudio/CakeAuctionAPI/releases/latest) [![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

The official developer interface for **CakeAuction**. This API provides a managed environment for building robust addons, interacting with the auction ecosystem, and ensuring cross-platform compatibility.

<br/>

## 📝 Description
CakeAuction API is designed to provide high-level abstractions for auction management while maintaining complete isolation through a dedicated Addon System. It allows developers to extend core functionality without manual resource management or boilerplate code.

**Key Features:**
- **Managed Lifecycle:** Automated registration and cleanup of listeners, commands, and tasks.
- **Native Folia Support:** Built-in integration with FoliaLib for regional threading.
- **Dependency Isolation:** Each addon operates in its own ClassLoader.
- **Transactional Events:** Pre-process events allow validation and cancellation before database commits.

<br/>

## 🚀 Installation

> [!TIP]
> CakeAuction API is hosted on **JitPack**. Ensure you use the `compileOnly` scope to avoid bundling the API into your JAR.

### Gradle (Groovy)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.CakesStudio:CakeAuction:VERSION'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.CakesStudio</groupId>
        <artifactId>CakeAuction</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
*(Replace `VERSION` with the target release, e.g., `1.1.5`)*

<br/>

## 🧩 Managed Addon System

### 1. Metadata (`addon.yml`)
Addons require an `addon.yml` file in the resources directory.

```yaml
name: MyAwesomeAddon
main: com.example.myaddon.MyAddon
version: 1.0.0
api-version: '1.1.5'       # Required CakeAuctionAPI version for compatibility
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
        saveDefaultConfig(); // Isolated config management
        
        // Automated resource management
        registerListener(new MyListener());
        registerCommand(new MyCommand());
    }

    @Override
    protected void onDisable() {
        // Final cleanup (state persistence, etc.)
    }
}
```

> [!IMPORTANT]
> **Automated Cleanup:** Upon disabling, CakeAuction automatically unregisters listeners, terminates schedulers (including non-cancelled tasks), removes commands from the Bukkit map, and closes open GUI menus.

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

### Managing Auctions
```java
ICakeAuctionAPI api = CakeAuctionAPI.getApi();

// Get active auctions
Collection<IAuctionItem> items = api.getActiveAuctions();

// Create a new auction lot
api.createAuction(player, itemStack, 500.0, 3600L, false);

// Buy an item programmatically
api.buyItem(player, auctionItem);
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

<br/>

## 📅 Event API
CakeAuction provides a comprehensive set of events. **ProcessEvents** are cancellable and fire *before* the action is finalized.

### Auction Process Events (Cancellable)
| Event | Description |
| :--- | :--- |
| `AuctionItemBuyProcessEvent` | Fires before a purchase is finalized. |
| `AuctionItemBuyCountProcessEvent` | Fires before a partial purchase (buy-count). |
| `AuctionItemSellProcessEvent` | Fires before a new lot is published. |
| `AuctionItemTakeProcessEvent` | Fires before a seller cancels their auction. |
| `AuctionItemTakeUnsoldProcessEvent` | Fires before claiming an expired item. |

### Auction Result Events
| Event | Description |
| :--- | :--- |
| `AuctionItemBuyEvent` | Fired when a purchase is successful. |
| `AuctionItemBuyCountEvent` | Fired when a partial purchase is successful. |
| `AuctionItemSellEvent` | Fired when a new lot is published. |
| `AuctionItemTakeEvent` | Fired when an item is reclaimed by the seller. |
| `AuctionItemTakeUnsoldEvent` | Fired when an expired item is reclaimed. |
| `AuctionItemAutoBuyEvent` | Fired when an item is bought via auto-buy system. |
| `AuctionPassUseEvent` | Fired when a player uses an auction pass. |

<br/>

## 💰 Economy API
CakeAuction abstracts economy operations, allowing you to interact with the server's economy (Vault, etc.) through a consistent interface.

```java
IEconomyManager economy = CakeAuctionAPI.getApi().getEconomyManager();

// Check balance and withdraw
if (economy.has(player, 100.0)) {
    economy.withdraw(player, 100.0);
}

// Deposit and format currency
economy.deposit(player, 50.0);
String formatted = economy.format(50.0); // e.g., "$50.00"
```

<br/>

## 🔗 Hook API
The Hook API provides a unified way to interact with external plugins like **ItemsAdder** or **Orizon** without direct dependencies.

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
2. **ClassLoader Safety:** Do not pass addon-local classes to persistent core-plugin collections without proper cleanup.
3. **Folia threading:** Never use `Bukkit.getScheduler()` within an addon; regional threading errors may occur on Folia servers.

<br/>

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
