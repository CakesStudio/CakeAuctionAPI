package dev.cakestudio.cakeauctionapi.addon;

import dev.cakestudio.cakeauctionapi.CakeAuctionAPI;
import dev.cakestudio.cakeauctionapi.api.manager.ui.IMenuManager;
import dev.cakestudio.cakeauctionapi.api.ICakeAuctionAPI;

import com.tcoded.folialib.wrapper.task.WrappedTask;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import lombok.Getter;
import lombok.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Base class for all CakeAuction addons.
 * Handles lifecycle management, configuration, event registration, and cleanup.
 */
public abstract class AbstractAddon {

    private Logger logger;
    private AddonDescription description;
    private ClassLoader classLoader;
    private File dataFolder;
    @Getter private volatile boolean enabled = false;
    private FileConfiguration config = null;
    private File configFile = null;
    private final List<Listener> registeredListeners = new CopyOnWriteArrayList<>();
    private final List<Object> registeredCommands = new CopyOnWriteArrayList<>();
    private final List<WrappedTask> tasks = new CopyOnWriteArrayList<>();
    private boolean initialized = false;

    public AbstractAddon() {}

    /**
     * Initializes the addon with its metadata.
     * Internal use only.
     *
     * @param description The addon metadata.
     * @param classLoader The addon classloader.
     */
    public final void init(@NonNull AddonDescription description, @NonNull ClassLoader classLoader) {
        if (initialized) {
            throw new IllegalStateException("Addon already initialized");
        }
        this.description = description;
        this.classLoader = classLoader;
        this.logger = Logger.getLogger("CakeAuction#" + description.name());
        this.dataFolder = new File(new File(getApi().getDataFolder(), "addons"), description.name());
        this.dataFolder.mkdirs();
        this.configFile = new File(dataFolder, "config.yml");
        this.initialized = true;
    }

    /**
     * Called when the addon is loaded. 
     * Executed before {@link #onEnable()}.
     */
    public void onLoad() {}

    /**
     * Called when the addon is being enabled.
     * Use this for initialization of resources and commands.
     */
    protected abstract void onEnable();

    /**
     * Called when the addon is being disabled.
     * Use this for final cleanup and data saving.
     */
    protected abstract void onDisable();

    /**
     * Updates the enabled state of the addon and triggers lifecycle methods.
     *
     * @param enabled The target state.
     */
    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        
        if (enabled) {
            try {

                logger.info("Enabling " + description.name() + " v" + description.version());

                this.enabled = true;

                onEnable();

            } catch (Exception e) {
                logger.severe("Error while enabling addon " + description.name() + ": " + e.getMessage());

                try {

                    this.enabled = false;

                    onDisable();

                    cleanup();

                } catch (Exception ignored) {}

                throw new RuntimeException("Failed to enable addon " + description.name(), e);
            }
        } else {
            this.enabled = false;

            logger.info("Disabling " + description.name());

            try {
                onDisable();
            } catch (Exception e) {
                logger.severe("Error while disabling addon " + description.name() + ": " + e.getMessage());
            }

            cleanup();
        }
    }

    /**
     * Performs a complete cleanup of addon resources.
     * This includes closing menus, unregistering listeners and commands, and canceling tasks.
     */
    protected void cleanup() {
        closeOpenedMenus();

        unregisterListeners();

        unregisterCommands();

        cancelTasks();
    }

    /**
     * Unregisters all listeners registered via {@link #registerListener(Listener)}.
     */
    protected void unregisterListeners() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }

        registeredListeners.clear();
    }

    /**
     * Unregisters all commands registered via {@link #registerCommand(Object)}.
     */
    protected void unregisterCommands() {
        for (Object command : registeredCommands) {
            getApi().unregisterCommand(command);
        }

        registeredCommands.clear();
    }

    /**
     * Cancels all tasks scheduled via {@code runTask*} methods.
     */
    protected void cancelTasks() {
        for (WrappedTask task : tasks) {
            try {
                if (!task.isCancelled()) {
                    task.cancel();
                }
            } catch (Exception ignored) {}
            tasks.remove(task);
        }
    }

    /**
     * Closes all menus that were opened by this addon for any online player.
     * This method uses the addon's classloader to identify relevant menus.
     */
    protected void closeOpenedMenus() {
        IMenuManager menuManager = getMenuManager();
        WeakReference<ClassLoader> myLoaderRef = new WeakReference<>(this.classLoader);

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                Object activeMenu = menuManager.getActiveMenu(player);
                ClassLoader myLoader = myLoaderRef.get();

                if (myLoader != null && activeMenu != null && activeMenu.getClass().getClassLoader() == myLoader) {
                    player.closeInventory();
                }

            } catch (Exception ignored) {}
        }

        if (!getApi().getPlugin().isEnabled()) return;

        getApi().getFoliaLib().getScheduler().runLaterAsync(() -> {
            ClassLoader myLoader = myLoaderRef.get();
            if (myLoader == null) return;

            for (Player player : Bukkit.getOnlinePlayers()) {
                try {
                    Object activeMenu = menuManager.getActiveMenu(player);

                    if (activeMenu != null && activeMenu.getClass().getClassLoader() == myLoader) {
                        player.closeInventory();
                    }

                } catch (Exception ignored) {}
            }
        }, 2L);
    }

    /**
     * Runs a task asynchronously.
     *
     * @param runnable The task to run.
     * @return The {@link WrappedTask} instance.
     */
    protected final WrappedTask runTaskAsync(@NonNull Runnable runnable) {
        final AtomicReference<WrappedTask> taskRef = new AtomicReference<>();
        taskRef.set(getApi().getFoliaLib().getScheduler().runLaterAsync(() -> {
            try {
                runnable.run();
            } finally {
                WrappedTask task = taskRef.get();
                if (task != null) tasks.remove(task);
            }
        }, 0L));
        tasks.add(taskRef.get());
        return taskRef.get();
    }

    /**
     * Runs a task asynchronously with a delay.
     *
     * @param runnable The task to run.
     * @param delay    The delay in ticks.
     * @return The {@link WrappedTask} instance.
     */
    protected final WrappedTask runTaskLaterAsync(@NonNull Runnable runnable, long delay) {
        final AtomicReference<WrappedTask> taskRef = new AtomicReference<>();
        taskRef.set(getApi().getFoliaLib().getScheduler().runLaterAsync(() -> {
            try {
                runnable.run();
            } finally {
                WrappedTask task = taskRef.get();
                if (task != null) tasks.remove(task);
            }
        }, delay));
        tasks.add(taskRef.get());
        return taskRef.get();
    }

    /**
     * Runs a repeating task asynchronously.
     *
     * @param runnable The task to run.
     * @param delay    The initial delay in ticks.
     * @param period   The period between executions in ticks.
     * @return The {@link WrappedTask} instance.
     */
    protected final WrappedTask runTaskTimerAsync(@NonNull Runnable runnable, long delay, long period) {
        WrappedTask task = getApi().getFoliaLib().getScheduler().runTimerAsync(runnable, delay, period);
        tasks.add(task);
        return task;
    }

    /**
     * Registers a Bukkit listener for this addon.
     * Listeners are automatically unregistered when the addon is disabled.
     *
     * @param listener The listener instance.
     */
    protected final void registerListener(@NonNull Listener listener) {
        getApi().getPlugin().getServer().getPluginManager().registerEvents(listener, getApi().getPlugin());
        registeredListeners.add(listener);
    }

    /**
     * Registers a command for this addon using the plugin's command manager.
     * Commands are automatically unregistered when the addon is disabled.
     *
     * @param command The command object to register.
     */
    protected final void registerCommand(@NonNull Object command) {
        getApi().registerCommand(command);
        registeredCommands.add(command);
    }

    /**
     * Returns the configuration of the addon.
     *
     * @return The {@link FileConfiguration} instance.
     */
    @NonNull
    public final FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /**
     * Reloads the configuration from the file on disk.
     */
    public final void reloadConfig() {
        if (configFile == null || !configFile.exists()) {
            config = new YamlConfiguration();
            return;
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = getResource("config.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
        }
    }

    /**
     * Saves the current configuration state to the disk.
     */
    public final void saveConfig() {
        if (config == null || configFile == null) return;

        try {
            config.save(configFile);
        } catch (IOException e) {
            logger.severe("Could not save config to " + configFile + ": " + e.getMessage());
        }
    }

    /**
     * Saves the default config.yml from the JAR resources to the data folder if it doesn't exist.
     */
    public final void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    /**
     * Returns an input stream for a resource embedded in the addon JAR.
     *
     * @param filename The path to the resource.
     * @return The {@link InputStream} or null if not found.
     */
    @Nullable
    public final InputStream getResource(@NonNull String filename) {
        try {
            URL url = classLoader.getResource(filename);
            if (url == null) return null;
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Saves a resource from the addon JAR to the data folder.
     *
     * @param resourcePath The path of the resource in the JAR.
     * @param replace      Whether to overwrite the file if it already exists.
     */
    public final void saveResource(@NonNull String resourcePath, boolean replace) {
        File outFile = new File(dataFolder, resourcePath);
        
        try {
            if (!outFile.getCanonicalPath().startsWith(dataFolder.getCanonicalPath())) {
                throw new IllegalArgumentException("Path traversal detected in resource path: " + resourcePath);
            }
        } catch (IOException e) {
             throw new IllegalArgumentException("Invalid resource path: " + resourcePath, e);
        }

        if (outFile.exists() && !replace) return;
        
        outFile.getParentFile().mkdirs();
        try (InputStream in = getResource(resourcePath)) {

            if (in == null) {
                logger.warning("Resource '" + resourcePath + "' not found in addon JAR.");
                return;
            }

            try (FileOutputStream out = new FileOutputStream(outFile)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

        } catch (IOException e) {
            logger.severe("Failed to save resource '" + resourcePath + "': " + e.getMessage());
        }
    }

    /**
     * Provides access to the CakeAuction API.
     *
     * @return The {@link ICakeAuctionAPI} implementation.
     */
    @NonNull
    public final ICakeAuctionAPI getApi() {
        return CakeAuctionAPI.getApi();
    }

    /**
     * Convenience method for accessing the menu manager.
     *
     * @return The {@link IMenuManager} instance.
     */
    @NonNull
    public final IMenuManager getMenuManager() {
        return getApi().getMenuManager();
    }

    /**
     * Returns the addon's private logger.
     *
     * @return The {@link Logger} instance.
     */
    public final @NonNull Logger getLogger() {
        return logger;
    }

    /**
     * Returns the addon metadata from its description file.
     *
     * @return The {@link AddonDescription} instance.
     */
    public final @NonNull AddonDescription getDescription() {
        return description;
    }

    /**
     * Returns the data folder specific to this addon.
     *
     * @return The data {@link File} folder.
     */
    public final @NonNull File getDataFolder() {
        return dataFolder;
    }

}