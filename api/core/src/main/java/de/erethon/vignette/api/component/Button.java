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
package de.erethon.vignette.api.component;

import de.erethon.vignette.api.GUI;
import de.erethon.vignette.api.action.InteractionListener;

/**
 * A {@link Component} that allows actions to be performed upon interaction.
 *
 * @param <THIS> the type itself
 * @param <TYPE> the GUI implementation
 * @author Daniel Saukel
 */
public interface Button<THIS extends Button<THIS, TYPE>, TYPE extends GUI> extends Component<THIS, TYPE> {

    /**
     * Returns the formatted title of the button.
     *
     * @return the formatted title of the button
     */
    String getTitle();

    /**
     * Sets the title to a text.
     * <p>
     * The text may be formatted or unformatted.
     *
     * @param text the text to set
     */
    void setTitle(String text);

    /**
     * Returns the sound String played to the player when the button is clicked.
     *
     * @return the sound String played to the player when the button is clicked
     */
    String getSound();

    /**
     * Sets the sound String played to the player when the button is clicked.
     *
     * @param sound the sound String
     */
    void setSound(String sound);

    /**
     * Returns the InteractionListener attached to this button.
     *
     * @return the InteractionListener attached to this button
     */
    InteractionListener getInteractionListener();

    /**
     * Sets the InteractionListener attached to this button.
     *
     * @param listener the listener to set
     */
    void setInteractionListener(InteractionListener listener);

}
