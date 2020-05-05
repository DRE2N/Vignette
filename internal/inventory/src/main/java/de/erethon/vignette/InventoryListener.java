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

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.PaginatedInventoryGUI;
import de.erethon.vignette.api.VignetteAPI;
import de.erethon.vignette.api.action.Action;
import de.erethon.vignette.api.action.CloseEvent;
import de.erethon.vignette.api.action.InteractionEvent;
import de.erethon.vignette.api.action.MoveItemStackEvent;
import de.erethon.vignette.api.action.MoveItemStackListener;
import de.erethon.vignette.api.component.InventoryButton;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author Daniel Saukel
 */
public class InventoryListener implements Listener {

    private static final Set<InventoryAction> MOVE_ACTIONS = new HashSet<>();

    static {
        MOVE_ACTIONS.add(InventoryAction.PLACE_ALL);
        MOVE_ACTIONS.add(InventoryAction.PLACE_ONE);
        MOVE_ACTIONS.add(InventoryAction.PLACE_SOME);
        MOVE_ACTIONS.add(InventoryAction.PICKUP_ALL);
        MOVE_ACTIONS.add(InventoryAction.PICKUP_HALF);
        MOVE_ACTIONS.add(InventoryAction.PICKUP_ONE);
        MOVE_ACTIONS.add(InventoryAction.PICKUP_SOME);
        MOVE_ACTIONS.add(InventoryAction.COLLECT_TO_CURSOR);
        MOVE_ACTIONS.add(InventoryAction.MOVE_TO_OTHER_INVENTORY);
        MOVE_ACTIONS.add(InventoryAction.SWAP_WITH_CURSOR);
    }

    private void fireAISE(InventoryGUI gui, InventoryClickEvent event) {
        MoveItemStackListener aisl = gui.getMoveItemStackListener();
        if (aisl == null) {
            return;
        }
        MoveItemStackEvent aise = new MoveItemStackEvent(gui, event.getCursor(), event.getSlot(), (Player) event.getWhoClicked());
        aisl.onAddition(aise);
        if (aise.getResult() != null) {
            event.setResult(aise.getResult());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            return;
        }
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
            InventoryAction iAction = event.getAction();
            if (button.getInteractionListener() != null) {
                Action vAction = Action.CLICK;
                switch (iAction) {
                    case PLACE_ALL:
                    case PICKUP_ALL:
                    case MOVE_TO_OTHER_INVENTORY:
                        vAction = Action.LEFT_CLICK;
                        break;
                    case PICKUP_HALF:
                    case PLACE_ONE:
                        vAction = Action.RIGHT_CLICK;
                }
                InteractionEvent ie = new InteractionEvent(gui, button, player, vAction);
                button.getInteractionListener().onAction(ie);
                if (ie.isClickCancelled()) {
                    return;
                }
            }
            if (MOVE_ACTIONS.contains(iAction)) {
                fireAISE(gui, event);
            }
            break;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        if (PaginatedInventoryGUI.exclude == player) {
            return;
        }
        for (InventoryGUI gui : VignetteAPI.getCache(InventoryGUI.class)) {
            if (!gui.is(event.getInventory())) {
                continue;
            }
            if (gui.getCloseListener() != null) {
                gui.getCloseListener().onClose(new CloseEvent(gui, player));
            }
            gui.removeViewer(player);
            if (gui.isTransient()) {
                gui.unregister();
            }
        }
    }

}
