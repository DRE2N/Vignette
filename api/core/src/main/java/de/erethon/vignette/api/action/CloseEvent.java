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
package de.erethon.vignette.api.action;

import de.erethon.vignette.api.GUI;
import org.bukkit.entity.Player;

/**
 * Created internally when a player closes a {@link de.erethon.vignette.api.GUI}.
 *
 * @author Daniel Saukel
 */
public class CloseEvent {

    private GUI gui;
    private Player player;

    public CloseEvent(GUI gui, Player player) {
        this.gui = gui;
        this.player = player;
    }

    /**
     * Returns the GUI that was closed.
     *
     * @return the GUI that was closed
     */
    public GUI getGUI() {
        return gui;
    }

    /**
     * Returns the player who closed the GUI.
     *
     * @return the player who closed the GUI
     */
    public Player getPlayer() {
        return player;
    }

}
