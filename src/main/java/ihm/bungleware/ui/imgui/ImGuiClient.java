package ihm.bungleware.ui.imgui;

/** Can be registered to ImGuiLoader. */
public interface ImGuiClient {
	boolean guiEnabled();
	void renderGui();
}
