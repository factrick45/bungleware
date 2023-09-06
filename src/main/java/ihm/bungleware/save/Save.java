package ihm.bungleware.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.*;

import ihm.bungleware.module.Modules;

/**
 * Saves and loads state from a configuration file. Meant to have a short
 * lifetime. Basic usage involves creating a new instance and calling .save()
 * or .load().
 */
public class Save {
    private static final String HEADER =
        "; generated by Bungleware\n" +
        "version: 0.1.0\n";

    private CfgTree tree;
    private final Path savefile;

    public Save(Path path) {
        savefile = path;
    }

    public void save() {
        tree = new CfgTree();
        saveModules();
        try {
            Files.writeString(
                savefile, HEADER + tree.toString(), CREATE, TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            tree = new CfgTree(Files.readString(savefile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadModules();
    }

    private void saveModules() {
        for (var cat : Modules.getCategories()) {
            String catsection = "modules." + cat.getName();
            for (var mod : cat) {
                tree.setSection(catsection + "." + mod.getName());
                tree.put("_enabled", mod.isEnabled() ? "true" : "false");
                for (var set : mod.getSettings())
                    tree.put(set.getName(), set.serialize());
            }
        }
    }

    private void loadModules() {
        for (var cat : Modules.getCategories()) {
            String catsection = "modules." + cat.getName();
            for (var mod : cat) {
                tree.setSection(catsection + "." + mod.getName());
                mod.setEnabled(
                    (tree.get("_enabled").equals("true")) ? true : false
                );
                for (var set : mod.getSettings()) {
                    if (tree.get(set.getName()) != null)
                        set.deserialize(tree.get(set.getName()));
                }
            }
        }
    }
}
