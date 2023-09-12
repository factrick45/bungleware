package ihm.bungleware.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImageBackedTexture;

import ihm.bungleware.module.Modules;
import ihm.bungleware.module.visual.Esp;
import ihm.bungleware.module.visual.Nightvision;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
            opcode = Opcodes.GETFIELD, ordinal = 0
        ),
        method = "Lnet/minecraft/client/render/GameRenderer;renderWorld(IFJ)V"
    )
    public void onRenderWorldPost(
        int anaglyphFilter, float tickDelta, long limitTime, CallbackInfo ci
    ) {
        Esp esp = (Esp)Modules.getModule("ESP");
        if (esp.isEnabled())
            esp.onRenderWorldPost(tickDelta);
    }

    @Shadow
    private NativeImageBackedTexture lightmapTexture;
    @Shadow
    private boolean lightmapDirty;
    @Shadow
    private int[] lightmapTexturePixels;

    @Inject(at = @At("HEAD"), method = "updateLightmap", cancellable = true)
    public void setAllLights(float tickDelta, CallbackInfo ci) {
        Nightvision nv = (Nightvision)Modules.getModule("Nightvision");
        if (!nv.isEnabled())
            return;
        if (!lightmapDirty)
            return;
        nv.setAllLights(lightmapTexturePixels);
        lightmapTexture.upload();
        lightmapDirty = false;
        ci.cancel();
    }
}
