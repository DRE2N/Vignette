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

import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.component.InventoryButtonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Mixin for inventory GUI pagination.
 *
 * @author Daniel Saukel
 */
public interface PaginatedInventoryLayout {

    public static final InventoryButton PREVIOUS_PAGE = new InventoryButtonBuilder()
            .title(ChatColor.GOLD + "PREVIOUS PAGE")
            .sound("ui.button.click")
            .build();
    public static final InventoryButton NEXT_PAGE = new InventoryButtonBuilder()
            .title(ChatColor.GOLD + "NEXT PAGE")
            .sound("ui.button.click")
            .build();
    public static final InventoryButton PLACEHOLDER = new InventoryButtonBuilder()
            .icon(Material.BLACK_STAINED_GLASS_PANE)// TODO: Breaks support for -MC 1.12.2
            .title("")
            .build();

    /**
     * Determines in which slots the previous / next page buttons show up
     */
    public enum PaginationButtonPosition {
        /**
         * The previous page button appears in the upper left corner, the next page button in the upper right corner.
         */
        TOP,
        /**
         * The previous page button appears on the left side in the line in the middle, the next page button on the right.
         */
        CENTER,
        /**
         * The previous page button appears in the upper left corner, the next page button in the upper right corner.
         */
        BOTTOM
    }

    default int getSwitchButtonSlot(PaginationButtonPosition pos, int guiSize, boolean nextPage) {
        int sbslot = -1;
        switch (pos) {
            case TOP:
                sbslot = 0;
                break;
            case CENTER:
                sbslot = 9 * (guiSize / 9 / 2);
                break;
            case BOTTOM:
                sbslot = guiSize - 9;
        }
        return nextPage ? sbslot + 8 : sbslot;
    }

    boolean areSwitchButtonLinePlaceholdersEnabled();

    void setSwitchButtonLinePlaceholdersEnabled(boolean enabled);

}
