package ihm.bungleware.mixin;

import net.minecraft.client.MinecraftClient;

import ihm.bungleware.Bungleware;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /** Fired as Minecraft is polling keyboard input with Keyboard.next(). */
    @Inject(at = @At("HEAD"), method = "handleKeyInput")
    public void onKey(CallbackInfo ci) {
        int key = Keyboard.getEventKey() == 0 ?
            Keyboard.getEventCharacter() : Keyboard.getEventKey();
        Bungleware.instance().onKey(key, Keyboard.getEventKeyState());
    }

    /** Fired after Minecraft is confirmed to have a window and rendering context. */
    @Inject(at = @At("TAIL"), method = "initializeGame")
    public void onInitializePost(CallbackInfo ci) {
        Bungleware.instance().onInitializePost();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void onTick(CallbackInfo ci) {
        Bungleware.instance().onTick();
    }

    /**
     * fired only outside of a screen, (Screen handles it otherwise, see
     * ScreenMixin), as Minecraft is polling mouse input with Mouse.next().
     */
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(IZ)V",
            ordinal = 0
        ),
        method = "tick"
    )
    public void onMouse(CallbackInfo ci) {
        if (Mouse.getEventDWheel() != 0)
            Bungleware.instance().onMouseWheel(Mouse.getEventDWheel());
        // no button is being pressed
        if (Mouse.getEventButton() == -1)
            return;
        Bungleware.instance().onMouseButton(
            Mouse.getEventButton(), Mouse.getEventButtonState()
        );
    }

    /**
     * updateDisplay is called directly before rendering is finished and the
     * displays buffers are swapped. Nothing is being drawn at this point, but
     * some OpenGL state can persist to the next frame and cause issues.
     */
    @Inject(at = @At("HEAD"), method = "updateDisplay")
    public void onRenderPost(CallbackInfo ci) {
        //MinecraftClient mc = (MinecraftClient)(Object)this;
        //mc.profiler.push("bungleware");
        Bungleware.instance().onRenderPost();
        //mc.profiler.pop();
    }
}
