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
package de.erethon.vignette.api.context;

import org.bukkit.entity.Player;

/**
 * Context modifiers can be used to set contents that cannot be
 * statically added but depend on the player who requests them
 * and the state of the game environment.
 * <p>
 * Usage example for a button:
 * <pre>{@code
 * InventoryButton myButton = new InventoryButtonBuilder()
 *         .contextModifier((t, p) -> t.setTitle("You are" + (p.isOp() ? " " : " NOT ") + "OP"))
 *         .build();
 * }</pre>
 * <p>
 * In this case, the {@link de.erethon.vignette.api.component.Button} "myButton" is only
 * shown to players that are server operators.
 *
 * Usage example for a GUI:
 * <pre>{@code
 * InventoryGUI myGUI = new InventoryGUI(ChatColor.BLUE + "Context Modifier Test");
 * myGUI.setLayout(new FlowInventoryLayout(myGUI, 9));
 * myGUI.addContextModifier((t, p) -> t.setTitle("You are" + (p.isOp() ? " " : " NOT ") + "OP"));
 * }</pre>
 * <p>
 * As ContextModifiers may modify their {@link Contextualized}
 * containers, it should be considered to copy the Contextualized
 * before applying the modifiers.
 *
 * @param <T> the type to modify
 *
 * @author Daniel Saukel
 */
@FunctionalInterface
public interface ContextModifier<T extends Contextualized> {

    /**
     * Applies the modifier to a {@link Contextualized} object.
     *
     * @param target the Contextualized object
     * @param player the player requesting content
     */
    void apply(T target, Player player);

}
