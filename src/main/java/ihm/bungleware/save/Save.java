package ihm.bungleware.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.*;

import ihm.bungleware.module.Category;
import ihm.bungleware.module.Module;
import ihm.bungleware.module.Modules;
import ihm.bungleware.setting.Setting;
import ihm.bungleware.ui.Hud;
import ihm.bungleware.utils.Utils;

/**
 * Saves and loads state from a configuration file. Meant to have a short
 * lifetime. Basic usage involves creating a new instance and calling .save()
 * or .load().
 */
public class Save {
    private static final String HEADER =
        "version: " + Utils.getModVersion() + "\n";

    private CfgTree tree;
    private final Path savefile;

    public Save(Path path) {
        savefile = path;
    }

    public void save() {
        tree = new CfgTree();
        saveHud();
        saveModules();
        try {
            Files.write(
                savefile, (HEADER + tree.toString()).getBytes(), CREATE,
                TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            tree = new CfgTree(Files.readAllBytes(savefile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadHud();
        loadModules();
    }

    private void saveHud() {
        tree.setSection("hud");
        tree.put("watermark", Hud.INSTANCE.enableWatermark ? "true" : "false");
        tree.put("modules", Hud.INSTANCE.enableModules ? "true" : "false");
    }

    private void loadHud() {
        tree.setSection("hud");
        if (tree.get("watermark") != null)
            Hud.INSTANCE.enableWatermark = tree.get("watermark").equals("true");
        if (tree.get("modules") != null)
            Hud.INSTANCE.enableModules = tree.get("modules").equals("true");
    }

    private void saveModules() {
        for (Category cat : Modules.getCategories()) {
            String catsection = "modules." + cat.getName();
            for (Module mod : cat) {
                tree.setSection(catsection + "." + mod.getName());
                tree.put("_enabled", mod.isEnabled() ? "true" : "false");
                for (Setting set : mod.getSettings()) {
                    if (set.shouldSave())
                        tree.put(set.getName(), set.serialize());
                }
            }
        }
    }

    private void loadModules() {
        for (Category cat : Modules.getCategories()) {
            String catsection = "modules." + cat.getName();
            for (Module mod : cat) {
                tree.setSection(catsection + "." + mod.getName());
                // This module clearly isn't in the cfg file
                if (tree.get("_enabled") == null)
                    continue;
                mod.setEnabled(tree.get("_enabled").equals("true"));
                for (Setting set : mod.getSettings()) {
                    if (tree.get(set.getName()) != null)
                        set.deserialize(tree.get(set.getName()));
                }
            }
        }
    }
}
