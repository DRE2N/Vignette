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
package de.erethon.vignette;

import de.erethon.vignette.util.VignetteModule;
import org.bukkit.plugin.Plugin;

/**
 * @author Daniel Saukel
 */
public class InventoryModule implements VignetteModule {

    @Override
    public void onInit(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

}
