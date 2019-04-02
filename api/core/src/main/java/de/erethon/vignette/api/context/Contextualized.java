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

import java.util.List;
import org.bukkit.entity.Player;

/**
 * Represents an object that can be modified by {@link ContextModifier}s.
 *
 * @param <T> the type itself
 * @author Daniel Saukel
 */
public interface Contextualized<T extends Contextualized<T>> {

    /**
     * Returns the {@link ContextModifier}s.
     * <p>
     * {@link #applyAllContextModifiers(org.bukkit.entity.Player)} applies
     * the modifiers from the lowest to the highest index.
     *
     * @return the {@link ContextModifier}s
     */
    List<ContextModifier<T>> getContextModifiers();

    /**
     * Adds a {@link ContextModifier} to the contextualized object.
     *
     * @param ctxt the modifier to add
     */
    void addContextModifier(ContextModifier<T> ctxt);

    /**
     * Removes a {@link ContextModifier} from the contextualized object.
     *
     * @param ctxt the modifier to remove
     */
    void removeContextModifier(ContextModifier<T> ctxt);

    /**
     * Sets the {@link ContextModifier}s.
     *
     * @param ctxts the modifiers to set
     */
    void setContextModifiers(List<ContextModifier<T>> ctxts);

    /**
     * Applies all {@link ContextModifier}s from the lowest to the highest
     * index of {@link #getContextModifiers()} to the contextualized object.
     * <p>
     * Consider making a {@link #copy()} first as this changes the object's state.
     *
     * @param player the player
     */
    default void applyAllContextModifiers(Player player) {
        getContextModifiers().forEach(m -> m.apply((T) this, player));
    }

    /**
     * Returns an exact copy of the contextualized object.
     *
     * @return an exact copy of the contextualized object
     */
    Contextualized<T> copy();

}
