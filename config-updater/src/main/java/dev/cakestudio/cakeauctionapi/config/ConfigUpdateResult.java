package dev.cakestudio.cakeauctionapi.config;

import java.nio.file.Path;

public record ConfigUpdateResult(int previousVersion, int currentVersion, boolean changed, Path backup) {}