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
package de.erethon.vignette.api.action;

/**
 * @author Daniel Saukel
 */
@FunctionalInterface
public interface MoveItemStackListener {

    /**
     * Fired when an {@link org.bukkit.inventory.ItemStack} is added to an inventory GUI.
     *
     * @param event the event
     */
    void onAddition(MoveItemStackEvent event);

}
