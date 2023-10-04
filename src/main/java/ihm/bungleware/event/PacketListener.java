package ihm.bungleware.event;

import net.minecraft.network.Packet;

public interface PacketListener {
    default public void onSendPacket(Packet packet, boolean[] cancel) {}
}
