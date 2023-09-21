package ihm.bungleware.module.visual;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import static net.minecraft.client.render.VertexFormats.*;
import net.minecraft.util.math.Vec3d;

import ihm.bungleware.event.EventHandler;
import ihm.bungleware.event.RenderListener;
import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;
import ihm.bungleware.utils.MathUtils;

import static org.lwjgl.opengl.GL11.*;

public class Chunks extends Module implements RenderListener {
    private BoolSetting noDepth =
        new BoolSetting("No Depth", "Draw over terrain", false);

    public Chunks() {
        super("Chunks", "Visualize chunk borders");
        addSetting(noDepth);
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

    private void lineColor(BufferBuilder bb, int x) {
        if (x == 0 || x % 16 == 0) {
            bb.color(0.2f, 0.2f, 1.0f, 1.0f);
            return;
        }
        if (x % 4 == 0) {
            bb.color(0.0f, 0.6f, 0.6f, 1.0f);
            return;
        }
        bb.color(1.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void onRenderWorldPost(float tickDelta) {
        if (noDepth.getVal())
            GlStateManager.disableDepthTest();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();

        MinecraftClient mc = MinecraftClient.getInstance();
        Vec3d pos = MathUtils.getLerpPos(mc.player, tickDelta);
        Vec3d lpos = new Vec3d(
            -(pos.x % 16) - (pos.x > 0.0 ? 0.0 : 16.0),
            -pos.y,
            -(pos.z % 16) - (pos.z > 0.0 ? 0.0 : 16.0)
        );

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        bb.offset(lpos.x, lpos.y, lpos.z);
        bb.begin(GL_LINES, POSITION_COLOR);

        // outer chunks
        for (int i = -16; i < 32; i += 16) {
            bb.vertex(i, 0, -16).color(1.0f, 0.0f, 0.0f, 1.0f).next();
            bb.vertex(i, 256, -16).color(1.0f, 0.0f, 0.0f, 1.0f).next();

            bb.vertex(i, 0, 32).color(1.0f, 0.0f, 0.0f, 1.0f).next();
            bb.vertex(i, 256, 32).color(1.0f, 0.0f, 0.0f, 1.0f).next();

            bb.vertex(-16, 0, i).color(1.0f, 0.0f, 0.0f, 1.0f).next();
            bb.vertex(-16, 256, i).color(1.0f, 0.0f, 0.0f, 1.0f).next();

            bb.vertex(32, 0, i).color(1.0f, 0.0f, 0.0f, 1.0f).next();
            bb.vertex(32, 256, i).color(1.0f, 0.0f, 0.0f, 1.0f).next();
        }
        bb.vertex(32, 0, 32).color(1.0f, 0.0f, 0.0f, 1.0f).next();
        bb.vertex(32, 256, 32).color(1.0f, 0.0f, 0.0f, 1.0f).next();

        // vertical lines
        for (int i = 0; i < 16; i += 2) {
            bb.vertex(i, 0, 0);
            lineColor(bb, i);
            bb.next();
            bb.vertex(i, 256, 0);
            lineColor(bb, i);
            bb.next();

            bb.vertex(0, 0, i);
            lineColor(bb, i);
            bb.next();
            bb.vertex(0, 256, i);
            lineColor(bb, i);
            bb.next();

            bb.vertex(i, 0, 16);
            lineColor(bb, i);
            bb.next();
            bb.vertex(i, 256, 16);
            lineColor(bb, i);
            bb.next();

            bb.vertex(16, 0, i);
            lineColor(bb, i);
            bb.next();
            bb.vertex(16, 256, i);
            lineColor(bb, i);
            bb.next();
        }
        bb.vertex(16, 0, 16);
        lineColor(bb, 16);
        bb.next();
        bb.vertex(16, 256, 16);
        lineColor(bb, 16);
        bb.next();

        // horizontal lines
        for (int i = 0; i <= 256; i += 2) {
            bb.vertex(0, i, 0);
            lineColor(bb, i);
            bb.next();
            bb.vertex(16, i, 0);
            lineColor(bb, i);
            bb.next();

            bb.vertex(0, i, 0);
            lineColor(bb, i);
            bb.next();
            bb.vertex(0, i, 16);
            lineColor(bb, i);
            bb.next();

            bb.vertex(0, i, 16);
            lineColor(bb, i);
            bb.next();
            bb.vertex(16, i, 16);
            lineColor(bb, i);
            bb.next();

            bb.vertex(16, i, 0);
            lineColor(bb, i);
            bb.next();
            bb.vertex(16, i, 16);
            lineColor(bb, i);
            bb.next();
        }

        tes.draw();
        bb.offset(0.0, 0.0, 0.0);

        if (noDepth.getVal())
            GlStateManager.enableDepthTest();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }
}
