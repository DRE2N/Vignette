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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    private void fireMISE(InventoryGUI gui, InventoryClickEvent event) {
        MoveItemStackListener misl = gui.getMoveItemStackListener();
        if (misl == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                MoveItemStackEvent mise = new MoveItemStackEvent(gui, event.getAction(), event.getInventory().getItem(event.getSlot()),
                        event.getSlot(), (Player) event.getWhoClicked());
                try {
                    misl.onAddition(mise);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }.runTaskLater(JavaPlugin.getProvidingPlugin(InventoryListener.class), 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        for (InventoryGUI gui : VignetteAPI.getCache(InventoryGUI.class)) {
            if (!gui.getViewers().contains(player)) {
                continue;
            }
            if (event.getClickedInventory() instanceof PlayerInventory) {
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                }
                return;
            }
            InventoryButton button = gui.getButton(event.getCurrentItem());
            InventoryAction iAction = event.getAction();
            if (button == null) {
                if (MOVE_ACTIONS.contains(iAction)) {
                    fireMISE(gui, event);
                }
                continue;
            }
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
            boolean cancelled = true;
            if (vAction == Action.LEFT_CLICK) {
                cancelled = button.isLeftClickLocked();
            } else if (vAction == Action.RIGHT_CLICK) {
                cancelled = button.isRightClickLocked();
            }
            event.setCancelled(cancelled);
            if (button.getSound() != null) {
                player.playSound(player.getLocation(), button.getSound(), 1f, 1f);
            }
            if (button.getInteractionListener() != null) {
                InteractionEvent ie = new InteractionEvent(gui, button, player, vAction);
                try {
                    button.getInteractionListener().onAction(ie);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (ie.isClickCancelled()) {
                    return;
                }
            }
            if (!cancelled && MOVE_ACTIONS.contains(iAction)) {
                fireMISE(gui, event);
            }
            break;
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        for (InventoryGUI gui : VignetteAPI.getCache(InventoryGUI.class)) {
            if (gui.getViewers().contains(player)) {
                event.setCancelled(true);
                return;
            }
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
