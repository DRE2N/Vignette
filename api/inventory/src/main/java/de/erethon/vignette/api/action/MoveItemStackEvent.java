/*
 * Written in 2020 by Daniel Saukel
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

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.layout.InventoryLayout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.ItemStack;

/**
 * @author Daniel Saukel
 */
public class MoveItemStackEvent {

    private InventoryGUI gui;
    private ItemStack itemStack;
    private int slot;
    private Player player;
    private Result result;

    public MoveItemStackEvent(InventoryGUI gui, ItemStack itemStack, int slot, Player player) {
        this.gui = gui;
        this.itemStack = itemStack;
        this.slot = slot;
        this.player = player;
    }

    /**
     * Returns the GUI that was interacted with.
     *
     * @return the GUI that was interacted with
     */
    public InventoryGUI getGUI() {
        return gui;
    }

    /**
     * Returns the ItemStack that was moved.
     *
     * @return the ItemStack that was moved
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Returns the slot where the ItemStack was added or taken from.
     *
     * @return the slot where the ItemStack was added or taken from
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Returns the player who interacted with the GUI.
     *
     * @return the player who interacted with the GUI
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the added ItemStack wrapped in an InventoryButton or returns null if the buttonnwas taken away.
     *
     * @return the added item as a button; null if the item was removed
     */
    public InventoryButton confirmAsButton() {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }
        InventoryLayout layout = (InventoryLayout) gui.getLayout();
        InventoryButton button = new InventoryButton(itemStack);
        layout.set(slot, button);
        return button;
    }

    /**
     * Returns the result set for the Bukkit click event through {@link #setResult(org.bukkit.event.Event.Result) or null if nothing has been set.
     *
     * @return the result set for the click event through {@link #setResult(org.bukkit.event.Event.Result) or null if nothing has been set
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the result for the Bukkit click event.
     *
     * @param result the result for the click event
     */
    public void setResult(Result result) {
        this.result = result;
    }

}
