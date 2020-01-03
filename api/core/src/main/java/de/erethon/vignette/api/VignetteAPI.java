/*
 * Written from 2019-2020 by Daniel Saukel
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software
 * to the public domain worldwide.
 *
 * This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package de.erethon.vignette.api;

import de.erethon.vignette.util.VignetteModule;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

/**
 * This class loads the modules and tracks and manages the instances of {@link GUI}.
 *
 * @author Daniel Saukel
 */
public class VignetteAPI {

    private static List<GUI> cache = new ArrayList<>();

    private VignetteAPI() {
    }

    /**
     * This method must be called one time before using Vignette.
     * <p>
     * It handles the initialization of the modules.
     *
     * @param plugin the plugin that enables Vignette
     * @deprecated this is terrible and I feel ashamed of myself
     */
    @Deprecated
    public static void init(Plugin plugin) {
        try {
            ((VignetteModule) Class.forName("de.erethon.vignette.InventoryModule").newInstance()).onInit(plugin);
            plugin.getLogger().log(Level.INFO, "Successfully loaded Vignette module \"Inventory\".");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Returns a List of the registered GUIs.
     *
     * @return a List of the registered GUIs
     */
    public static List<GUI> getCache() {
        return new ArrayList<>(cache);
    }

    /**
     * Returns a List of the registered GUIs of a specific type.
     *
     * @param <T>  the type of the GUI implementation
     * @param type the class of the GUI implementation
     * @return a List of the registered GUIs of a specific type
     */
    public static <T extends GUI> List<T> getCache(Class<T> type) {
        List<T> guis = new ArrayList<>();
        for (GUI gui : cache) {
            if (type.isInstance(gui)) {
                guis.add((T) gui);
            }
        }
        return guis;
    }

    /**
     * Starts tracking the GUI.
     *
     * @param gui the GUI
     */
    public static void register(GUI gui) {
        if (!isRegistered(gui)) {
            cache.add(gui);
        }
    }

    /**
     * Stops tracking the GUI.
     *
     * @param gui the GUI
     */
    public static void unregister(GUI gui) {
        cache.remove(gui);
    }

    /**
     * Returns if the GUI is registered.
     *
     * @param gui the gui
     * @return if the GUI is registered
     */
    public static boolean isRegistered(GUI gui) {
        return cache.contains(gui);
    }

}
