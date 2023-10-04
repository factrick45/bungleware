package ihm.bungleware.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Flight extends Module {
    private FloatSetting speed =
        new FloatSetting("Speed", "Speed multiplier", 2.0f, 0.0f, 10.0f);

    public Flight() {
        super("Flight", "Fly in the air");
        addSetting(speed);
        addDefaultBind();
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();

        float vely = 0.0f;
        if (mc.options.jumpKey.isPressed())
            vely += 1.0f;
        if (mc.options.sneakKey.isPressed())
            vely -= 1.0f;

        Vec3d mov =
            new Vec3d(mc.player.sidewaysSpeed, vely, mc.player.forwardSpeed);
        mov = mov.rotateY(-mc.player.yaw * ((float)Math.PI / 180.0f));

        mc.player.velocityX = mov.x * speed.getVal() * 0.2f;
        mc.player.velocityY = mov.y * speed.getVal() * 0.2f;
        mc.player.velocityZ = mov.z * speed.getVal() * 0.2f;
    }
}
