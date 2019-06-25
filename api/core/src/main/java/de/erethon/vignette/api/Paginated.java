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

import org.bukkit.entity.Player;

/**
 * Represents a graphical user interface with multiple pages.
 *
 * @author Daniel Saukel
 */
public interface Paginated extends PlayerViewable {

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
     * Returns the amount of pages.
     *
     * @return the amount of pages
     */
    int getPages();

    @Override
    default void open(Player... players) {
        open(0, players);
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

    /**
     * Returns the title text of a specific page.
     *
     * @param page the page
     * @return the title text of a specific page
     */
    String getTitle(int page);

    /**
     * Sets the title of a specific page to a text.
     *
     * @param page  the page
     * @param title the title text to set
     */
    void setTitle(int page, String title);

    /**
     * Returns the last page opened by this player; null if never opened.
     *
     * @param player the player who opened the GUI
     * @return the last page opened by this player; null if never opened
     */
    Integer getOpenedPage(Player player);

}
