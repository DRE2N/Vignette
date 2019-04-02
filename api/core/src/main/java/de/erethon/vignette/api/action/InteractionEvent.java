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
package de.erethon.vignette.api.action;

import org.bukkit.entity.Player;

/**
 * Created internally when a player interacts with a GUI {@link de.erethon.vignette.api.component.Component}.
 *
 * @author Daniel Saukel
 */
public class InteractionEvent {

    private Player player;
    private Action action;

    public InteractionEvent(Player player, Action action) {
        this.player = player;
        this.action = action;
    }

    /**
     * Returns the player who interacted with the GUI
     *
     * @return the player who interacted with the GUI
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns an Action that matchs the action that occurred
     *
     * @return an Action that matchs the action that occurred
     */
    public Action getAction() {
        return action;
    }

}
