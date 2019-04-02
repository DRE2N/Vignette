/*
 * Written from 2015-2019 by Daniel Saukel
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
package de.erethon.vignette.util;

import de.erethon.vignette.api.request.RequestParticipator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public interface PlayerWrapper extends RequestParticipator {

    /**
     * Returns the Bukkit Player object.
     *
     * @return the Bukkit Player object
     */
    Player getPlayer();

    @Override
    default Collection<Player> getRequestPlayers() {
        return new ArrayList<>(Arrays.asList(getPlayer()));
    }

    /**
     * Returns the player's name
     *
     * @return the player's name
     */
    String getName();

    /**
     * Returns the player's unique ID
     *
     * @return the player's unique ID
     */
    UUID getUniqueId();

}
