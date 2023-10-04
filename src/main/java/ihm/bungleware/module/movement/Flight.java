package ihm.bungleware.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Flight extends Module {
    public Flight() {
        super("Flight", "Fly in the air.");
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
        mc.player.velocityY = vely * 0.2f;
    }
}
