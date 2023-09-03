package ihm.bungleware.module.visual;

import ihm.bungleware.module.Module;

public class Esp extends Module {
    public Esp() {
        super("ESP");
    }

    @Override
    public void onDisabled() {
        System.out.println("disabled");
    }

    @Override
    public void onEnabled() {
        System.out.println("enabled");
    }

    @Override
    public void onTick() {
        System.out.println("tick");
    }
}
