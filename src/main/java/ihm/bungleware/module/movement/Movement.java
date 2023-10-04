package ihm.bungleware.module.movement;

import ihm.bungleware.module.Category;

public class Movement extends Category {
    public Movement() {
        super("Movement");
        add(new Flight());
        add(new Vclip());
    }
}
