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

import de.erethon.vignette.api.action.CloseListener;
import de.erethon.vignette.api.component.Component;
import de.erethon.vignette.api.context.Contextualized;
import de.erethon.vignette.api.layout.Layout;
import de.erethon.vignette.api.request.RequestParticipator;
import java.util.Collection;
import java.util.function.Predicate;
import org.bukkit.entity.Player;

/**
 * Represents a graphical user interface.
 *
 * @param <T> the type itself
 * @author Daniel Saukel
 */
public interface GUI<T extends GUI<T>> extends Contextualized<T> {

    /**
     * Returns the title text.
     *
     * @return the title text
     */
    String getTitle();

    /**
     * Sets the title to a text.
     *
     * @param text the text to set
     */
    void setTitle(String text);

    /**
     * Returns a Collection of the Components.
     *
     * @return a Collection of the Components
     */
    default Collection<Component<?, T>> getComponents() {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + "has no layout set");
        }
        return getLayout().getComponents();
    }

    /**
     * Adds a Component to the GUI.
     *
     * @param component the Component to add
     * @return if adding the Component was successful
     */
    default boolean add(Component<?, T> component) {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + " has no layout set");
        }
        return getLayout().add(component);
    }

    /**
     * Attempts to remove a Component from the GUI.
     * <p>
     * Fails silently if the GUI does not contain it.
     *
     * @param component the Component to remove
     * @return if removing the Component was successful
     */
    default boolean remove(Component<?, T> component) {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + "has no layout set");
        }
        return getLayout().remove(component);
    }

    /**
     * Attempts to remove all Components of the GUI that satisfy the given predicate.
     *
     * @param filter a predicate which returns true for Components to be removed
     */
    default void removeIf(Predicate<? super Component<?, T>> filter) {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + "has no layout set");
        }
        getLayout().removeIf(filter);
    }

    /**
     * Removes all Components from the GUI.
     */
    default void clear() {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + "has no layout set");
        }
        getLayout().clear();
    }

    /**
     * Returns if there is still space for further components in this GUI
     *
     * @return if there is still space for further components in this GUI
     */
    default boolean hasSpaceLeft() {
        if (getLayout() == null) {
            throw new IllegalStateException("The GUI " + toString() + "has no layout set");
        }
        return getLayout().hasSpaceLeft();
    }

    /**
     * Returns the Layout.
     *
     * @return the Layout
     */
    Layout<T> getLayout();

    /**
     * Sets the Layout.
     *
     * @param layout the Layout to set
     */
    void setLayout(Layout<T> layout);

    /**
     * Returns the CloseListener attached to this GUI.
     *
     * @return the CloseListener attached to this GUI
     */
    CloseListener getCloseListener();

    /**
     * Sets the CloseListener attached to this GUI.
     *
     * @param listener the listener to set
     */
    void setCloseListener(CloseListener listener);

    /**
     * If this GUI shall be unregistered automatically when closed.
     *
     * @return if this GUI shall be unregistered automatically when closed.
     */
    boolean isTransient();

    /**
     * Sets if this GUI shall be unregistered automatically when closed.
     *
     * @param isTransient if this GUI shall be unregistered automatically when closed.
     */
    void setTransient(boolean isTransient);

    /**
     * Returns a transient copy if the GUI has context modifiers and applies all of them; returns the GUI itself if not.
     *
     * @param viewer the viewer
     * @return a transient copy if the GUI has context modifiers and applies all of them; returns the GUI itself if not
     */
    GUI getContextualizedCopy(Player viewer);

    /**
     * Opens the GUI to a player and adds him to the viewers Collection.
     * <p>
     * Fails silently if the player is not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param player the Player
     * @return the opened GUI. It might be a copy of this object depending on the implementation
     */
    T open(Player player);

    /**
     * Opens the GUI to an array of players and adds them to the viewers Collection.
     * <p>
     * Ignores Players who are not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param players the Players
     */
    default void open(Player... players) {
        for (Player player : players) {
            open(player);
        }
    }

    /**
     * Opens the GUI to all players represented by an array of RequestParticipators and adds them to the viewers Collection.
     * <p>
     * Ignores Players who are not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param requestParticipators the RequestParticipators
     */
    default void open(RequestParticipator... requestParticipators) {
        for (RequestParticipator rp : requestParticipators) {
            Collection<Player> players = rp.getRequestPlayers();
            open(players.toArray(new Player[players.size()]));
        }
    }

    /**
     * Returns a Collection of the Players viewing the GUI.
     * <p>
     * To remove Players from this Collection, use {@link #close(Player...)}.
     *
     * @return the Players who are viewing the GUI
     */
    Collection<Player> getViewers();

    /**
     * Closes the GUI to an array of Players.
     * <p>
     * If the array is empty, the GUI is closed to all players.
     * <p>
     * Ignores Players who are not online.
     *
     * @param players the Players to remove from the viewers' Collection
     */
    void close(Player... players);

    /**
     * Starts tracking the GUI.
     */
    default void register() {
        VignetteAPI.register(this);
    }

    /**
     * Stops tracking the GUI.
     */
    default void unregister() {
        VignetteAPI.unregister(this);
    }

    /**
     * Returns if the GUI is registered.
     *
     * @return if the GUI is registered
     */
    default boolean isRegistered() {
        return VignetteAPI.isRegistered(this);
    }

    /**
     * Returns an exact copy of the GUI.
     *
     * @return an exact copy of the GUI
     */
    @Override
    T copy();

}
