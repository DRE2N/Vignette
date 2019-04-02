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

import de.erethon.vignette.api.pagination.Paginated;
import java.util.Stack;
import org.bukkit.entity.Player;

/**
 * A chest inventory based paginated GUI.
 *
 * @author Daniel Saukel
 */
public class PaginatedInventoryGUI extends InventoryGUI implements Paginated<InventoryGUIPage> {

    private Stack<InventoryGUIPage> pages;
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

    @Override
    public boolean isComponentMoveUpEnabled() {
        return componentMoveUpEnabled;
    }

    @Override
    public void setComponentMoveUpEnabled(boolean moveUp) {
        componentMoveUpEnabled = moveUp;
    }

    @Override
    public Stack<InventoryGUIPage> getPages() {
        return pages;
    }

    @Override
    public InventoryGUIPage newPage() {
        return pages.push(new InventoryGUIPage(pages.size()));
    }

    @Override
    public void open(int page, Player... players) {
        GUI gui = pages.get(page);
        if (gui == null) {
            throw new IllegalArgumentException(this + " does not have " + page + " pages");
        }
        gui.open(players);
    }

}
