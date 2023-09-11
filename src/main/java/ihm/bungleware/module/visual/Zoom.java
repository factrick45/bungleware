package ihm.bungleware.module.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Zoom extends Module {
    private FloatSetting scale =
        new FloatSetting("Scale", "FOV scale", 2.0f, 0.1f, 10.0f);

    private float oldFov;
    private float oldSensitivity;

    public Zoom() {
        super("Zoom", "Zoomer lamao");
        addSetting(scale);
        addDefaultBind();
    }

    @Override
    public void onEnabled() {
        GameOptions options = MinecraftClient.getInstance().options;
        oldFov = options.fov;
        oldSensitivity = options.sensitivity;

        options.fov /= scale.getVal();
        options.sensitivity /= scale.getVal();
    }

    @Override
    public void onDisabled() {
        GameOptions options = MinecraftClient.getInstance().options;
        options.fov = oldFov;
        options.sensitivity = oldSensitivity;
    }
}
