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
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Contains common properties for inventory GUI layouts.
 *
 * @author Daniel Saukel
 */
public interface InventoryLayout extends Layout<InventoryGUI> {

    static final int LINE_LENGTH = 9;
    static final int CENTER_SLOT = 4;

    /**
     * Returns the component in a specific slot.
     *
     * @param slot the slot
     * @return the component in a specific slot
     */
    Component<?, InventoryGUI> getComponent(int slot);

    @Override
    default boolean add(Component<?, InventoryGUI> component) {
        if (!hasSpaceLeft()) {
            return false;
        } else if (getCurrent() == null) {
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
    boolean fillIf(Component<?, InventoryGUI> component, Predicate<Integer> filter);

    /**
     * Adds a component to the current slot.
     *
     * @param component the component
     * @return if setting the Component was successful
     */
    boolean setToCurrent(Component<?, InventoryGUI> component);

    /**
     * Adds a Component to a specific slot in the GUI.
     * <p>
     * This however might break the purpose of the layout.
     *
     * @param slot      the slot
     * @param component the Component to add
     * @return if setting the Component was successful
     */
    boolean set(int slot, Component<?, InventoryGUI> component);

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
    boolean shift(int sourceSlot, int targetSlot);

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
    default boolean shiftRelatively(int sourceSlot, int relativeTargetSlot) {
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
    default boolean shiftRelativelyIf(Predicate<Integer> filter, int relativeTargetSlot) {
        boolean success = false;
        for (int i = 0; i < getSize(); i++) {
            if (filter.test(i) && shiftRelatively(i, relativeTargetSlot)) {
                success = true;
            }
        }
        return success;
    }

    /**
     * Returns the amount of slots in the inventory.
     *
     * @return the amount of slots in the inventory
     */
    int getSize();

    @Override
    default boolean hasSpaceLeft() {
        return currentSlot() != -1;
    }

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     *
     * @param itemStack the ItemStack
     * @return the first InventoryButton that is equal to the ItemStack
     */
    InventoryButton getButton(ItemStack itemStack);

    /**
     * Returns the first InventoryButton which represents the {@link org.bukkit.inventory.ItemStack}.
     * <p>
     * Also checks versions of the button modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     *
     * @param itemStack     the ItemStack
     * @param contextPlayer the Player that is used to check the button's form modified by {@link de.erethon.vignette.api.context.ContextModifier}s.
     * @return the first InventoryButton that is equal to the ItemStack
     */
    InventoryButton getButton(ItemStack itemStack, Player contextPlayer);

    /**
     * Returns the Component at the {@link #currentSlot()}.
     *
     * @return the Component at the {@link #currentSlot()}
     */
    Component<?, InventoryGUI> getCurrent();

    /**
     * Returns the slot where the first button to add was or will be inserted.
     *
     * @return the slot where the first button to add was or will be inserted
     */
    default int firstSlot() {
        return 0;
    }

    /**
     * Returns the slot where the current button to add will be inserted.
     *
     * @return the slot where the current button to add will be inserted;
     *         -1 if no space is left
     */
    int currentSlot();

    /**
     * Returns the slot where the next button to add will be inserted.
     * <p>
     * Sets the current slot to the next slot.
     *
     * @return the slot where the next button to add will be inserted;
     *         -1 if no space is left
     */
    int nextSlot();

}
