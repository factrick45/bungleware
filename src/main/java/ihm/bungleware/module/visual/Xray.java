package ihm.bungleware.module.visual;

import net.minecraft.client.MinecraftClient;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Xray extends Module {
    public Xray() {
        super("Xray", "Make walls translucent.");
        addDefaultBind();
    }

    @Override
    public void onEnabled() {
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    @Override
    public void onDisabled() {
        MinecraftClient.getInstance().worldRenderer.reload();
    }
}
