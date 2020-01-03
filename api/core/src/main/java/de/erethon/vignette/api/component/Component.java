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

import de.erethon.vignette.api.GUI;
import de.erethon.vignette.api.context.Contextualized;

/**
 * A GUI Component.
 *
 * @param <THIS> the type itself
 * @param <TYPE> the GUI implementation
 * @author Daniel Saukel
 */
public interface Component<THIS extends Component<THIS, TYPE>, TYPE extends GUI> extends Contextualized<THIS> {
}
