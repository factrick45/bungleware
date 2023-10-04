package ihm.bungleware.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;

import ihm.bungleware.event.EventHandler;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(
        at = @At("HEAD"),
        cancellable = true,
        method = "send(Lnet/minecraft/network/Packet;)V"
    )
    private void onSendPacket(
        Packet packet,
        CallbackInfo ci
    ) {
        boolean[] cancel = {false};
        EventHandler.onSendPacket(packet, cancel);
        if (cancel[0])
            ci.cancel();
    }

    @Inject(
        at = @At("HEAD"),
        cancellable = true,
        method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V"
    )
    private void onSendPacket(
        Packet packet,
        GenericFutureListener<? extends Future<? super Void>> genericFutureListener,
        GenericFutureListener<? extends Future<? super Void>>[] genericFutureListener2,
        CallbackInfo ci
    ) {
        boolean[] cancel = {false};
        EventHandler.onSendPacket(packet, cancel);
        if (cancel[0])
            ci.cancel();
    }
}
