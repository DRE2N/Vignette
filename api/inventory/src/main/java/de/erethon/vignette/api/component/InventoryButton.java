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

import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.action.InteractionListener;
import de.erethon.vignette.api.context.ContextModifier;
import de.erethon.vignette.api.context.StatusModifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * An {@link org.bukkit.inventory.ItemStack} based {@link Button} for {@link de.erethon.vignette.api.InventoryGUI}s.
 *
 * @author Daniel Saukel
 */
public class InventoryButton implements Button<InventoryButton, InventoryGUI> {

    private ItemStack itemStack;
    private String sound;
    private boolean leftClickLocked = true, rightClickLocked = true;
    private InteractionListener interactionListener;
    private Set<StatusModifier<?>> statusModifiers = new HashSet<>();
    private List<ContextModifier<InventoryButton>> contextModifiers = new ArrayList<>();

    /**
     * Creates a new InventoryButton directly from an {@link org.bukkit.inventory.ItemStack}.
     * <p>
     * All values that {@link org.bukkit.inventory.ItemStack#clone()} supports are supported for a button.
     *
     * @param itemStack the ItemStack
     */
    public InventoryButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * @param icon   the icon
     * @param number the number attached to this button
     * <p>
     * 1 results in no number being used.
     * <p>
     * The behavior for 0 and negative values is undefined. In earlier Minecraft versions, they work and are displayed in red.
     * @param title  the title
     * @param lines  additional lines
     */
    public InventoryButton(Material icon, int number, String title, String... lines) {
        itemStack = new ItemStack(icon);
        itemStack.setAmount(number);
        ItemMeta meta = getRawMeta();
        meta.setDisplayName(title);
        if (lines != null && lines.length != 0) {
            meta.setLore(Arrays.asList(lines));
        }
        setRawMeta(meta);
    }

    /**
     * @param icon  the icon
     * @param title the title
     * @param lines additional lines
     */
    public InventoryButton(Material icon, String title, String... lines) {
        this(icon, 1, title, lines);
    }

    /**
     * Creates a simple new InventoryButton. Uses paper as the default icon.
     *
     * @param title the title
     */
    public InventoryButton(String title) {
        this(Material.PAPER, title);
    }

    private InventoryButton(InventoryButton button) {
        itemStack = button.itemStack.clone();
        sound = button.sound;
        leftClickLocked = button.leftClickLocked;
        rightClickLocked = button.rightClickLocked;
        interactionListener = button.interactionListener;
        contextModifiers = button.contextModifiers;
    }

    /**
     * Returns the icon of this button.
     *
     * @return the icon
     */
    public Material getIcon() {
        return itemStack.getType();
    }

    /**
     * Sets the icon of this button.
     *
     * @param icon the icon to set
     */
    public void setIcon(Material icon) {
        itemStack.setType(icon);
    }

    /**
     * Returns the number attached to this button or 0 if there is none.
     *
     * @return the number attached to this button
     */
    public int getNumber() {
        return itemStack.getAmount();
    }

    /**
     * Sets the attached number to a value.
     * <p>
     * 1 results in no number being used.
     * <p>
     * The behavior for 0 and negative values is undefined. In earlier Minecraft versions, they work and are displayed in red.
     *
     * @param number the number to set
     */
    public void setNumber(int number) {
        itemStack.setAmount(number);
    }

    @Override
    public String getTitle() {
        ItemMeta meta = getRawMeta();
        return meta != null ? meta.getDisplayName() : null;
    }

    @Override
    public void setTitle(String text) {
        ItemMeta meta = getRawMeta();
        meta.setDisplayName(text);
        setRawMeta(meta);
    }

    /**
     * Returns additional lines.
     *
     * @return additional lines
     */
    public List<String> getLines() {
        return itemStack.getItemMeta().getLore();
    }

    /**
     * Sets additional lines to the button.
     *
     * @param lines the additional lines to set
     */
    public void setLines(List<String> lines) {
        ItemMeta meta = getRawMeta();
        meta.setLore(lines);
        setRawMeta(meta);
    }

    /**
     * Adds additional lines to the button.
     *
     * @param lines the additional lines to add
     */
    public void addLines(String... lines) {
        ItemMeta meta = getRawMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(lines));
        meta.setLore(lore);
        setRawMeta(meta);
    }

    /**
     * Removes additional lines from the button.
     *
     * @param lines the additional lines to remove
     */
    public void removeLines(String... lines) {
        ItemMeta meta = getRawMeta();
        if (!meta.hasLore()) {
            return;
        }
        List<String> lore = meta.getLore();
        lore.removeAll(Arrays.asList(lines));
        meta.setLore(lore);
        setRawMeta(meta);
    }

    @Override
    public String getSound() {
        return sound;
    }

    @Override
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Returns a copy of the raw {@link org.bukkit.inventory.meta.ItemMeta} of the underlying {@link org.bukkit.inventory.ItemStack}.
     *
     * @return a copy of the raw {@link org.bukkit.inventory.meta.ItemMeta} of the underlying {@link org.bukkit.inventory.ItemStack}
     */
    public ItemMeta getRawMeta() {
        return itemStack.getItemMeta();
    }

    /**
     * Sets raw {@link org.bukkit.inventory.meta.ItemMeta} to the underlying {@link org.bukkit.inventory.ItemStack}.
     *
     * @param meta the {@link org.bukkit.inventory.meta.ItemMeta} to set
     */
    public void setRawMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
    }

    /**
     * Returns if the button may be taken from the GUI.
     * <p>
     * This is equal to !({@link #isLeftClickLocked()} && {@link #isRightClickLocked()}).
     *
     * @return if the button may be taken from the GUI
     */
    public boolean isStealable() {
        return !(leftClickLocked && rightClickLocked);
    }

    /**
     * Sets if the button may be taken from the GUI.
     * <p>
     * This sets {@link #isLeftClickLocked()} and {@link #isRightClickLocked()} to negated the parameter.
     *
     * @param stealable if the button may be taken from the GUI
     */
    public void setStealable(boolean stealable) {
        leftClickLocked = !stealable;
        rightClickLocked = !stealable;
    }

    /**
     * Returns if the button cannot be picked up with a left click.
     *
     * @return if the button cannot be picked up with a left click
     */
    public boolean isLeftClickLocked() {
        return leftClickLocked;
    }

    /**
     * Sets if the button cannot be picked up with a left click.
     *
     * @param locked if the button cannot be picked up with a left click
     */
    public void setLeftClickLocked(boolean locked) {
        leftClickLocked = locked;
    }

    /**
     * Returns if the button cannot be picked up with a right click.
     *
     * @return if the button cannot be picked up with a right click
     */
    public boolean isRightClickLocked() {
        return rightClickLocked;
    }

    /**
     * Sets if the button cannot be picked up with a right click.
     *
     * @param locked if the button cannot be picked up with a right click
     */
    public void setRightClickLocked(boolean locked) {
        rightClickLocked = locked;
    }

    @Override
    public InteractionListener getInteractionListener() {
        return interactionListener;
    }

    @Override
    public void setInteractionListener(InteractionListener listener) {
        interactionListener = listener;
    }

    @Override
    public List<ContextModifier<InventoryButton>> getContextModifiers() {
        return contextModifiers;
    }

    @Override
    public void addContextModifier(ContextModifier<InventoryButton> ctxt) {
        contextModifiers.add(ctxt);
    }

    @Override
    public void removeContextModifier(ContextModifier<InventoryButton> ctxt) {
        contextModifiers.remove(ctxt);
    }

    @Override
    public void setContextModifiers(List<ContextModifier<InventoryButton>> ctxts) {
        if (ctxts == null) {
            contextModifiers.clear();
        } else {
            contextModifiers = ctxts;
        }
    }

    @Override
    public boolean hasStatusModifier(StatusModifier<?> status) {
        return statusModifiers.contains(status);
    }

    @Override
    public Set<StatusModifier<?>> getStatusModifiers() {
        return statusModifiers;
    }

    @Override
    public void addStatusModifier(StatusModifier<?> status) {
        statusModifiers.add(status);
    }

    @Override
    public void removeStatusModifier(StatusModifier<?> status) {
        statusModifiers.remove(status);
    }

    @Override
    public void setStatusModifiers(Set<StatusModifier<?>> status) {
        if (status == null) {
            statusModifiers.clear();
        } else {
            statusModifiers = status;
        }
    }

    /**
     * Returns a raw {@link org.bukkit.inventory.ItemStack} to use in an inventory GUI.
     * <p>
     * Modifying the returned stack does not modify the button itself.
     *
     * @return a raw {@link org.bukkit.inventory.ItemStack} to use in an inventory GUI
     */
    public ItemStack createItemStack() {
        return itemStack.clone();
    }

    /**
     * Checks if the button is a representation of an {@link org.bukkit.inventory.ItemStack}.
     *
     * @param rawButton an ItemStack in an inventory GUI
     * @return if the ItemStack is a representation of this button
     */
    public boolean is(ItemStack rawButton) {
        return itemStack.equals(rawButton);
    }

    /**
     * Checks if the button is a representation of an {@link org.bukkit.inventory.ItemStack}.
     * <p>
     * If the button has one or more {@link de.erethon.vignette.api.context.ContextModifier}s and if it is not a representation of the ItemStack
     * in the button's form not modified by its ContextModifiers, a copy of the button is made and the modifiers are applied to this copy for reference
     * to check if the modified form is a representation of the ItemStack.
     *
     * @param rawButton     an ItemStack in an inventory GUI
     * @param contextPlayer the Player
     * @return if the ItemStack is a representation of this button
     */
    public boolean is(ItemStack rawButton, Player contextPlayer) {
        boolean is = is(rawButton);
        if (is) {
            return true;

        } else if (!getContextModifiers().isEmpty()) {
            InventoryButton modified = copy();
            modified.applyAllContextModifiers(contextPlayer);
            return modified.is(rawButton);

        } else {
            return false;
        }
    }

    @Override
    public InventoryButton copy() {
        return new InventoryButton(this);
    }

}
