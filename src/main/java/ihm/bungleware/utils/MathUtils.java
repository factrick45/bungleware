package ihm.bungleware.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
    /** Returns the lerped position of an entity. */
    public static Vec3d getLerpPos(Entity entity, float tickDelta) {
        return new Vec3d(
            entity.prevTickX + (entity.x - entity.prevTickX) * (double)tickDelta,
            entity.prevTickY + (entity.y - entity.prevTickY) * (double)tickDelta,
            entity.prevTickZ + (entity.z - entity.prevTickZ) * (double)tickDelta
        );
    }

    /** Returns lerped position, localized to the player. */
    public static Vec3d getLocalPos(Entity entity, float tickDelta) {
        Entity player = MinecraftClient.getInstance().player;
        return getLerpPos(entity, tickDelta)
            .subtract(getLerpPos(player, tickDelta));
    }
}
