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
package de.erethon.vignette.api.context;

/**
 * A class that is supposed to be attached to a {@link Contextualized} object.
 * It adds further information to the status of the object.
 * <p>
 * A usage example can be a GUI that is used to set something up
 * where closing it means finishing the setup process.
 * However, some settings might require a submenu opened through a button
 * in the main setup menu for more complex setups.
 * But opening the submenu would also close the main menu and thus trigger
 * the {@link de.erethon.vignette.api.action.CloseListener} that wouldn't
 * be able to differentiate between when the GUI is closed in favor of a
 * submenu or to finish the setup. A StatusModifier can be added as a
 * marker to make the listener aware of this:
 * <pre>{@code
 * private static final StatusModifier OPENING_SUBMENU = new StatusModifier("OPENING_SUBMENU");
 * ...
 * buttonToOpenSubMenu.addInteractionListener(e -> {
 *     e.getGUI().addStatusModifier(OPENING_SUBMENU);
 *     ...
 * });
 * ...
 * mainMenuGUI.addCloseListener(e -> {
 *     if (e.getGUI().hasStatusModifier(OPENING_SUBMENU)) {
 *         return;
 *     } else ...
 * }
 * }</pre>
 *
 * @param <T> the data type of this modifier
 * @author Daniel Saukel
 */
public class StatusModifier<T> {

    private String key;
    private T value;

    /**
     * Instantiates the StatusModifier without a value.
     * <p>
     * This can be used instead of boolean values where the check that the modifier exists is enough information.
     *
     * @param key the key
     */
    public StatusModifier(String key) {
        this.key = key;
    }

    public StatusModifier(String key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key String used to identify this StatusModifier.
     *
     * @return the key String used to identify this StatusModifier
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the content of this StatusModifier.
     * <p>
     * These values may be used to add further complexity to a status.
     *
     * @return the content of this StatusModifier
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the content of this StatusModifier.
     * <p>
     * These values may be used to add further complexity to a status.
     *
     * @param value the value
     */
    public void setValue(T value) {
        this.value = value;
    }

}
