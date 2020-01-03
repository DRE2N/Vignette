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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GUI interaction types. Subject to change.
 *
 * @deprecated draft
 * @author Daniel Saukel
 */
@Deprecated
public enum Action {

    CLICK,
    LEFT_CLICK(CLICK),
    RIGHT_CLICK(CLICK),
    WHEEL_CLICK(CLICK),
    HOVER;

    private Set<Action> parents;

    Action(Action... parents) {
        this.parents = Stream.of(parents).collect(Collectors.toSet());
    }

    public boolean isSubsumable(Action other) {
        return this == other || parents.contains(other);
    }

}
