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
package de.erethon.vignette.api.layout;

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.Paginated;
import de.erethon.vignette.api.PaginatedInventoryGUI;
import de.erethon.vignette.api.component.Component;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.component.InventoryButtonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Layout base for inventory GUI pagination.
 *
 * @author Daniel Saukel
 */
public abstract class PaginatedInventoryLayout implements InventoryLayout {

    public static final InventoryButton PREVIOUS_PAGE = new InventoryButtonBuilder()
            .title(ChatColor.GOLD + "PREVIOUS PAGE")
            .sound("ui.button.click")
            .onInteract(l -> {
                if (l.getGUI() instanceof Paginated) {
                    Paginated gui = ((Paginated) l.getGUI());
                    Player player = l.getPlayer();
                    gui.open(gui.getOpenedPage(player) - 1, player);
                }
            })
            .build();
    public static final InventoryButton NEXT_PAGE = new InventoryButtonBuilder()
            .title(ChatColor.GOLD + "NEXT PAGE")
            .sound("ui.button.click")
            .onInteract(l -> {
                if (l.getGUI() instanceof Paginated) {
                    Paginated gui = ((Paginated) l.getGUI());
                    Player player = l.getPlayer();
                    gui.open(gui.getOpenedPage(player) + 1, player);
                }
            })
            .build();
    public static final InventoryButton PLACEHOLDER = new InventoryButtonBuilder()
            .icon(Material.getMaterial("WHITE_STAINED_GLASS_PANE") != null ? Material.getMaterial("WHITE_STAINED_GLASS_PANE") : Material.getMaterial("STAINED_GLASS_PANE"))
            .title(ChatColor.RESET.toString())
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

    private PaginatedInventoryGUI gui;

    protected Stack<Component<?, InventoryGUI>[]> components;
    protected int page = -1;
    protected int slot;
    private int size;
    private PaginationButtonPosition paginationButtonPosition;
    private boolean switchButtonLinePlaceholdersEnabled;

    protected PaginatedInventoryLayout(PaginatedInventoryGUI gui, int size, PaginationButtonPosition paginationButtonPosition) {
        components = new Stack<>();
        this.size = size;
        this.gui = gui;
        this.paginationButtonPosition = paginationButtonPosition;
        newPage();
    }

    protected PaginatedInventoryLayout(PaginatedInventoryGUI gui, PaginatedInventoryLayout layout) {
        this(gui, layout.components.get(0).length, layout.paginationButtonPosition);
        slot = layout.slot;
        for (Component<?, InventoryGUI>[] original : layout.components) {
            Component<?, InventoryGUI>[] copy = components.push(new Component[getSize()]);
            for (int i = 0; i < getSize(); i++) {
                if (original[i] != null) {
                    copy[i] = (Component<?, InventoryGUI>) original[i].copy();
                }
            }
        }
        switchButtonLinePlaceholdersEnabled = layout.switchButtonLinePlaceholdersEnabled;
    }

    @Override
    public InventoryGUI getGUI() {
        return gui;
    }

    @Override
    public Collection<Component<?, InventoryGUI>> getComponents() {
        Collection<Component<?, InventoryGUI>> copy = new ArrayList<>(getSize() * components.size());
        components.forEach(p -> copy.addAll(Arrays.asList(p)));
        return copy;
    }

    /**
     * Returns a List that contains one Collection of Components per page ordered by their number.
     *
     * @return a List that contains one Collection of Components per page ordered by their number.
     */
    public List<Collection<Component<?, InventoryGUI>>> getComponentsPerPage() {
        List<Collection<Component<?, InventoryGUI>>> copy = new ArrayList<>(components.size());
        for (Component<?, InventoryGUI>[] component : components) {
            copy.add(Arrays.asList(component));
        }
        return copy;
    }

    @Override
    public Component<?, InventoryGUI> getComponent(int slot) {
        int page = 0;
        while (slot >= getSize()) {
            slot -= getSize();
            page++;
        }
        return getComponent(page, slot);
    }

    /**
     * Returns the component in a specific slot on a specific page.
     * <p>
     * This does not return the global slot like {@link #getComponent(int)} but the
     *
     * @param page the page number
     * @param slot the slot at the page
     * @return the component in a specific slot on a specific page
     */
    public Component<?, InventoryGUI> getComponent(int page, int slot) {
        return components.get(page)[slot];
    }

    @Override
    public boolean fillIf(Component<?, InventoryGUI> component, Predicate<Integer> filter) {
        boolean success = false;
        for (Component<?, InventoryGUI>[] page : components) {
            for (int i = 0; i < getSize(); i++) {
                if (filter.test(i)) {
                    page[i] = component;
                    success = true;
                }
            }
        }
        return success;
    }

    @Override
    public boolean setToCurrent(Component<?, InventoryGUI> component) {
        if (currentSlot() > getSize()) {
            return false;
        }
        components.get(currentPage())[currentSlot()] = component;
        return true;
    }

    @Override
    public boolean set(int slot, Component<?, InventoryGUI> component) {
        int page = 0;
        while (slot >= getSize()) {
            slot -= getSize();
            page++;
        }
        return set(page, slot, component);
    }

    /**
     * Sets a Component to a specific slot on a specific page.
     *
     * @param page      the page number
     * @param slot      the slot number
     * @param component the Component to set
     * @return if setting the Component was successful
     */
    public boolean set(int page, int slot, Component<?, InventoryGUI> component) {
        if (page >= components.size() || slot >= getSize()) {
            return false;
        }
        components.get(page)[slot] = component;
        return true;
    }

    @Override
    public boolean shift(int sourceSlot, int targetSlot) {
        int sourcePage = 0;
        while (sourceSlot >= getSize()) {
            sourceSlot -= getSize();
            sourcePage++;
        }
        if (sourcePage >= components.size()) {
            return false;
        }
        int targetPage = 0;
        while (targetSlot >= getSize()) {
            targetSlot -= getSize();
            targetPage++;
        }
        if (targetPage >= components.size()) {
            return false;
        }
        Component<?, InventoryGUI> source = components.get(sourcePage)[sourceSlot];
        components.get(sourcePage)[sourceSlot] = null;
        if (components.get(targetPage)[targetSlot] != null) {
            return false;
        }
        components.get(targetPage)[targetSlot] = source;
        return true;
    }

    @Override
    public boolean remove(Component<?, InventoryGUI> component) {
        for (Component<?, InventoryGUI>[] components : components) {
            for (int i = 0; i < getSize(); i++) {
                Component c = components[i];
                if (c != null && c.equals(component)) {
                    components[i] = null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clear() {
        components.clear();
        page = -1;
        newPage();
    }

    /**
     * Returns the amount of pages.
     *
     * @return the amount of pages
     */
    public int getPages() {
        return components.size();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public InventoryButton getButton(ItemStack itemStack) {
        for (Component<?, InventoryGUI>[] components : components) {
            Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                    .filter(c -> c instanceof InventoryButton)
                    .filter(c -> ((InventoryButton) c).is(itemStack))
                    .findFirst();
            if (button.isPresent()) {
                return (InventoryButton) button.get();
            }
        }
        return null;
    }

    @Override
    public InventoryButton getButton(ItemStack itemStack, Player contextPlayer) {
        for (Component<?, InventoryGUI>[] components : components) {
            Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                    .filter(c -> c instanceof InventoryButton)
                    .filter(c -> ((InventoryButton) c).is(itemStack, contextPlayer))
                    .findFirst();
            if (button.isPresent()) {
                return (InventoryButton) button.get();
            }
        }
        return null;
    }

    @Override
    public Component<?, InventoryGUI> getCurrent() {
        if (currentSlot() >= getSize()) {
            throw new IndexOutOfBoundsException("Size: " + getSize() + "; Current slot: " + currentSlot());
        }
        return components.get(currentPage())[currentSlot()];
    }

    /**
     * Returns the page where the current button to add will be inserted.
     *
     * @return the page where the current button to add will be inserted
     */
    public int currentPage() {
        return page;
    }

    @Override
    public int currentSlot() {
        return slot;
    }

    /**
     * Adds a new, empty page.
     */
    public void newPage() {
        components.add(new Component[getSize()]);
        int i = components.size() - 1;
        set(i, getSwitchButtonSlot(false), PREVIOUS_PAGE);
        set(i, getSwitchButtonSlot(true), NEXT_PAGE);
        if (areSwitchButtonLinePlaceholdersEnabled()) {
            addSwitchButtonLinePlaceholders(i);
        }
        slot = firstSlot();
        page++;
    }

    /**
     * Returns the position of the button to switch the page.
     *
     * @param nextPage true for the next page, false for the previous page
     * @return the position of the button to switch the page
     */
    public int getSwitchButtonSlot(boolean nextPage) {
        int sbslot = -1;
        switch (paginationButtonPosition) {
            case TOP:
                sbslot = 0;
                break;
            case CENTER:
                sbslot = 9 * (getSize() / 9 / 2);
                break;
            case BOTTOM:
                sbslot = getSize() - 9;
        }
        return nextPage ? sbslot + 8 : sbslot;
    }

    /**
     * Returns if the line between the previous / next page buttons shall be filled with placeholders.
     *
     * @return if the line between the previous / next page buttons shall be filled with placeholders
     */
    public boolean areSwitchButtonLinePlaceholdersEnabled() {
        return switchButtonLinePlaceholdersEnabled;
    }

    /**
     * Sets if the line between the previous / next page buttons shall be filled with placeholders.
     *
     * @param enabled if the line between the previous / next page buttons shall be filled with placeholders
     */
    public void setSwitchButtonLinePlaceholdersEnabled(boolean enabled) {
        switchButtonLinePlaceholdersEnabled = enabled;
        if (enabled) {
            for (int i = 0; i < getPages(); i++) {
                addSwitchButtonLinePlaceholders(i);
            }
        } else {
            removeIf(c -> c.equals(PLACEHOLDER));
        }
    }

    private void addSwitchButtonLinePlaceholders(int page) {
        int sbslot = getSwitchButtonSlot(false);
        set(page, sbslot + 1, PLACEHOLDER);
        set(page, sbslot + 2, PLACEHOLDER);
        set(page, sbslot + 3, PLACEHOLDER);
        set(page, sbslot + 4, PLACEHOLDER);
        set(page, sbslot + 5, PLACEHOLDER);
        set(page, sbslot + 6, PLACEHOLDER);
        set(page, sbslot + 7, PLACEHOLDER);
    }

}
