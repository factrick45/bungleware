package ihm.bungleware.mixin;

import net.minecraft.client.gui.screen.Screen;

import ihm.bungleware.Bungleware;

import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    /**
     * Fired as Minecraft is polling mouse input when a screen is open.
     * See also onMouse in MinecraftClientMixin.
     */
    @Inject(at = @At("HEAD"), method = "handleMouse")
    public void onMouse(CallbackInfo ci) {
        if (Mouse.getEventDWheel() != 0)
            Bungleware.instance().onMouseWheel(Mouse.getEventDWheel());
        if (Mouse.getEventButton() == -1)
            return;
        Bungleware.instance().onMouseButton(
            Mouse.getEventButton(), Mouse.getEventButtonState()
        );
    }
}
