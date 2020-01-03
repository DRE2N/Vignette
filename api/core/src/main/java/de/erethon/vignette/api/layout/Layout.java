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

import de.erethon.vignette.api.GUI;
import de.erethon.vignette.api.component.Component;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Contains methods to arrange the {@link de.erethon.vignette.api.component.Component}s of a GUI in a certain reproducible way.
 *
 * @param <T> the GUI implementation
 * @author Daniel Saukel
 */
public interface Layout<T extends GUI> {

    /**
     * Returns the GUI attached to this layout
     *
     * @return the GUI attached to this layout
     */
    T getGUI();

    /**
     * Returns a Collection of the Components.
     *
     * @return a Collection of the Components
     */
    Collection<Component<?, T>> getComponents();

    /**
     * Adds a Component to the GUI.
     *
     * @param component the Component to add
     * @return if adding the Component was successful
     */
    boolean add(Component<?, T> component);

    /**
     * Attempts to remove a Component from the GUI.
     * <p>
     * Fails silently if the GUI does not contain it.
     *
     * @param component the Component to remove
     * @return if removing the Component was successful
     */
    boolean remove(Component<?, T> component);

    /**
     * Attempts to remove all Components of the GUI that satisfy the given predicate.
     *
     * @param filter a predicate which returns true for Components to be removed
     */
    default void removeIf(Predicate<? super Component<?, T>> filter) {
        getComponents().stream().filter(filter).forEach(c -> remove(c));
    }

    /**
     * Removes all Components from the GUI.
     */
    void clear();

    /**
     * Returns if there is still space for further components in the {@link GUI} attached to this layout
     *
     * @return if there is still space for further components in this GUI
     */
    boolean hasSpaceLeft();

    /**
     * Returns a copy of the Layout.
     * <p>
     * {@link de.erethon.vignette.api.component.Component}s are copied as well.
     *
     * @param gui the GUI that is to be attached to the copy
     * @return a copy of the Layout
     */
    Layout<T> copy(T gui);

}
