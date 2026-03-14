# CakeAuction API

[![GitHub Release](https://img.shields.io/github/release/CakesStudio/CakeAuctionAPI.svg?logo=github&color=brightgreen)](https://github.com/CakesStudio/CakeAuctionAPI/releases/latest)
[![JitPack](https://img.shields.io/jitpack/v/github/CakesStudio/CakeAuctionAPI.svg?logo=jitpack&color=blue)](https://jitpack.io/#CakesStudio/CakeAuctionAPI)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?logo=opensourceinitiative&color=blue)](LICENSE)

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

> [!IMPORTANT]
> **No Standalone Installation Needed:** The API is already bundled within the main **CakeAuction** plugin. You do **not** need to put a separate API JAR in your server's `/plugins` folder.

> [!TIP]
> CakeAuction API is hosted on **JitPack**. Ensure you use the `compileOnly` scope to avoid bundling the API into your JAR.

### Gradle (Groovy)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Note: use :api suffix to depend only on the API module
    compileOnly 'com.github.CakesStudio:CakeAuctionAPI:VERSION'
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
        <artifactId>CakeAuctionAPI</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
*(Replace `VERSION` with the target release, e.g., `1.3.4`)*

<br/>

## 📦 Library Relocation & Dependencies
CakeAuction core relocates its internal libraries (like **Adventure API** and **FoliaLib**) to prevent version conflicts.

> [!CAUTION]
> **DO NOT relocate** these libraries in your addon!
> The CakeAuction API uses original package names (`net.kyori`, `com.tcoded.folialib`). The core plugin automatically handles the translation between your addon's calls and its internal relocated libraries.

Simply use `compileOnly` for these dependencies in your `build.gradle`:

**Recommended build.gradle setup:**
```gradle
dependencies {
    compileOnly "com.tcoded:FoliaLib:0.5.1"
    compileOnly 'net.kyori:adventure-platform-bukkit:4.4.1'
    compileOnly 'net.kyori:adventure-text-minimessage:4.26.1'
    compileOnly 'net.kyori:adventure-text-serializer-legacy:4.26.1'
    compileOnly 'net.kyori:adventure-text-serializer-plain:4.26.1'
}

tasks.shadowJar {
    // Only relocate your private dependencies that you implementation/shade into your JAR.
    // NEVER relocate net.kyori or com.tcoded.
}
```

<br/>

<br/>

## 📖 Documentation
Detailed guides, manager usage, and event references can be found in the:
### 👉 [**API Documentation (DOCUMENTATION.md)**](DOCUMENTATION.md)

<br/>

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.