package ihm.bungleware.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.client.MinecraftClient;

import ihm.bungleware.module.combat.Combat;
import ihm.bungleware.module.misc.Misc;
import ihm.bungleware.module.movement.Movement;
import ihm.bungleware.module.visual.Visual;
import ihm.bungleware.utils.Utils;

/**
 * Collects and initializes categories, sending callbacks and providing search
 * functions for each category and their modules.
 */
public class Modules {
    private static List<Category> categories = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Return the first module that when applied to by filter returns true,
     * or return null if not applicable.
     */
    public static Module forFirstCriteria(Predicate<Module> filter) {
        for (var cat : categories) {
            for (var mod : cat) {
                if (filter.test(mod))
                    return mod;
            }
        }
        return null;
    }

    /** For each module, apply action. */
    public static void forEach(Consumer<Module> action) {
        for (var cat : categories) {
            for (var mod : cat)
                action.accept(mod);
        }
    }

    /** Passes the loop index as an additional argument to action. */
    public static void forEach(BiConsumer<Module, Integer> action) {
        int i = 0;
        for (var cat : categories) {
            for (var mod : cat) {
                action.accept(mod, i);
                i++;
            }
        }
    }

    /** For each module, if in game and enabled, apply action. */
    public static void forEachEnabled(Consumer<Module> action) {
        if (!Utils.isInGame())
            return;
        for (var cat : categories) {
            for (var mod : cat) {
                if (mod.isEnabled())
                    action.accept(mod);
            }
        }
    }

    /** Passes the loop index as an additional argument to action. */
    public static void forEachEnabled(BiConsumer<Module, Integer> action) {
        if (!Utils.isInGame())
            return;
        int i = 0;
        for (var cat : categories) {
            for (var mod : cat) {
                if (mod.isEnabled()) {
                    action.accept(mod, i);
                    i++;
                }
            }
        }
    }

    public static Category[] getCategories() {
        return categories.toArray(new Category[0]);
    }

    /** Return the module with a given name */
    public static Module getModule(String name) {
        return forFirstCriteria(mod -> {return mod.getName().equals(name);});
    }

    public static void init() {
        if (initialized)
            return;
        initialized = true;

        categories.add(new Combat());
        categories.add(new Movement());
        categories.add(new Visual());
        categories.add(new Misc());
    }

    public static void onGameJoin() {
        forEachEnabled(mod -> {mod.onEnabled();});
    }

    public static void onKey(int key, boolean pressed) {
        forEach(mod -> {
                for (var bind : mod.getBinds())
                    bind.onKey(key, pressed);});
    }

    public static void onTick() {
        if (Utils.isPaused())
            return;
        forEachEnabled(mod -> {mod.onTick();});
    }
}
