package dev.cakestudio.cakeauctionapi.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ConfigUpdateOptions {
    public static final ConfigUpdateOptions DEFAULT = builder().build();
    private final List<Relocation> relocations;

    private ConfigUpdateOptions(List<Relocation> relocations) {
        this.relocations = relocations.stream().sorted(Comparator.comparingInt(Relocation::version)).toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Relocation> relocations() {
        return relocations;
    }

    public record Relocation(int version, String oldPath, String newPath) {
        public Relocation {
            if (version < 2) throw new IllegalArgumentException("Migration version must be at least 2");
            validatePath(oldPath);
            validatePath(newPath);
            if (oldPath.equals(newPath) || oldPath.startsWith(newPath + ".") || newPath.startsWith(oldPath + ".")) {
                throw new IllegalArgumentException("Migration paths must not overlap");
            }
        }

        private static void validatePath(String path) {
            Objects.requireNonNull(path, "Migration path");
            if (path.isBlank() || path.startsWith(".") || path.endsWith(".") || path.contains("..")
                    || path.equals("config-version") || path.startsWith("config-version.")) {
                throw new IllegalArgumentException("Invalid migration path: " + path);
            }
        }
    }

    public static final class Builder {
        private final List<Relocation> relocations = new ArrayList<>();

        /**
         * Moves a key or section when upgrading past this version. Existing destination values win;
         * conflicting source values remain in place, so no administrator value is discarded.
         */
        public Builder relocate(int version, String oldPath, String newPath) {
            relocations.add(new Relocation(version, oldPath, newPath));
            return this;
        }

        public ConfigUpdateOptions build() {
            return new ConfigUpdateOptions(List.copyOf(relocations));
        }
    }
}