package cc.silk.gui;

import cc.silk.gui.newgui.NewClickGUI;
import cc.silk.utils.render.nanovg.NanoVGContext;
import net.minecraft.client.gui.screen.Screen;

public final class ClickGuiFactory {
    private ClickGuiFactory() {
    }

    public static Screen create() {
        return NanoVGContext.init() ? new NewClickGUI() : new ClickGui();
    }
}
