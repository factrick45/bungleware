package ihm.bungleware.module.visual;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;

public class Esp extends Module {
    private BoolSetting test =
        new BoolSetting("Bool", "Testtttt", false);
    private IntSetting test2 =
        new IntSetting("Integer", "testy", 5, 0, 10);
    private ColorSetting test3 =
        new ColorSetting("Color", "testie", 0xffffffff);
    private FloatSetting test4 =
        new FloatSetting("Float", "testificate", 0.5f, 0.0f, 1.0f);
    private ModeSetting test5 =
        new ModeSetting("Mode", "testificator", "One", "One", "Two", "Three");

    public Esp() {
        super("ESP", "Extrasensory perception!");
        addSettings(test, test2, test3, test4, test5);
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
