package dev.cakestudio.cakeauctionapi.addon;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Metadata description for a CakeAuction addon.
 * Loaded from the addon.yml resource.
 *
 * @param name           The unique name of the addon.
 * @param mainClass      The fully qualified name of the main class.
 * @param version        The version string of the addon.
 * @param apiVersion     The version of the CakeAuction API the addon is built for.
 * @param foliaSupported Whether the addon supports the Folia server software.
 * @param description    A brief description of the addon's purpose.
 * @param authors        A set of authors who contributed to the addon.
 * @param depend         A list of required plugin/addon dependencies.
 * @param softDepend     A list of optional plugin/addon dependencies.
 */
public record AddonDescription(
        @NonNull String name,
        @NonNull String mainClass,
        @NonNull String version,
        @Nullable String apiVersion,
        boolean foliaSupported,
        @Nullable String description,
        @NonNull Set<String> authors,
        @NonNull List<String> depend,
        @NonNull List<String> softDepend
) {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    public AddonDescription {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid addon name: " + name + ". Only a-z, A-Z, 0-9, _ and - are allowed.");
        }
    }

    /**
     * Loads an addon description from a reader source.
     *
     * @param reader The reader providing the YAML content.
     * @return A new {@link AddonDescription} instance.
     */
    public static AddonDescription load(@NonNull Reader reader) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
        
        String name = config.getString("name", "addon.yml is missing 'name'");
        String main = config.getString("main", "addon.yml is missing 'main'");
        String version = config.getString("version", "1.0.0");
        String apiVersion = config.getString("api-version");
        boolean foliaSupported = config.getBoolean("folia-supported", false);
        String desc = config.getString("description");
        
        Set<String> authors = new HashSet<>(config.getStringList("authors"));
        String author = config.getString("author");
        if (author != null) authors.add(author);
        
        List<String> depend = config.getStringList("depend");
        List<String> softDepend = config.getStringList("soft-depend");
        
        return new AddonDescription(
                name,
                main,
                version,
                apiVersion,
                foliaSupported,
                desc,
                Collections.unmodifiableSet(authors),
                Collections.unmodifiableList(depend),
                Collections.unmodifiableList(softDepend)
        );
    }

}