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
public abstract class InventoryLayout implements Layout<InventoryGUI> {

    public static final int LINE_LENGTH = 9;
    public static final int CENTER_SLOT = 4;

    private InventoryGUI gui;

    protected Component<?, InventoryGUI>[] components;
    protected int slot;

    /**
     * @param gui  the GUI
     * @param size the amount of slots in the GUI. Must be a multiple of 9
     */
    protected InventoryLayout(InventoryGUI gui, int size) {
        components = new Component[size];
        this.gui = gui;
    }

    protected InventoryLayout(InventoryGUI gui, InventoryLayout layout) {
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

    /**
     * Returns the component in a specific slot.
     *
     * @param slot the slot
     * @return the component in a specific slot
     */
    public Component<?, InventoryGUI> getComponent(int slot) {
        return components[slot];
    }

    @Override
    public boolean add(Component<?, InventoryGUI> component) {
        if (currentSlot() == -1) {
            return false;
        } else if (components[currentSlot()] == null) {
            setToCurrent(component);
            return true;
        } else {
            nextSlot();
            return add(component);
        }
    }

    /**
     * Attempts to add the Componens to all slots that satisfy the given predicate.
     *
     * @param component the component to fill the slots
     * @param filter    a predicate which returns true for slots to be filled
     * @return if adding to one or more slots was successful
     */
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

    /**
     * Adds a component to the current slot.
     *
     * @param component the component
     */
    public void setToCurrent(Component<?, InventoryGUI> component) {
        components[currentSlot()] = component;
    }

    /**
     * Adds a Component to a specific slot in the GUI.
     * <p>
     * This however might break the purpose of the layout.
     *
     * @param slot      the slot
     * @param component the Component to add
     */
    public void set(int slot, Component<?, InventoryGUI> component) {
        components[slot] = component;
    }

    /**
     * Attempts to shift a Component from a slot to another.
     * <p>
     * Removes the Component from the source slot.
     * <p>
     * If the target slot is not empty, the Component will not be added to it.
     *
     * @param sourceSlot the slot from where the Component is moved
     * @param targetSlot the slot where the Component shall be moved to
     * @return if the Component could be added to the target slot
     */
    public boolean shift(int sourceSlot, int targetSlot) {
        Component<?, InventoryGUI> source = components[sourceSlot];
        components[sourceSlot] = null;
        if (components[targetSlot] != null) {
            return false;
        }
        components[targetSlot] = source;
        return true;
    }

    /**
     * Attempts to shift a Component from a slot to another.
     * <p>
     * Removes the Component from the source slot.
     * <p>
     * If the target slot is not empty, the Component will not be added to it.
     *
     * @param sourceSlot         the slot from where the Component is moved
     * @param relativeTargetSlot the relative amount of slot between the source and the target
     * @return if the Component could be added to the target slot
     */
    public boolean shiftRelatively(int sourceSlot, int relativeTargetSlot) {
        return shift(sourceSlot, sourceSlot + relativeTargetSlot);
    }

    /**
     * Attempts to shift all Components that satisfy the given predicate from a slot to another.
     * <p>
     * Removes the Components from their source slots.
     * <p>
     * If the target slot is not empty, the Component will not be added to it.
     *
     * @param filter             a predicate which returns true for slots to be moved
     * @param relativeTargetSlot the relative amount of slot between the source and the target
     * @return if at least Component could be added to the target slot
     */
    public boolean shiftRelativelyIf(Predicate<Integer> filter, int relativeTargetSlot) {
        boolean success = false;
        for (int i = 0; i < getSize(); i++) {
            if (filter.test(i) && shiftRelatively(i, relativeTargetSlot)) {
                success = true;
            }
        }
        return success;
    }

    @Override
    public boolean remove(Component<?, InventoryGUI> component) {
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if (c != null && c.equals(component)) {
                components[i] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the amount of slots in the inventory.
     *
     * @return the amount of slots in the inventory
     */
    public int getSize() {
        return components.length;
    }

    @Override
    public boolean hasSpaceLeft() {
        return slot != -1;
    }

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     *
     * @param itemStack the ItemStack
     * @return the first InventoryButton that is equal to the ItemStack
     */
    public InventoryButton getButton(ItemStack itemStack) {
        Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                .filter(c -> c instanceof InventoryButton)
                .filter(c -> ((InventoryButton) c).is(itemStack))
                .findFirst();
        return button.isPresent() ? (InventoryButton) button.get() : null;
    }

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     * <p>
     * Also checks versions of the button modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @param itemStack     the ItemStack
     * @param contextPlayer the Player that is used to check the button's form modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     * @return the first InventoryButton that is equal to the ItemStack
     */
    public InventoryButton getButton(ItemStack itemStack, Player contextPlayer) {
        Optional<Component<?, InventoryGUI>> button = Stream.of(components)
                .filter(c -> c instanceof InventoryButton)
                .filter(c -> ((InventoryButton) c).is(itemStack, contextPlayer))
                .findFirst();
        return button.isPresent() ? (InventoryButton) button.get() : null;
    }

    /**
     * Returns the slot where the current button to add will be inserted.
     *
     * @return the slot where the current button to add will be inserted;
     *         -1 if no space is left
     */
    public int currentSlot() {
        return slot;
    }

    /**
     * Returns the slot where the next button to add will be inserted.
     * <p>
     * Sets the current slot to the next slot.
     *
     * @return the slot where the next button to add will be inserted;
     *         -1 if no space is left
     */
    public abstract int nextSlot();

}
