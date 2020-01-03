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

import org.bukkit.Material;

/**
 * A builder for one-line instantiation of {@link InventoryButton}s.
 *
 * @author Daniel Saukel
 */
public class InventoryButtonBuilder extends ButtonBuilder<InventoryButtonBuilder, InventoryButton> {

    private Material icon = Material.PAPER;
    private int number = 1;
    private String[] lines;
    private boolean stealable;

    /**
     * Sets the icon of this button.
     *
     * @param icon the icon
     * @return the builder
     */
    public InventoryButtonBuilder icon(Material icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Sets the attached number to a value.
     * <p>
     * 1 results in no number being used.
     * <p>
     * The behavior for 0 and negative values is undefined. In earlier Minecraft versions, they work and are displayed in red.
     *
     * @param number the number to set
     * @return the builder
     */
    public InventoryButtonBuilder number(int number) {
        this.number = number;
        return this;
    }

    /**
     * Sets additional lines to the button.
     *
     * @param lines the additional lines to set
     * @return the builder
     */
    public InventoryButtonBuilder lines(String... lines) {
        this.lines = lines;
        return this;
    }

    /**
     * Sets if the button may be taken from the GUI.
     * <p>
     * This is false by default.
     *
     * @param stealable if the button may be taken from the GUI
     * @return the builder
     */
    public InventoryButtonBuilder stealable(boolean stealable) {
        this.stealable = stealable;
        return this;
    }

    @Override
    public InventoryButton build() {
        InventoryButton button = new InventoryButton(icon, number, title, lines);
        button.setStealable(stealable);
        button.setSound(sound);
        button.setInteractionListener(interactionListener);
        button.setContextModifiers(contextModifiers);
        return button;
    }

}
