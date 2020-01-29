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
import de.erethon.vignette.api.layout.PaginatedInventoryLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * A chest inventory based paginated GUI.
 *
 * @author Daniel Saukel
 */
public class PaginatedInventoryGUI extends InventoryGUI implements Paginated<InventoryGUI> {

    /**
     * @deprecated for internal use only
     */
    @Deprecated
    public static Player exclude;

    private List<Inventory> openedInventories = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private Map<Player, Integer> openedPage = new HashMap<>();
    private boolean componentMoveUpEnabled;

    public PaginatedInventoryGUI() {
        super();
    }

    /**
     * @param title the title
     */
    public PaginatedInventoryGUI(String title) {
        super(title);
    }

    public PaginatedInventoryGUI(PaginatedInventoryGUI gui) {
        super(gui);
    }

    @Override
    public int getPages() {
        return ((PaginatedInventoryLayout) getLayout()).getPages();
    }

    @Override
    public String getTitle(int page) {
        if (titles.size() > page && page > 0) {
            String title = titles.get(page);
            if (title != null) {
                return title;
            }
        }
        return getTitle();
    }

    @Override
    public void setTitle(int page, String title) {
        if (titles.size() <= page) {
            titles = Arrays.asList(titles.toArray(new String[page + 1]));
        }
        titles.set(page, title);
    }

    @Override
    public boolean isComponentMoveUpEnabled() {
        return componentMoveUpEnabled;
    }

    @Override
    public void setComponentMoveUpEnabled(boolean moveUp) {
        componentMoveUpEnabled = moveUp;
    }

    @Override
    public PaginatedInventoryGUI open(int page, Player player) {
        if (!isRegistered()) {
            throw new IllegalStateException("The GUI " + toString() + " is not registered");
        }
        if (page >= getPages()) {
            page = 0;
        } else if (page < 0) {
            page = getPages() - 1;
        }
        if (viewers.contains(player)) {
            openedPage.put(player, page);
            exclude = player;
            player.openInventory(openedInventories.get(page));
            exclude = null;
            return this;
        } else {
            PaginatedInventoryGUI copy = ((PaginatedInventoryGUI) getContextualizedCopy(player));
            copy.viewers.add(player);
            copy.openedPage.put(player, page);
            player.openInventory(copy.createInventories(player).get(page));
            return copy;
        }
    }

    @Override
    public void close(Player... players) {
        for (Player player : players) {
            if (viewers.contains(player)) {
                openedPage.remove(player);
                viewers.remove(player);
                player.closeInventory();
            }
        }
    }

    @Override
    public void removeViewer(Player player) {
        super.removeViewer(player);
        openedPage.remove(player);
    }

    /**
     * Creates the inventories for each page.
     * <p>
     * This should only be done if the inventory is a {@link #getContextualizedCopy(Player) contextualized copy}.
     *
     * @param viewer the viewer
     * @return the inventory
     */
    private List<Inventory> createInventories(Player viewer) {
        PaginatedInventoryLayout layout = (PaginatedInventoryLayout) getLayout();
        for (int page = 0; page < getPages(); page++) {
            openedInventories.add(Bukkit.createInventory(null, getSize(), getTitle(page)));
            for (int slot = 0; slot < getSize(); slot++) {
                Component<?, InventoryGUI> comp = layout.getComponent(page, slot);
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
                openedInventories.get(page).setItem(slot, button.createItemStack());
            }
        }
        return openedInventories;
    }

    /**
     * Returns a List of each {@link org.bukkit.inventory.Inventory} created per page from this GUI.
     *
     * @return a List of each {@link org.bukkit.inventory.Inventory} created per page from this GUI
     */
    public List<Inventory> getOpenedInventories() {
        return openedInventories;
    }

    @Override
    public Integer getOpenedPage(Player player) {
        return openedPage.get(player);
    }

    @Override
    public boolean is(Inventory rawInventory) {
        if (rawInventory == null) {
            return false;
        }
        return openedInventories.contains(rawInventory);
    }

    @Override
    public PaginatedInventoryGUI copy() {
        return new PaginatedInventoryGUI(this);
    }

}
