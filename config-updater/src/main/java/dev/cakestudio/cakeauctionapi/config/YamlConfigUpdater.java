package dev.cakestudio.cakeauctionapi.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.ArrayList;
import java.util.Objects;

public final class YamlConfigUpdater {
    private YamlConfigUpdater() {}

    public static synchronized ConfigUpdateResult update(Path file, InputStream defaults,
                                                         ConfigUpdateOptions options) throws IOException {
        Objects.requireNonNull(defaults, "Bundled configuration resource");
        Objects.requireNonNull(options, "Update options");
        Path target = file.toAbsolutePath().normalize();
        try {
            byte[] defaultBytes = defaults.readAllBytes();
            YamlDocument bundled = YamlDocument.create(new ByteArrayInputStream(defaultBytes));
            int latest = version(bundled.get("config-version"));
            if (latest == 0) throw new IOException("Bundled config must have a positive integer config-version");

            if (!Files.exists(target)) {
                writeAtomically(target, defaultBytes);
                return new ConfigUpdateResult(0, latest, true, null);
            }

            byte[] original = Files.readAllBytes(target);
            YamlDocument document = YamlDocument.create(new ByteArrayInputStream(original),
                    new ByteArrayInputStream(defaultBytes),
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.builder().setAutoUpdate(false).build());
            int previous = version(document.get("config-version"));

            int effectiveVersion = Math.max(1, previous);
            if (previous >= latest) return new ConfigUpdateResult(previous, previous, false, null);

            for (var relocation : options.relocations()) {
                if (relocation.version() > effectiveVersion && relocation.version() <= latest) {
                    relocateMissing(document, relocation.oldPath(), relocation.newPath());
                }
            }
            var updater = UpdaterSettings.builder()
                    .setEnableDowngrading(false).setKeepAll(true).setAutoSave(false);
            if (previous > 0) updater.setVersioning(new BasicVersioning("config-version"));
            document.update(updater.build());
            document.set("config-version", latest);

            var versionBlock = document.getBlock("config-version");
            document.remove("config-version");
            document.set("config-version", versionBlock);
            String updated = document.dump();
            int versionLine = updated.lastIndexOf("\nconfig-version:");
            if (versionLine >= 0) {
                updated = updated.substring(0, versionLine).stripTrailing() + "\n\n" + updated.substring(versionLine + 1);
            }

            Path backupDir = target.getParent().resolve("backups");
            Files.createDirectories(backupDir);
            Path backup = Files.createTempFile(backupDir, target.getFileName() + "-v" + effectiveVersion + "-", ".bak");
            Files.write(backup, original);
            writeAtomically(target, updated.getBytes(StandardCharsets.UTF_8));
            return new ConfigUpdateResult(previous, latest, true, backup);
        } catch (RuntimeException e) {
            throw new IOException("Cannot update configuration " + target + "; original file was not replaced", e);
        }
    }

    private static int version(Object value) {
        if (value == null || !value.toString().matches("[1-9][0-9]*")) return 0;
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static void relocateMissing(YamlDocument document, String source, String destination) {
        if (!document.contains(source)) return;
        if (!document.contains(destination)) {
            int separator = destination.indexOf('.');
            while (separator >= 0) {
                String parent = destination.substring(0, separator);
                if (document.contains(parent) && !document.isSection(parent)) return;
                separator = destination.indexOf('.', separator + 1);
            }
            document.move(source, destination);
        } else if (document.isSection(source) && document.isSection(destination)) {
            Section oldSection = document.getSection(source);
            for (String key : new ArrayList<>(oldSection.getRoutesAsStrings(false))) {
                relocateMissing(document, source + "." + key, destination + "." + key);
            }
            if (oldSection.getRoutesAsStrings(false).isEmpty()) {
                if (oldSection.getComments() != null) {
                    document.getSection(destination).addComments(oldSection.getComments());
                }
                document.remove(source);
            }
        }
    }

    private static void writeAtomically(Path target, byte[] bytes) throws IOException {
        Files.createDirectories(target.getParent());
        Path temporary = Files.createTempFile(target.getParent(), ".config-update-", ".tmp");
        try {
            Files.write(temporary, bytes);
            if (Files.exists(target) && Files.getFileAttributeView(target, PosixFileAttributeView.class) != null) {
                Files.setPosixFilePermissions(temporary, Files.getPosixFilePermissions(target));
            }
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}