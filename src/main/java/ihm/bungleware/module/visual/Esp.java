package ihm.bungleware.module.visual;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.BoolSetting;

public class Esp extends Module {
    private BoolSetting test = new BoolSetting("Test", "Testtttt", false);

    public Esp() {
        super("ESP", "Extrasensory perception!");
        addSetting(test);
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
        if (test.getVal())
            System.out.println("tick");
        else
            System.out.println("tock");
    }
}
