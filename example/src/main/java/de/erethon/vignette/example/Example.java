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
package de.erethon.vignette.example;

import de.erethon.vignette.api.GUI;
import de.erethon.vignette.api.InventoryGUI;
import de.erethon.vignette.api.PaginatedInventoryGUI;
import de.erethon.vignette.api.VignetteAPI;
import de.erethon.vignette.api.component.InventoryButton;
import de.erethon.vignette.api.component.InventoryButtonBuilder;
import de.erethon.vignette.api.layout.CenteredInventoryLayout;
import de.erethon.vignette.api.layout.FlowInventoryLayout;
import de.erethon.vignette.api.layout.PaginatedFlowInventoryLayout;
import de.erethon.vignette.api.pagination.PaginatedInventoryLayout.PaginationButtonPosition;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class Example extends JavaPlugin {

    public static final InventoryGUI[] TEST = new InventoryGUI[]{
        new InventoryGUI("Title"),
        new PaginatedInventoryGUI(ChatColor.DARK_RED + "Dark Red Title"),
        new InventoryGUI(ChatColor.DARK_RED + "Centered GUI Test"),
        new InventoryGUI(ChatColor.GOLD + "Overloaded centered GUI Test"),
        new InventoryGUI(ChatColor.BLUE + "Context Modifier Test")
    };

    static {
        TEST[0].setLayout(new FlowInventoryLayout(TEST[0], 9));
        TEST[0].add(new InventoryButton("Test"));
        TEST[0].add(new InventoryButtonBuilder()
                .title(ChatColor.DARK_GREEN + "Test")
                .lines("Lore1", "Lore2")
                .icon(Material.RED_MUSHROOM)
                .sound("ui.button.click")
                .onInteract(e -> e.getPlayer().sendMessage(e.getAction().toString()))
                .build()
        );
        TEST[1].setLayout(new PaginatedFlowInventoryLayout((PaginatedInventoryGUI) TEST[1], 9, PaginationButtonPosition.CENTER));
        TEST[1].add(new InventoryButton(Material.MINECART, ChatColor.GOLD + "This is a button", ChatColor.GOLD + "with multiple", ChatColor.GOLD + "lines of text"));
        TEST[2].setLayout(new CenteredInventoryLayout(TEST[2], 18));
        TEST[2].add(new InventoryButton("0-0"));
        TEST[2].add(new InventoryButton("0-1"));
        TEST[2].add(new InventoryButton("0-2"));
        TEST[2].add(new InventoryButton("0-3"));
        TEST[2].add(new InventoryButton("0-4"));
        TEST[2].add(new InventoryButton("0-5"));
        TEST[2].add(new InventoryButton("0-6"));
        TEST[2].add(new InventoryButton("0-7"));
        TEST[2].add(new InventoryButton("0-8"));
        TEST[2].add(new InventoryButton("1-0"));
        TEST[2].add(new InventoryButton("1-1"));
        TEST[2].add(new InventoryButton("1-2"));
        TEST[2].add(new InventoryButton("1-3"));
        TEST[3].setLayout(new CenteredInventoryLayout(TEST[3], 9));
        for (int i = 0; i <= 12; i++) {
            TEST[3].add(new InventoryButton("Test"));
        }
        TEST[4].setLayout(new FlowInventoryLayout(TEST[4], 9));
        TEST[4].addContextModifier((t, p) -> t.setTitle("You are" + (p.isOp() ? " " : " NOT ") + "OP"));
        TEST[4].add(new InventoryButtonBuilder()
                .contextModifier((t, p) -> t.setTitle("You are" + (p.isOp() ? " " : " NOT ") + "OP"))
                .build()
        );
    }

    @Override
    public void onEnable() {
        VignetteAPI.init(this);
        for (GUI gui : TEST) {
            gui.register();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Run as a player!");
            return true;
        }
        GUI gui = TEST[0];
        if (args.length > 0) {
            int i = 0;
            try {
                i = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
            }
            if (i < TEST.length && i >= 0) {
                gui = TEST[i];
            }
        }
        gui.open((Player) sender);
        return true;
    }

}
