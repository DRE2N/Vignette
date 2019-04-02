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
package de.erethon.vignette.api;

import de.erethon.vignette.api.component.Component;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.layout.InventoryLayout;
import de.erethon.vignette.api.layout.Layout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A chest inventory based GUI.
 *
 * @author Daniel Saukel
 */
public class InventoryGUI extends AbstractGUI<InventoryGUI> {

    private Inventory openedInventory;

    public InventoryGUI() {
        super();
    }

    /**
     * @param title the title
     */
    public InventoryGUI(String title) {
        super(title);
    }

    private InventoryGUI(InventoryGUI gui) {
        super(gui);
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

    @Override
    public void close(Player... players) {
        for (Player player : players) {
            if (viewers.contains(player)) {
                viewers.remove(player);
                player.closeInventory();
            }
        }
    }

    @Override
    public void open(Player... players) {
        for (Player player : players) {
            viewers.add(player);
            player.openInventory(createInventory(player));
        }
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
     * Creates the inventory.
     *
     * @param viewer the viewer
     * @return the inventory
     */
    public Inventory createInventory(Player viewer) {
        InventoryGUI gui;
        boolean copied;
        if (!getContextModifiers().isEmpty()) {
            gui = copy();
            gui.setTransient(true);
            gui.applyAllContextModifiers(viewer);
            copied = true;
        } else {
            gui = this;
            copied = false;
        }
        gui.openedInventory = Bukkit.createInventory(null, gui.getSize(), gui.getTitle());

        InventoryLayout layout = (InventoryLayout) gui.getLayout();
        for (int i = 0; i < layout.getSize(); i++) {
            Component comp = layout.getComponent(i);
            if (!(comp instanceof InventoryButton)) {
                continue;
            }
            InventoryButton button = (InventoryButton) comp;
            if (!button.getContextModifiers().isEmpty()) {
                // If the GUI is just a transient copy anyway, there is no need to copy the buttons.
                if (!copied) {
                    button = ((InventoryButton) comp).copy();
                }
                button.applyAllContextModifiers(viewer);
            }
            gui.openedInventory.setItem(i, button.createItemStack());
        }

        return gui.openedInventory;
    }

    /**
     * Returns the last {@link org.bukkit.inventory.Inventory} created from this GUI.
     *
     * @return the last {@link org.bukkit.inventory.Inventory} created from this GUI
     */
    public Inventory getOpenedInventory() {
        return openedInventory;
    }

    @Override
    public InventoryGUI copy() {
        return new InventoryGUI(this);
    }

}
