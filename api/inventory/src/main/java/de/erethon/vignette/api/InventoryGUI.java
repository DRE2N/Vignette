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

import de.erethon.vignette.api.action.MoveItemStackListener;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.layout.InventoryLayout;
import de.erethon.vignette.api.layout.Layout;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Shared code of chest inventory based GUIs.
 *
 * @author Daniel Saukel
 */
public abstract class InventoryGUI extends AbstractGUI<InventoryGUI> {

    private MoveItemStackListener moveItemStackListener;

    protected InventoryGUI() {
        super();
    }

    /**
     * @param title the title
     */
    protected InventoryGUI(String title) {
        super(title);
    }

    protected InventoryGUI(InventoryGUI gui) {
        super(gui);
        moveItemStackListener = gui.moveItemStackListener;
    }

    @Override
    public void setLayout(Layout<InventoryGUI> layout) {
        if (!(layout instanceof InventoryLayout)) {
            throw new IllegalArgumentException(layout.getClass().getName() + " is not an instance of " + InventoryLayout.class.getName());
        }
        super.setLayout(layout);
    }

    /**
     * Returns the amount of slots in the inventory.
     * <p>
     * The size is set through an {@link de.erethon.vignette.api.layout.InventoryLayout}.
     *
     * @return the amount of slots in the inventory
     */
    public int getSize() {
        return ((InventoryLayout) getLayout()).getSize();
    }

    /**
     * Removes a player from the viewers list without closing the inventory.
     *
     * @param player the player to remove from the viewers list
     * @deprecated for internal use only; use {@link #close(org.bukkit.entity.Player...)} instead.
     */
    @Deprecated
    public void removeViewer(Player player) {
        viewers.remove(player);
    }

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     *
     * @param itemStack the ItemStack
     * @return the first InventoryButton that is equal to the ItemStack
     */
    public InventoryButton getButton(ItemStack itemStack) {
        return ((InventoryLayout) getLayout()).getButton(itemStack);
    }

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     * <p>
     * Also checks versions of the button modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @param itemStack     the ItemStack
     * @param contextPlayer the Player that is used to check the button's form modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     * @return the first InventoryButton that is equal to the ItemStack
     */
    public InventoryButton getButton(ItemStack itemStack, Player contextPlayer) {
        return ((InventoryLayout) getLayout()).getButton(itemStack, contextPlayer);
    }

    /**
     * Returns the MoveItemStackListener attached to this GUI.
     *
     * @return the MoveItemStackListener attached to this GUI
     */
    public MoveItemStackListener getMoveItemStackListener() {
        return moveItemStackListener;
    }

    /**
     * Sets the MoveItemStackListener attached to this GUI.
     *
     * @param listener the listener to set
     */
    public void setMoveItemStackListener(MoveItemStackListener listener) {
        moveItemStackListener = listener;
    }

    /**
     * Checks if the GUI is a representation of an {@link org.bukkit.inventory.Inventory}.
     *
     * @param rawInventory an Inventory
     * @return if the Inventory is a representation of this GUI
     */
    public abstract boolean is(Inventory rawInventory);

    /**
     * Checks if the GUI is a representation of an {@link org.bukkit.inventory.Inventory}.
     * <p>
     * If the GUI has one or more {@link de.erethon.vignette.api.context.ContextModifier}s and if it is not a representation of the Inventory
     * in the GUI's form not modified by its ContextModifiers, a copy of the GUI is made and the modifiers are applied to this copy for reference
     * to check if the modified form is a representation of the Inventory.
     *
     * @param rawInventory  an Inventory
     * @param contextPlayer the Player
     * @return if the Inventory is a representation of this GUI
     */
    public boolean is(Inventory rawInventory, Player contextPlayer) {
        boolean is = is(rawInventory);
        if (is) {
            return true;

        } else if (!getContextModifiers().isEmpty()) {
            InventoryGUI modified = copy();
            modified.applyAllContextModifiers(contextPlayer);
            return modified.is(rawInventory);

        } else {
            return false;
        }
    }

}
