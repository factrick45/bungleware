package ihm.bungleware.event;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.network.Packet;

/** Handles dispatching of events. */
public class EventHandler {
    private static List<PacketListener> packetListeners = new ArrayList<>();
    private static List<RenderListener> renderListeners = new ArrayList<>();

    public static void register(Object listener) {
        if (listener instanceof PacketListener &&
            !packetListeners.contains(listener))
            packetListeners.add((PacketListener)listener);
        if (listener instanceof RenderListener &&
            !renderListeners.contains(listener))
            renderListeners.add((RenderListener)listener);
    }

    public static void unregister(Object listener) {
        if (listener instanceof PacketListener)
            packetListeners.remove(listener);
        if (listener instanceof RenderListener)
            renderListeners.remove(listener);
    }

    public static void onRenderWorldPost(float tickDelta) {
        for (RenderListener listener : renderListeners)
            listener.onRenderWorldPost(tickDelta);
    }

    public static void onSendPacket(Packet packet, boolean[] cancel) {
        for (PacketListener listener : packetListeners)
            listener.onSendPacket(packet, cancel);
    }
}
