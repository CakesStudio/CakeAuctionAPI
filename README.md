# CakeAuction API

[![GitHub Release](https://img.shields.io/github/release/CakesStudio/CakeAuctionAPI.svg?logo=github&color=brightgreen)](https://github.com/CakesStudio/CakeAuctionAPI/releases/latest)
[![JitPack](https://img.shields.io/jitpack/v/github/CakesStudio/CakeAuctionAPI.svg?logo=jitpack&color=blue)](https://jitpack.io/#CakesStudio/CakeAuctionAPI)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?logo=opensourceinitiative&color=blue)](LICENSE)

Developer API for **CakeAuction** (Minecraft Paper/Folia). Provides managed addon lifecycle, economy hooks, cross-server networking, and auction event handling.

## Overview

- **Managed Lifecycle:** Automatic registration and cleanup of listeners, commands, tasks, and menus.
- **Folia & Paper Concurrency:** Integrated FoliaLib scheduler for thread safety.
- **Multi-Currency Economy:** Extensible provider system (`IEconomyProvider`).
- **Transactional Events:** Cancellable process events prior to database transactions.
- **Networking & Synchronization:** Direct TCP, Redis Pub/Sub, and BungeeCord plugin messaging.

## Dependency Configuration

The API is bundled into the main CakeAuction plugin. Include the API dependency using `compileOnly` (Gradle) or `provided` (Maven).

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.CakesStudio:CakeAuctionAPI:2.0.0'
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
        <version>2.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Library Relocation

The CakeAuction core plugin relocates Adventure (`net.kyori`) and FoliaLib (`com.tcoded`). Addons referencing these libraries must relocate them into identical target packages to avoid runtime `NoSuchMethodError`.

```groovy
dependencies {
    compileOnly 'com.github.CakesStudio:CakeAuctionAPI:2.0.0'
    compileOnly 'com.tcoded:FoliaLib:0.5.1'
    compileOnly 'net.kyori:adventure-platform-bukkit:4.4.1'
    compileOnly 'net.kyori:adventure-text-minimessage:4.26.1'
}

tasks.shadowJar {
    relocate 'net.kyori', 'dev.cakestudio.cakeauction.libs.kyori'
    relocate 'com.tcoded.folialib', 'dev.cakestudio.cakeauction.libs.folialib'
}
```

## Documentation

Full API reference, manager usage examples, and event tables:

- [API Documentation (DOCUMENTATION.md)](DOCUMENTATION.md)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file.
