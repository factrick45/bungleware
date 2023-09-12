package ihm.bungleware.module.visual;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.Tessellator;
import static net.minecraft.client.render.VertexFormats.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import ihm.bungleware.event.EventHandler;
import ihm.bungleware.event.RenderListener;
import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;
import ihm.bungleware.utils.MathUtils;

import static org.lwjgl.opengl.GL11.*;

public class Esp extends Module implements RenderListener {
    private BoolSetting playersOnly =
        new BoolSetting("Players only", "Only apply to players", true);

    public Esp() {
        super("ESP", "Extrasensory perception!");
        addSetting(playersOnly);
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

    private void renderEntity(Entity entity, float tickDelta) {
        Vec3d lpos = MathUtils.getLocalPos(entity, tickDelta);
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        Box box = entity.getBoundingBox();
        double bx = box.maxX - box.minX;
        double by = box.maxY - box.minY;
        double bz = box.maxZ - box.minZ;

        bb.offset(lpos.x - bx / 2, lpos.y, lpos.z - bz / 2);

        GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
        bb.begin(GL_QUADS, POSITION);
        // north
        bb.vertex(0, 0, 0).next();
        bb.vertex(0, by, 0).next();
        bb.vertex(bx, by, 0).next();
        bb.vertex(bx, 0, 0).next();
        // south
        bb.vertex(0, 0, bz).next();
        bb.vertex(bx, 0, bz).next();
        bb.vertex(bx, by, bz).next();
        bb.vertex(0, by, bz).next();
        // west
        bb.vertex(0, 0, 0).next();
        bb.vertex(0, 0, bz).next();
        bb.vertex(0, by, bz).next();
        bb.vertex(0, by, 0).next();
        // east
        bb.vertex(bx, 0, 0).next();
        bb.vertex(bx, by, 0).next();
        bb.vertex(bx, by, bz).next();
        bb.vertex(bx, 0, bz).next();
        // top
        bb.vertex(0, by, 0).next();
        bb.vertex(0, by, bz).next();
        bb.vertex(bx, by, bz).next();
        bb.vertex(bx, by, 0).next();
        // bottom
        bb.vertex(0, 0, 0).next();
        bb.vertex(bx, 0, 0).next();
        bb.vertex(bx, 0, bz).next();
        bb.vertex(0, 0, bz).next();
        tes.draw();

        GlStateManager.color(1.0f, 0.0f, 0.0f, 1.0f);
        bb.begin(GL_LINE_STRIP, POSITION);
        bb.vertex(0, 0, 0).next();
        bb.vertex(bx, 0, 0).next();
        bb.vertex(bx, by, 0).next();
        bb.vertex(bx, by, bz).next();
        bb.vertex(bx, 0, bz).next();
        bb.vertex(0, 0, bz).next();
        bb.vertex(0, by, bz).next();
        bb.vertex(0, by, 0).next();
        bb.vertex(0, 0, 0).next();
        bb.vertex(0, 0, bz).next();
        bb.vertex(bx, 0, bz).next();
        bb.vertex(bx, 0, 0).next();
        bb.vertex(bx, by, 0).next();
        bb.vertex(0, by, 0).next();
        bb.vertex(0, by, bz).next();
        bb.vertex(bx, by, bz).next();
        tes.draw();

        bb.offset(0.0, 0.0, 0.0);
    }

    @Override
    public void onRenderWorldPost(float tickDelta) {
        GlStateManager.disableDepthTest();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();

        MinecraftClient mc = MinecraftClient.getInstance();
        for (Entity ent : mc.world.getLoadedEntities()) {
            if (ent.equals(MinecraftClient.getInstance().player))
                continue;
            if (playersOnly.getVal() && !(ent instanceof PlayerEntity))
                continue;
            renderEntity(ent, tickDelta);
        }

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepthTest();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }
}
