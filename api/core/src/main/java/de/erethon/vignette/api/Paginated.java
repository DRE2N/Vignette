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

import org.bukkit.entity.Player;

/**
 * Represents a graphical user interface with multiple pages.
 *
 * @param <T> The type itself
 * @author Daniel Saukel
 */
public interface Paginated<T extends GUI<T>> extends GUI<T> {

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

    @Override
    default T open(Player player) {
        return open(0, player);
    }

    /**
     * Opens a specific page of the GUI to a player and adds him to the viewers Collection.
     * <p>
     * Fails silently if the player is not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param page   the page index to open. If the value is higher than or equal to {@link #getPages()},
     *               the first page will be opened; if it is lower than 0, the last page will be opened.
     * @param player the Player
     * @return the opened GUI. It might be a copy of this object depending on the implementation
     */
    T open(int page, Player player);

    /**
     * Opens a specific page of the GUI to an array of players and adds them to the viewers Collection.
     * <p>
     * Ignores Players who are not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param page    the page index to open. If the value is higher than or equal to {@link #getPages()},
     *                the first page will be opened; if it is lower than 0, the last page will be opened.
     * @param players the Players
     */
    default void open(int page, Player... players) {
        for (Player player : players) {
            open(page, player);
        }
    }

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
