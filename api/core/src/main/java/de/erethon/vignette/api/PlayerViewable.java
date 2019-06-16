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
package de.erethon.vignette.api;

import de.erethon.vignette.api.request.RequestParticipator;
import java.util.Collection;
import org.bukkit.entity.Player;

/**
 * Something that can be showed to a {@link org.bukkit.entity.Player}.
 *
 * @author Daniel Saukel
 */
public interface PlayerViewable {

    /**
     * Opens the GUI to an array of players and adds them to the viewers Collection.
     * <p>
     * Ignores Players who are not online.
     * <p>
     * Triggers all associated {@link de.erethon.vignette.api.context.ContextModifier}s
     *
     * @throws IllegalStateException if the GUI is not registered
     * @param players the Players
     */
    void open(Player... players);

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

}
