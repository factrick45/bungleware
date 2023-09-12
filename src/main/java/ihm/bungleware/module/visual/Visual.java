package ihm.bungleware.module.visual;

import ihm.bungleware.module.Category;

public class Visual extends Category {
    public Visual() {
        super("Visual");
        add(new Esp());
        add(new Nightvision());
        add(new Xray());
        add(new Zoom());
    }
}
