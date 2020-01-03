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
import java.util.Arrays;

/**
 * An {@link InventoryLayout} that adds {@link de.erethon.vignette.api.component.Component}s to the center where possible.
 * <p>
 * One line is started to be filled after the previous one is completely filled.
 * <p>
 * If a count of {@link de.erethon.vignette.api.component.InventoryButton}s in a line is unequal,
 * the one in the middle is left out in order to achieve symmetry.
 *
 * @author Daniel Saukel
 */
public class CenteredInventoryLayout extends SingleInventoryLayout {

    private static final boolean[][] STATES = new boolean[][]{
        {true, true, true, true, true, true, true, true, true},
        {true, true, true, true, false, true, true, true, true},
        {true, true, true, false, true, false, true, true, true},
        {true, true, true, false, false, false, true, true, true},
        {true, true, false, false, true, false, false, true, true},
        {true, true, false, false, false, false, false, true, true},
        {true, false, false, false, true, false, false, false, true},
        {true, false, false, false, false, false, false, false, true},
        {false, false, false, false, true, false, false, false, false},
        {false, false, false, false, false, false, false, false, false}
    };

    private static final int[] NEW_NEXT_SLOT = {4, 5, 5, 6, 6, 7, 7, 8, 8, 9};

    public CenteredInventoryLayout(InventoryGUI gui, int size) {
        super(gui, size);
    }

    public CenteredInventoryLayout(InventoryGUI gui, CenteredInventoryLayout layout) {
        super(gui, layout);
    }

    @Override
    public int firstSlot() {
        return CENTER_SLOT;
    }

    @Override
    public int nextSlot() {
        if (slot >= getSize()) {
            slot = -1;
            return slot;
        }

        int firstInLine = slot - slot % LINE_LENGTH;// First slot of first line that isn't filled
        boolean[] scan = scan(slot / LINE_LENGTH);
        int sid = -1;
        for (boolean[] state : STATES) {
            sid++;
            if (!Arrays.equals(scan, state)) {
                continue;
            }
            if (state[CENTER_SLOT]) {
                shiftRelativelyIf(i -> i > CENTER_SLOT + firstInLine && i < firstInLine + LINE_LENGTH, -1);
            } else {
                if (!state[0]) {
                    slot = firstInLine + CENTER_SLOT + LINE_LENGTH;
                    if (slot >= getSize()) {
                        slot = -1;
                    }
                    break;
                }
                shiftRelativelyIf(i -> i > firstInLine && i <= firstInLine + CENTER_SLOT, -1);
            }
            slot = firstInLine + NEW_NEXT_SLOT[sid];
            break;
        }
        return slot;
    }

    private boolean[] scan(int line) {
        boolean[] scan = new boolean[LINE_LENGTH];
        int j = 0;
        for (int i = line * LINE_LENGTH; i < (line + 1) * LINE_LENGTH; i++) {
            scan[j] = components[i] == null;
            j++;
        }
        return scan;
    }

    @Override
    public CenteredInventoryLayout copy(InventoryGUI gui) {
        return new CenteredInventoryLayout(gui, this);
    }

}
