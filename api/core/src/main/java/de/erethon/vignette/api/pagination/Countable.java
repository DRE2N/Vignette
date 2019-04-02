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
package de.erethon.vignette.api.pagination;

/**
 * Represents one countable of a {@link de.erethon.vignette.api.GUI}.
 *
 * @author Daniel Saukel
 */
public interface Countable {

    /**
     * Returns the number of this Countable.
     *
     * @return the number of this Countable
     */
    int getNumber();

}
