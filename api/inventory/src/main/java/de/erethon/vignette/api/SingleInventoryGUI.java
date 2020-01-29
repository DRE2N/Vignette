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

import de.erethon.vignette.api.component.Component;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.layout.InventoryLayout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Daniel Saukel
 */
public class SingleInventoryGUI extends InventoryGUI {

    private Inventory openedInventory;

    public SingleInventoryGUI() {
        super();
    }

    /**
     * @param title the title
     */
    public SingleInventoryGUI(String title) {
        super(title);
    }

    public SingleInventoryGUI(SingleInventoryGUI gui) {
        super(gui);
    }

    @Override
    public SingleInventoryGUI open(Player player) {
        if (!isRegistered()) {
            throw new IllegalStateException("The GUI " + toString() + " is not registered");
        }
        SingleInventoryGUI copy = ((SingleInventoryGUI) getContextualizedCopy(player));
        copy.viewers.add(player);
        player.openInventory(copy.createInventory(player));
        return copy;
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

    /**
     * Creates the inventory.
     * <p>
     * This should only be done if the inventory is a {@link #getContextualizedCopy(Player) contextualized copy}.
     *
     * @param viewer the viewer
     * @return the inventory
     */
    private Inventory createInventory(Player viewer) {
        openedInventory = Bukkit.createInventory(null, getSize(), getTitle());
        InventoryLayout layout = (InventoryLayout) getLayout();
        for (int i = 0; i < getSize(); i++) {
            Component<?, InventoryGUI> comp = layout.getComponent(i);
            if (!(comp instanceof InventoryButton)) {
                continue;
            }
            InventoryButton button = (InventoryButton) comp;
            if (!button.getContextModifiers().isEmpty()) {
                // If the GUI is just a transient copy anyway, there is no need to copy the buttons.
                if (!isTransient()) {
                    button = ((InventoryButton) comp).copy();
                }
                button.applyAllContextModifiers(viewer);
            }
            openedInventory.setItem(i, button.createItemStack());
        }
        return openedInventory;
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
    public boolean is(Inventory rawInventory) {
        if (rawInventory == null) {
            return false;
        }
        return rawInventory.equals(openedInventory);
    }

    @Override
    public SingleInventoryGUI copy() {
        return new SingleInventoryGUI(this);
    }

}
