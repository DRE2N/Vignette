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
package de.erethon.vignette.api;

import de.erethon.vignette.api.context.ContextModifier;
import de.erethon.vignette.api.layout.Layout;
import de.erethon.vignette.util.PlayerCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * Provides a skeletal implementation of {@link GUI}.
 *
 * @param <T> the type itself
 * @author Daniel Saukel
 */
public abstract class AbstractGUI<T extends AbstractGUI<T>> implements GUI<T> {

    private String title;
    private Layout<T> layout;
    private List<ContextModifier<T>> contextModifiers = new ArrayList<>();
    private boolean isTransient;

    protected PlayerCollection viewers = new PlayerCollection();

    protected AbstractGUI() {
        this("");
    }

    protected AbstractGUI(String title) {
        this.title = title;
    }

    protected AbstractGUI(AbstractGUI gui) {
        this(gui.title);
        layout = gui.layout.copy(this);
        contextModifiers = gui.contextModifiers;
        isTransient = gui.isTransient;
        if (gui.isRegistered()) {
            register();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String text) {
        title = text;
    }

    @Override
    public Layout<T> getLayout() {
        return layout;
    }

    @Override
    public void setLayout(Layout<T> layout) {
        this.layout = layout;
    }

    @Override
    public List<ContextModifier<T>> getContextModifiers() {
        return contextModifiers;
    }

    @Override
    public void addContextModifier(ContextModifier<T> ctxt) {
        contextModifiers.add(ctxt);
    }

    @Override
    public void removeContextModifier(ContextModifier<T> ctxt) {
        contextModifiers.remove(ctxt);
    }

    @Override
    public void setContextModifiers(List<ContextModifier<T>> ctxts) {
        if (ctxts == null) {
            contextModifiers.clear();
        } else {
            contextModifiers = ctxts;
        }
    }

    @Override
    public Collection<Player> getViewers() {
        return viewers.getOnlinePlayers();
    }

    @Override
    public boolean isTransient() {
        return isTransient;
    }

    @Override
    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

}
