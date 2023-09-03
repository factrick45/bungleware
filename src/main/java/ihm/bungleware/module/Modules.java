package ihm.bungleware.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    public static Module forFirstCriteria(Function<Module, Boolean> filter) {
        for (var cat : categories) {
            for (var mod : cat) {
                if (filter.apply(mod))
                    return mod;
            }
        }
        return null;
    }

    /**
     * For each module, if in game and enabled, apply action.
     */
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

    public static Module getModule(String name) {
        return forFirstCriteria(mod -> {return mod.getName() == name;});
    }

    public static void init() {
        if (initialized)
            return;
        initialized = true;

        categories.add(new Visual());

        System.out.println(getModule("ESP").getName());
    }

    public static void onGameJoin() {
        forEachEnabled(mod -> {mod.onEnabled();});
    }

    public static void onTick() {
        forEachEnabled(mod -> {mod.onTick();});
    }
}
