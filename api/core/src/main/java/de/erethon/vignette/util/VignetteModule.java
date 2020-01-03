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
package de.erethon.vignette.util;

import org.bukkit.plugin.Plugin;

/**
 * Class for module registration.
 *
 * @author Daniel Saukel
 */
public interface VignetteModule {

    /**
     * Fired when {@link de.erethon.vignette.api.VignetteAPI#init(org.bukkit.plugin.Plugin)} is called.
     *
     * @param plugin the plugin that enables Vignette
     * @throws IllegalStateException if the environment does not support the module
     */
    void onInit(Plugin plugin) throws IllegalStateException;

}
