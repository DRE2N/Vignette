/*
 * Written in 2019 by Daniel Saukel
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

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.VignetteAPI;
import de.erethon.vignette.api.action.Action;
import de.erethon.vignette.api.action.InteractionEvent;
import de.erethon.vignette.api.component.InventoryButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Daniel Saukel
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        for (InventoryGUI gui : VignetteAPI.getCache(InventoryGUI.class)) {
            if (!gui.getViewers().contains(player)) {
                continue;
            }
            InventoryButton button = gui.getButton(event.getCurrentItem());
            if (button == null) {
                continue;
            }
            event.setCancelled(!button.isStealable());
            if (button.getSound() != null) {
                player.playSound(player.getLocation(), button.getSound(), 1f, 1f);
            }
            if (button.getInteractionListener() != null) {
                button.getInteractionListener().onAction(new InteractionEvent(player, Action.CLICK));// TO DO
            }
            break;
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        for (InventoryGUI gui : VignetteAPI.getCache(InventoryGUI.class)) {
            if (event.getInventory().equals(gui.getOpenedInventory()) && gui.isTransient()) {
                gui.unregister();
            }
            gui.close(player);
        }
    }

}
