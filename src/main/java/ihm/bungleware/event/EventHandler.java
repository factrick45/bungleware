package ihm.bungleware.event;

import java.util.List;
import java.util.ArrayList;

/** Handles dispatching of events. */
public class EventHandler {
    private static List<RenderListener> renderListeners = new ArrayList<>();

    public static void register(Object listener) {
        if (listener instanceof RenderListener &&
            !renderListeners.contains(listener))
            renderListeners.add((RenderListener)listener);
    }

    public static void unregister(Object listener) {
        if (listener instanceof RenderListener)
            renderListeners.remove(listener);
    }

    public static void onRenderWorldPost(float tickDelta) {
        for (RenderListener listener : renderListeners)
            listener.onRenderWorldPost(tickDelta);
    }
}
