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

import de.erethon.vignette.api.pagination.Countable;

/**
 * @author Daniel Saukel
 */
public class InventoryGUIPage extends InventoryGUI implements Countable {

    private int number;

    public InventoryGUIPage(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

}
