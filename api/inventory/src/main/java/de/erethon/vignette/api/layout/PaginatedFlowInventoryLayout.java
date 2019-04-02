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
package de.erethon.vignette.api.layout;

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.PaginatedInventoryGUI;
import de.erethon.vignette.api.pagination.PaginatedInventoryLayout;
import de.erethon.vignette.api.pagination.PaginatedInventoryLayout.PaginationButtonPosition;

/**
 * A basic paginated {@link InventoryLayout} that adds {@link de.erethon.vignette.api.component.Component}s in dextrograde reading direction.
 *
 * @author Daniel Saukel
 */
public class PaginatedFlowInventoryLayout extends FlowInventoryLayout implements PaginatedInventoryLayout {

    private PaginationButtonPosition paginationButtonPosition;
    private boolean switchButtonLinePlaceholdersEnabled;

    public PaginatedFlowInventoryLayout(PaginatedInventoryGUI gui, int size, PaginationButtonPosition paginationButtonPosition) {
        super(gui, size);
        this.paginationButtonPosition = paginationButtonPosition;
        set(getSwitchButtonSlot(paginationButtonPosition, size, false), PREVIOUS_PAGE);
        set(getSwitchButtonSlot(paginationButtonPosition, size, true), NEXT_PAGE);
    }

    protected PaginatedFlowInventoryLayout(InventoryGUI gui, PaginatedFlowInventoryLayout layout) {
        super(gui, layout);
        paginationButtonPosition = layout.paginationButtonPosition;
        switchButtonLinePlaceholdersEnabled = layout.switchButtonLinePlaceholdersEnabled;
    }

    @Override
    public boolean areSwitchButtonLinePlaceholdersEnabled() {
        return switchButtonLinePlaceholdersEnabled;
    }

    @Override
    public void setSwitchButtonLinePlaceholdersEnabled(boolean enabled) {
        switchButtonLinePlaceholdersEnabled = enabled;
        if (enabled) {
            int sbslot = getSwitchButtonSlot(paginationButtonPosition, getSize(), false);
            set(sbslot + 1, PLACEHOLDER);
            set(sbslot + 2, PLACEHOLDER);
            set(sbslot + 3, PLACEHOLDER);
            set(sbslot + 4, PLACEHOLDER);
            set(sbslot + 5, PLACEHOLDER);
            set(sbslot + 6, PLACEHOLDER);
            set(sbslot + 7, PLACEHOLDER);
        } else {
            removeIf(c -> c.equals(PLACEHOLDER));
        }
    }

    @Override
    public int nextSlot() {
        slot++;
        if (slot >= getSize()) {
            slot = -1;
        }
        return slot;
    }

    @Override
    public PaginatedFlowInventoryLayout copy(InventoryGUI gui) {
        return new PaginatedFlowInventoryLayout(gui, this);
    }

}
