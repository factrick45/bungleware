package ihm.bungleware.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Nofall extends Module {
    public Nofall() {
        super("Nofall", "Prevent fall damage");
        addDefaultBind();
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player.fallDistance > 2)
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket(true));
    }
}
