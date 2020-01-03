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
package de.erethon.vignette.api.layout;

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.PaginatedInventoryGUI;

/**
 * A basic paginated {@link InventoryLayout} that adds {@link de.erethon.vignette.api.component.Component}s in dextrograde reading direction.
 *
 * @author Daniel Saukel
 */
public class PaginatedFlowInventoryLayout extends PaginatedInventoryLayout {

    public PaginatedFlowInventoryLayout(PaginatedInventoryGUI gui, int size, PaginationButtonPosition paginationButtonPosition) {
        super(gui, size, paginationButtonPosition);
    }

    public PaginatedFlowInventoryLayout(PaginatedInventoryGUI gui, PaginatedFlowInventoryLayout layout) {
        super(gui, layout);
    }

    @Override
    public int nextSlot() {
        slot++;
        if (slot >= getSize()) {
            newPage();
            slot = 0;
        }
        return slot;
    }

    @Override
    public PaginatedFlowInventoryLayout copy(InventoryGUI gui) {
        if (!(gui instanceof PaginatedInventoryGUI)) {
            throw new IllegalArgumentException("GUI is not a PaginatedInventoryGUI");
        }
        return new PaginatedFlowInventoryLayout((PaginatedInventoryGUI) gui, this);
    }

}
