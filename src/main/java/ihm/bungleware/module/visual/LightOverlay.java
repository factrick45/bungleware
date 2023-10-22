package ihm.bungleware.module.visual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.AirBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import static net.minecraft.client.render.VertexFormats.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import ihm.bungleware.event.EventHandler;
import ihm.bungleware.event.RenderListener;
import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;
import ihm.bungleware.utils.MathUtils;

import com.mojang.blaze3d.platform.GlStateManager;
import static org.lwjgl.opengl.GL11.*;

public class LightOverlay extends Module implements RenderListener {
    private static class BlockLight {
        public final BlockPos pos;
        public final float[] color;

        public BlockLight(BlockPos pos, float[] color) {
            this.pos = pos;
            this.color = color;
        }
    }

    private int counter = 0;
    private BlockLight[] blocks = null;

    private IntSetting updateDelay = new IntSetting(
        "Delay", "Time in ticks between each update", 5, 1, 20);
    private IntSetting radius = new IntSetting(
        "Radius", "Radius around the player to draw", 15, 1, 30);

    public LightOverlay() {
        super("Light Overlay", "Display the light levels of adjacent blocks");
        addSettings(updateDelay, radius);
        addDefaultBind();
    }

    @Override
    public void onDisabled() {
        EventHandler.unregister(this);
    }

    @Override
    public void onEnabled() {
        EventHandler.register(this);
    }

    @Override
    public void onTick() {
        counter++;
        if (counter <= updateDelay.getVal())
            return;
        counter = 0;

        int r = radius.getVal();
        BlockPos ppos = MinecraftClient.getInstance().player.getBlockPos();
        World world = MinecraftClient.getInstance().world;

        List<BlockLight> nblocks = new ArrayList<>();

        Iterable<BlockPos> iterator =
            BlockPos.iterate(ppos.add(-r, -r, -r), ppos.add(r, r, r));
        for (BlockPos pos : iterator) {
            // position to scan for light
            Block block = world.getBlockState(pos).getBlock();
            BlockPos lightpos = pos.add(0, 1, 0);
            if (block instanceof AirBlock || !block.isFullCube())
                continue;
            // can be culled
            if (!(world.getBlockState(lightpos).getBlock() instanceof AirBlock)) {
                continue;
            }
            // mobs will always spawn here
            if (world.getLightLevel(lightpos) < 7) {
                nblocks.add(new BlockLight(pos, new float[]{1.0f, 0.0f, 0.0f}));
                continue;
            }
            // mobs will spawn here at night
            if (world.getLightAtPos(LightType.BLOCK, lightpos) < 7) {
                nblocks.add(new BlockLight(pos, new float[]{1.0f, 1.0f, 0.0f}));
                continue;
            }
        }
        blocks = nblocks.toArray(new BlockLight[0]);
    }

    @Override
    public void onRenderWorldPost(float tickDelta) {
        if (blocks == null)
            return;

        Vec3d ppos =
            MathUtils.getLerpPos(MinecraftClient.getInstance().player, tickDelta);

        GlStateManager.disableTexture();

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        bb.begin(GL_LINES, POSITION_COLOR);

        for (BlockLight bl : blocks) {
            Vec3d pos = new Vec3d(bl.pos).subtract(ppos);
            bb.vertex(pos.x, pos.y + 1.01, pos.z);
            bb.color(bl.color[0], bl.color[1], bl.color[2], 1.0f);
            bb.next();
            bb.vertex(pos.x + 1, pos.y + 1.01, pos.z + 1);
            bb.color(bl.color[0], bl.color[1], bl.color[2], 1.0f);
            bb.next();
            bb.vertex(pos.x, pos.y + 1.01, pos.z + 1);
            bb.color(bl.color[0], bl.color[1], bl.color[2], 1.0f);
            bb.next();
            bb.vertex(pos.x + 1, pos.y + 1.01, pos.z);
            bb.color(bl.color[0], bl.color[1], bl.color[2], 1.0f);
            bb.next();
        }

        tes.draw();

        GlStateManager.enableTexture();
    }
}
