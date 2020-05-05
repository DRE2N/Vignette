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
import de.erethon.vignette.api.component.Button;
import org.bukkit.entity.Player;

/**
 * Created internally when a player interacts with a GUI {@link de.erethon.vignette.api.component.Component}.
 *
 * @author Daniel Saukel
 */
public class InteractionEvent {

    private GUI gui;
    private Button button;
    private Player player;
    private Action action;
    private boolean clickCancelled;

    public InteractionEvent(GUI gui, Button button, Player player, Action action) {
        this.gui = gui;
        this.button = button;
        this.player = player;
        this.action = action;
    }

    /**
     * Returns the GUI that was interacted with.
     *
     * @return the GUI that was interacted with
     */
    public GUI getGUI() {
        return gui;
    }

    /**
     * Returns the Button that was interacted with.
     *
     * @return the Button that was interacted with
     */
    public Button getButton() {
        return button;
    }

    /**
     * Returns the player who interacted with the GUI.
     *
     * @return the player who interacted with the GUI
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns an Action that matchs the action that occurred.
     *
     * @return an Action that matchs the action that occurred
     */
    public Action getAction() {
        return action;
    }

    /**
     * Returns if the click itself is cancelled after this event is called.
     *
     * @see #setClickCancelled(boolean)
     * @return if the click itself is cancelled after this event is called.
     */
    public boolean isClickCancelled() {
        return clickCancelled;
    }

    /**
     * Sets if the click itself is cancelled after this event is cancelled.
     *
     * @param cancelled if the click itself is cancelled after this event is cancelled
     */
    public void setClickCancelled(boolean cancelled) {
        clickCancelled = cancelled;
    }

}
