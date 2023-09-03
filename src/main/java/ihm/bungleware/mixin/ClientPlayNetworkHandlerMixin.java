package ihm.bungleware.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import ihm.bungleware.Bungleware;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    // refer to Module
    /*
    @Inject(at = @At("HEAD"), method = "onDisconnect")
    public void onGameLeave(CallbackInfo ci) {
        Bungleware.INSTANCE.onGameLeave();
    }
    */

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    public void onGameJoin(CallbackInfo ci) {
        Bungleware.INSTANCE.onGameJoin();
    }
}
