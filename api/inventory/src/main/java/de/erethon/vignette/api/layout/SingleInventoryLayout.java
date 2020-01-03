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
import de.erethon.vignette.api.component.Component;
import de.erethon.vignette.api.component.InventoryButton;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Contains common properties for inventory GUI layouts.
 *
 * @author Daniel Saukel
 */
public abstract class SingleInventoryLayout implements InventoryLayout {

    private InventoryGUI gui;

    protected Component<?, InventoryGUI>[] components;
    protected int slot;

    /**
     * @param gui  the GUI
     * @param size the amount of slots in the GUI. Must be a multiple of 9
     */
    protected SingleInventoryLayout(InventoryGUI gui, int size) {
        components = new Component[size];
        this.gui = gui;
        slot = firstSlot();
    }

    protected SingleInventoryLayout(InventoryGUI gui, SingleInventoryLayout layout) {
        this(gui, layout.getSize());
        slot = layout.slot;
        for (int i = 0; i < getSize(); i++) {
            if (layout.components[i] != null) {
                components[i] = (Component<?, InventoryGUI>) layout.components[i].copy();
            }
        }
    }

    @Override
    public InventoryGUI getGUI() {
        return gui;
    }

    @Override
    public Collection<Component<?, InventoryGUI>> getComponents() {
        return Arrays.asList(components);
    }

    @Override
    public Component<?, InventoryGUI> getComponent(int slot) {
        return components[slot];
    }

    @Override
    public boolean fillIf(Component<?, InventoryGUI> component, Predicate<Integer> filter) {
        boolean success = false;
        for (int i = 0; i < getSize(); i++) {
            if (filter.test(i)) {
                components[i] = component;
                success = true;
            }
        }
        return success;
    }

    @Override
    public boolean setToCurrent(Component<?, InventoryGUI> component) {
        if (currentSlot() > getSize()) {
            return false;
        }
        components[currentSlot()] = component;
        return true;
    }

    @Override
    public boolean set(int slot, Component<?, InventoryGUI> component) {
        if (slot >= getSize()) {
            return false;
        }
        components[slot] = component;
        return true;
    }

    @Override
    public boolean shift(int sourceSlot, int targetSlot) {
        if (sourceSlot >= getSize() || targetSlot >= getSize() || sourceSlot < 0 || targetSlot < 0) {
            return false;
        }
        Component<?, InventoryGUI> source = components[sourceSlot];
        components[sourceSlot] = null;
        if (components[targetSlot] != null) {
            return false;
        }
        components[targetSlot] = source;
        return true;
    }

    @Override
    public boolean remove(Component<?, InventoryGUI> component) {
        for (int i = 0; i < getSize(); i++) {
            Component<?, InventoryGUI> c = components[i];
            if (c != null && c.equals(component)) {
                components[i] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        components = new Component[getSize()];
        slot = firstSlot();
    }

    @Override
    public int getSize() {
        return components.length;
    }

    @Override
    public InventoryButton getButton(ItemStack itemStack) {
        Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                .filter(c -> c instanceof InventoryButton)
                .filter(c -> ((InventoryButton) c).is(itemStack))
                .findFirst();
        return button.isPresent() ? (InventoryButton) button.get() : null;
    }

    @Override
    public InventoryButton getButton(ItemStack itemStack, Player contextPlayer) {
        Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                .filter(c -> c instanceof InventoryButton)
                .filter(c -> ((InventoryButton) c).is(itemStack, contextPlayer))
                .findFirst();
        return button.isPresent() ? (InventoryButton) button.get() : null;
    }

    @Override
    public Component<?, InventoryGUI> getCurrent() {
        if (currentSlot() >= getSize()) {
            throw new IndexOutOfBoundsException("Size: " + getSize() + "; Current slot: " + currentSlot());
        }
        return components[currentSlot()];
    }

    @Override
    public int currentSlot() {
        return slot;
    }

}
