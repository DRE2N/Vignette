# Vignette
A GUI Framework for Bukkit.

    InventoryGUI gui = new InventoryGUI(ChatColor.DARK_RED + "My custom GUI");
    gui.setLayout(new CenteredInventoryLayout(gui, 18));
    gui.add(new InventoryButtonBuilder()
            .lines(ChatColor.BLUE + "Some", ChatColor.GOLD + "more", ChatColor.RED + "text lines")
            .icon(Material.DIAMOND_BLOCK)
            .sound("ui.button.click")
            .onInteract(e -> e.getPlayer().sendMessage("You have clicked a button."))
            .contextModifier((t, p) -> t.setTitle(PlaceholderAPI.setPlaceholders(p, "%my_placeholder%")))
            .build()
    );
	gui.register();

### Features
* Built-in layouts to format menus with ease
* GUI component builders for simple and clean code
* Lambda-based interaction event listeners that are added directly to the GUI buttons
* GUI contents can depend dynamically on the player viewing them and the environment through the lambda-based ContextModifier API
* Automatic pagination
* Modular setup that allows to easily include only needed parts of the project
* Public domain: Vignette may be used in any project, including proprietary ones
