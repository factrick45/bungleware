package ihm.bungleware.event;

public interface RenderListener {
    default public void onRenderWorldPost(float tickDelta) {}
}
