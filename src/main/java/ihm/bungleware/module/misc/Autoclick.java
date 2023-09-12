package ihm.bungleware.module.misc;

import ihm.bungleware.module.Module;
import ihm.bungleware.setting.*;
import ihm.bungleware.utils.Inputs;

public class Autoclick extends Module {
    private IntSetting ldelay = new IntSetting(
        "L delay", "Delay for left clicking", 10, 0, 20);
    private ModeSetting lmode = new ModeSetting(
        "L mode", "Behavior for left clicks",
        "Off", "Off", "Press");

    private IntSetting rdelay = new IntSetting(
        "R delay", "Delay for right clicking", 0, 0, 20);
    private ModeSetting rmode = new ModeSetting(
        "R mode", "Behavior for right clicks",
        "Press", "Off", "Press");

    private int lcounter = 0;
    private int rcounter = 0;

    public Autoclick() {
        super("Autoclick", "Automatically click the mouse");
        addSettings(ldelay, lmode, rdelay, rmode);
        addDefaultBind();
    }

    private void left() {
        if (lmode.getVal().equals("Press")) {
            lcounter++;
            if (lcounter > ldelay.getVal()) {
                lcounter = 0;
                Inputs.leftClick();
            }
            return;
        }
    }

    private void right() {
        if (rmode.getVal().equals("Press")) {
            rcounter++;
            if (rcounter > rdelay.getVal()) {
                rcounter = 0;
                Inputs.rightClick();
            }
            return;
        }
    }

    @Override
    public void onEnabled() {
        lcounter = 0;
        rcounter = 0;
    }

    @Override
    public void onTick() {
        left();
        right();
    }
}
