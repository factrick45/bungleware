package ihm.bungleware.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.BlockView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import ihm.bungleware.Bungleware;
import ihm.bungleware.module.Modules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At("HEAD"), method = "isSideInvisible", cancellable = true)
    public void xrayCull(
        BlockView view, BlockPos pos, Direction facing,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!Modules.getModule("Xray").isEnabled())
            return;
        var block = view.getBlockState(pos).getBlock();
        cir.setReturnValue(false);
    }

    /*
    @Inject(at = @At("HEAD"), method = "getLightLevel", cancellable = true)
    public void xrayLight(CallbackInfoReturnable<Integer> cir) {
        if (Modules.getModule("Xray").isEnabled())
            cir.setReturnValue(15);
    }
    */
}
