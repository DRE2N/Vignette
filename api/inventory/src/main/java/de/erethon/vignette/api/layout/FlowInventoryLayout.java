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

/**
 * A basic {@link InventoryLayout} that adds {@link de.erethon.vignette.api.component.Component}s in dextrograde reading direction.
 *
 * @author Daniel Saukel
 */
public class FlowInventoryLayout extends SingleInventoryLayout {

    public FlowInventoryLayout(InventoryGUI gui, int size) {
        super(gui, size);
    }

    protected FlowInventoryLayout(InventoryGUI gui, FlowInventoryLayout layout) {
        super(gui, layout);
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
    public FlowInventoryLayout copy(InventoryGUI gui) {
        return new FlowInventoryLayout(gui, this);
    }

}
