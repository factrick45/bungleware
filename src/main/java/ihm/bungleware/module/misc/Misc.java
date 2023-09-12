package ihm.bungleware.module.misc;

import ihm.bungleware.module.Category;

public class Misc extends Category {
    public Misc() {
        super("Misc");
        add(new Autoclick());
        add(new Autolog());
    }
}
