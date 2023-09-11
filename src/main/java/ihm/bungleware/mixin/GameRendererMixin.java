package ihm.bungleware.mixin;

import net.minecraft.client.render.GameRenderer;

import ihm.bungleware.module.Modules;
import ihm.bungleware.module.visual.Esp;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
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
        var esp = Modules.getModule("ESP");
        if (esp.isEnabled())
            ((Esp)esp).onRenderWorldPost(tickDelta);
    }
}
