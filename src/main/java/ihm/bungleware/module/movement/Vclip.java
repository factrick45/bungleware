package ihm.bungleware.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Vclip extends Module {
    private FloatSetting distance =
        new FloatSetting(
            "Distance", "Distance to teleport.", 3.0f, -10.0f, 10.0f);

    public Vclip() {
        super("Vclip", "Teleport vertically through floors.");
        addSetting(distance);
        addDefaultBind();
    }

    @Override
    public void onEnabled() {
        setEnabled(false);
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity p = mc.player;

        Packet packet = new PlayerMoveC2SPacket.PositionOnly(
            p.x, p.y + distance.getVal(), p.z, true);
        mc.getNetworkHandler().sendPacket(packet);
        p.updatePosition(p.x, p.y + distance.getVal(), p.z);
    }
}
