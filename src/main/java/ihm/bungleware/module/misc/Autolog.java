package ihm.bungleware.module.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.LiteralText;

import ihm.bungleware.Bungleware;
import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Autolog extends Module {
    private IntSetting minHealth =
        new IntSetting(
            "Health", "Log if your health is below a value", 15, 0, 20);

    public Autolog() {
        super("Autolog", "Disconnect when conditions are met");
        addSetting(minHealth);
        addDefaultBind();
    }

    @Override
    public void onTick() {
        String logMessage = shouldLog();
        if (logMessage == null)
            return;

        setEnabled(false);
        Bungleware.instance().save();
        System.out.println("[Bungleware] Disconnected: " + logMessage);
        MinecraftClient.getInstance().getNetworkHandler().onDisconnect(
            new DisconnectS2CPacket(new LiteralText(logMessage)));
    }

    private String shouldLog() {
        var mc = MinecraftClient.getInstance();
        if (mc.player.getHealth() < minHealth.getVal())
            return "Health (" + (int)mc.player.getHealth() +
                ") < " + minHealth.getVal();
        return null;
    }
}
