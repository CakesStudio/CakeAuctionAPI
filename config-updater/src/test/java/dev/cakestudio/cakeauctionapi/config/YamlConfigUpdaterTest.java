package dev.cakestudio.cakeauctionapi.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.nio.file.attribute.PosixFilePermission;

import static org.junit.jupiter.api.Assertions.*;

class YamlConfigUpdaterTest {
    @TempDir Path directory;

    @Test
    void newFileCopiesBundledBytesWithoutBackup() throws Exception {
        String defaults = "# Default comment\nname: sample\n\nconfig-version: 2\n";
        Path file = directory.resolve("nested/config.yml");
        var result = update(file, defaults, ConfigUpdateOptions.DEFAULT);
        assertEquals(defaults, Files.readString(file));
        assertTrue(result.changed());
        assertEquals(0, result.previousVersion());
        assertEquals(2, result.currentVersion());
        assertNull(result.backup());
        assertFalse(Files.exists(file.getParent().resolve("backups")));
    }

    @Test
    void upgradePreservesSecretsUnknownKeysCommentsAndBackup() throws Exception {
        String original = "# Admin comment\ndiscord:\n  token: custom-token\ncustom:\n  caption: Привет\n\nconfig-version: 1\n";
        Path file = existing("config.yml", original);
        var result = update(file, "discord:\n  token: default-token\n  enabled: false\nnew-key: 123\n\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT);
        var doc = YamlDocument.create(file.toFile());
        assertEquals("custom-token", doc.getString("discord.token"));
        assertFalse(doc.getBoolean("discord.enabled"));
        assertEquals("Привет", doc.getString("custom.caption"));
        assertEquals(123, doc.getInt("new-key"));
        assertEquals(original, Files.readString(result.backup()));
        assertTrue(Files.readString(file).contains("# Admin comment"));
        assertTrue(Files.readString(file).endsWith("\n\nconfig-version: 2\n"));
        byte[] updated = Files.readAllBytes(file);
        var repeated = update(file, "discord: {}\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT);
        assertFalse(repeated.changed());
        assertArrayEquals(updated, Files.readAllBytes(file));
        try (var files = Files.list(file.getParent().resolve("backups"))) {
            assertEquals(1, files.count());
        }
    }

    @Test
    void skippedVersionsApplyRelocationsInOrderAndKeepSourceComments() throws Exception {
        Path file = existing("config.yml", "# Custom caption\nold: custom\nconfig-version: 1\n");
        var options = ConfigUpdateOptions.builder().relocate(3, "middle", "latest")
                .relocate(2, "old", "middle").build();
        update(file, "latest: default\nconfig-version: 3\n", options);
        var doc = YamlDocument.create(file.toFile());
        assertEquals("custom", doc.getString("latest"));
        assertFalse(doc.contains("old"));
        assertFalse(doc.contains("middle"));
        assertTrue(Files.readString(file).contains("# Custom caption"));
    }

    @Test
    void sectionMigrationDoesNotOverwriteExplicitDestination() throws Exception {
        Path file = existing("config.yml", "mysql:\n  host: old-host\n  password: secret\n  custom: 17\ndatabase:\n  host: new-host\nconfig-version: 1\n");
        update(file, "database:\n  host: localhost\n  password: default\n  port: 3306\nconfig-version: 2\n",
                ConfigUpdateOptions.builder().relocate(2, "mysql", "database").build());
        var doc = YamlDocument.create(file.toFile());
        assertEquals("new-host", doc.getString("database.host"));
        assertEquals("secret", doc.getString("database.password"));
        assertEquals(17, doc.getInt("database.custom"));
        assertEquals("old-host", doc.getString("mysql.host"));
        assertFalse(doc.contains("mysql.password"));
    }

    @Test
    void nestedRelocationDoesNotReplaceScalarAncestor() throws Exception {
        Path file = existing("config.yml", "old: secret\ndestination: custom\nconfig-version: 1\n");
        update(file, "old: default\ndestination: fallback\nconfig-version: 2\n",
                ConfigUpdateOptions.builder().relocate(2, "old", "destination.key").build());
        var doc = YamlDocument.create(file.toFile());
        assertEquals("secret", doc.getString("old"));
        assertEquals("custom", doc.getString("destination"));
    }

    @Test
    void mergingIntoExistingSectionKeepsItsLegacyHeaderComment() throws Exception {
        Path file = existing("config.yml", "unrelated: true\n# Administrator database notes\nmysql:\n  password: secret\ndatabase:\n  host: custom-host\nconfig-version: 1\n");
        update(file, "database:\n  host: localhost\n  password: default\nconfig-version: 2\n",
                ConfigUpdateOptions.builder().relocate(2, "mysql", "database").build());
        var doc = YamlDocument.create(file.toFile());
        assertEquals("secret", doc.getString("database.password"));
        assertFalse(doc.contains("mysql"));
        assertTrue(Files.readString(file).contains("# Administrator database notes"));
    }

    @Test
    void missingVersionCanUpgradeToInitialVersion() throws Exception {
        Path file = existing("config.yml", "custom: keep\n");
        update(file, "added: true\nconfig-version: 1\n", ConfigUpdateOptions.DEFAULT);
        var doc = YamlDocument.create(file.toFile());
        assertEquals("keep", doc.getString("custom"));
        assertTrue(doc.getBoolean("added"));
        assertEquals(1, doc.getInt("config-version"));
    }

    @Test
    void unversionedFileUsesMigrationRulesWithoutLosingCredentials() throws Exception {
        Path file = existing("config.yml", "mysql:\n  password: secret\nconfig-version: broken\n");
        update(file, "database:\n  password: default\nconfig-version: 2\n",
                ConfigUpdateOptions.builder().relocate(2, "mysql", "database").build());
        var doc = YamlDocument.create(file.toFile());
        assertEquals("secret", doc.getString("database.password"));
        assertFalse(doc.contains("mysql"));
        assertEquals(2, doc.getInt("config-version"));
    }

    @Test
    void newerFileIsNotDowngradedOrReformatted() throws Exception {
        String original = "# Future config\ncustom: 'keep'\nconfig-version: 30\n";
        Path file = existing("config.yml", original);
        var result = update(file, "added: true\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT);
        assertFalse(result.changed());
        assertEquals(30, result.currentVersion());
        assertEquals(original, Files.readString(file));
        assertFalse(Files.exists(directory.resolve("backups")));
    }

    @Test
    void malformedYamlDoesNotOverwriteUserFile() throws Exception {
        String original = "token: [broken\nconfig-version: 1\n";
        Path file = existing("config.yml", original);
        assertThrows(IOException.class, () -> update(file, "token: default\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT));
        assertEquals(original, Files.readString(file));
        assertFalse(Files.exists(directory.resolve("backups")));
    }

    @Test
    void invalidDefaultsNeverTouchUserFile() throws Exception {
        Path file = existing("config.yml", "token: secret\nconfig-version: 1\n");
        assertThrows(IOException.class, () -> update(file, "key: 1\n", ConfigUpdateOptions.DEFAULT));
        assertEquals("token: secret\nconfig-version: 1\n", Files.readString(file));
    }

    @Test
    void addonRulesAreIsolatedPerFile() throws Exception {
        Path first = existing("first/config.yml", "old: first\nconfig-version: 1\n");
        Path second = existing("second/config.yml", "old: second\nconfig-version: 1\n");
        update(first, "renamed: default\nconfig-version: 2\n", ConfigUpdateOptions.builder().relocate(2, "old", "renamed").build());
        update(second, "added: true\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT);
        assertEquals("first", YamlDocument.create(first.toFile()).getString("renamed"));
        assertEquals("second", YamlDocument.create(second.toFile()).getString("old"));
        assertFalse(YamlDocument.create(second.toFile()).contains("renamed"));
    }

    @Test
    void originalFilePermissionsSurviveReplacement() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(Files.getFileStore(directory).supportsFileAttributeView("posix"));
        Path file = existing("config.yml", "token: secret\nconfig-version: 1\n");
        Set<PosixFilePermission> permissions = Set.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
        Files.setPosixFilePermissions(file, permissions);
        update(file, "added: true\nconfig-version: 2\n", ConfigUpdateOptions.DEFAULT);
        assertEquals(permissions, Files.getPosixFilePermissions(file));
    }

    private Path existing(String name, String content) throws IOException {
        Path file = directory.resolve(name);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
        return file;
    }

    private ConfigUpdateResult update(Path file, String defaults, ConfigUpdateOptions options) throws IOException {
        return YamlConfigUpdater.update(file, new ByteArrayInputStream(defaults.getBytes(StandardCharsets.UTF_8)), options);
    }
}
