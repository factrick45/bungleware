package ihm.bungleware.module.visual;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.BoolSetting;
import ihm.bungleware.setting.IntSetting;

public class Esp extends Module {
    private BoolSetting test = new BoolSetting("Test", "Testtttt", false);
    private IntSetting test2 = new IntSetting("Integer", "testy", 5, 0, 10);

    public Esp() {
        super("ESP", "Extrasensory perception!");
        addSettings(test, test2);
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
