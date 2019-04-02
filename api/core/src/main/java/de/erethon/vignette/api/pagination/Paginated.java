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
package de.erethon.vignette.api.pagination;

import de.erethon.vignette.api.PlayerViewable;
import java.util.Stack;
import org.bukkit.entity.Player;

/**
 * Represents a graphical user interface with multiple pages.
 *
 * @param <T> the Countable implementation type
 * @author Daniel Saukel
 */
public interface Paginated<T extends Countable> extends PlayerViewable {

    /**
     * Returns if the components of this GUI are automatically added to the next page if no space is left, or to the last one if space is freed.
     *
     * @return true if the components of this GUI are automatically added to the next page if no space is left, or to the last one if space is freed. false if
     *         not
     */
    boolean isComponentMoveUpEnabled();

    /**
     * Sets if the components of this GUI are automatically added to the next page if no space is left, or to the last one if space is freed.
     *
     * @param moveUp if the components of this GUI are automatically added to the next page if no space is left, or to the last one if space is freed
     */
    void setComponentMoveUpEnabled(boolean moveUp);

    /**
     * Returns the pages of this GUI.
     *
     * @return the pages of this GUI
     */
    Stack<T> getPages();

    /**
     * Creates a new page with the specifications of this GUI and adds it to the end of the stack.
     *
     * @return a new page with the specifications of this GUI
     */
    T newPage();

    @Override
    default void open(Player... players) {
        open(0);
    }

    /**
     * Opens the GUI at a specific page to an array of players and adds them to the viewers Collection.
     * <p>
     * Ignores Players who are not online.
     *
     * @param page    the page to open
     * @param players the Players
     */
    void open(int page, Player... players);

}
