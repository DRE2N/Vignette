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

import de.erethon.vignette.api.action.InteractionListener;
import de.erethon.vignette.api.context.ContextModifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder for one-line instantiation of {@link Button}s.
 *
 * @author Daniel Saukel
 * @param <THIS> the ButtonBuilder implementation
 * @param <TYPE> the Button implementation
 */
public abstract class ButtonBuilder<THIS extends ButtonBuilder<THIS, TYPE>, TYPE extends Button> {

    protected String title;
    protected String sound;
    protected InteractionListener interactionListener;
    protected List<ContextModifier<TYPE>> contextModifiers = new ArrayList<>();

    /**
     * Sets the title to a text.
     * <p>
     * The text may be formatted or unformatted.
     *
     * @param text the text to set
     * @return the builder
     */
    public THIS title(String text) {
        title = text;
        return (THIS) this;
    }

    /**
     * Sets the sound String played to the player when the button is clicked
     *
     * @param sound the sound String
     * @return the builder
     */
    public THIS sound(String sound) {
        this.sound = sound;
        return (THIS) this;
    }

    /**
     * Sets the InteractionListener attached to this button.
     *
     * @param listener the listener to set
     * @return the builder
     */
    public THIS onInteract(InteractionListener listener) {
        interactionListener = listener;
        return (THIS) this;
    }

    /**
     * Adds a context modifier to the button.
     *
     * @param ctxt the context modifier
     * @return the builder
     */
    public THIS contextModifier(ContextModifier<TYPE> ctxt) {
        contextModifiers.add(ctxt);
        return (THIS) this;
    }

    /**
     * Builds a {@link Button}.
     *
     * @throws IllegalStateException if obligatory attributes have not been set
     * @return the built {@link Button}
     */
    public abstract TYPE build();

}
