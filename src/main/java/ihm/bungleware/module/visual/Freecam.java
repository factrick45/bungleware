package ihm.bungleware.module.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import ihm.bungleware.event.EventHandler;
import ihm.bungleware.event.PacketListener;
import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Freecam extends Module implements PacketListener {
    private Vec3d prevPos;

    private FloatSetting speed =
        new FloatSetting("Speed", "Speed multiplier", 1.0f, 0.0f, 10.0f);

    public Freecam() {
        super("Freecam", "Classic out of body experience!");
        addSetting(speed);
        addDefaultBind();
    }

    @Override
    public void onDisabled() {
        MinecraftClient.getInstance().chunkCullingEnabled = true;
        Entity p = MinecraftClient.getInstance().player;
        p.updatePosition(prevPos.x, prevPos.y, prevPos.z);

        EventHandler.unregister(this);
    }

    @Override
    public void onEnabled() {
        MinecraftClient.getInstance().chunkCullingEnabled = false;
        Entity p = MinecraftClient.getInstance().player;
        prevPos = new Vec3d(p.x, p.y, p.z);
        EventHandler.register(this);
    }

    @Override
    public void onSendPacket(Packet packet, boolean[] cancel) {
        if (packet instanceof PlayerMoveC2SPacket)
            cancel[0] = true;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.player.noClip = true;
        mc.player.onGround = true;

        float vely = 0.0f;
        if (mc.options.jumpKey.isPressed())
            vely += 1.0f;
        if (mc.options.sneakKey.isPressed())
            vely -= 1.0f;

        Vec3d mov =
            new Vec3d(mc.player.sidewaysSpeed, vely, mc.player.forwardSpeed);
        mov = mov.rotateY(-mc.player.yaw * ((float)Math.PI / 180.0f));

        mc.player.updatePosition(
            mc.player.x + mov.x * speed.getVal(),
            mc.player.y + mov.y * speed.getVal(),
            mc.player.z + mov.z * speed.getVal());

        mc.player.velocityX = 0.0;
        mc.player.velocityY = 0.0;
        mc.player.velocityZ = 0.0;
    }
}
