package ihm.bungleware.module.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;
import ihm.bungleware.utils.GuiUtils;

public class Nightvision extends Module {
    private ColorSetting color =
        new ColorSetting("Color", "Color to override lights with", 0xffffffff);
    private BoolSetting rainbow =
        new BoolSetting("Rainbow", "Use a rainbow instead", false);

    public Nightvision() {
        super("Nightvision", "Override world lighting");
        addSettings(color, rainbow);
        addDefaultBind();
    }

    public void setAllLights(int[] lights) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.profiler.push("lightTex");
        if (mc.world == null) {
            mc.profiler.pop();
            return;
        }

        for (int i = 0; i < 256; i++)
            lights[i] = rainbow.getVal() ?
                GuiUtils.rainbow(i, 0.1f) : color.getVal();

        mc.profiler.pop();
    }
}
