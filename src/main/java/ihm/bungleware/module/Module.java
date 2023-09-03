package ihm.bungleware.module;

import ihm.bungleware.utils.Utils;

public class Module {
    private boolean enabled = false;
    private String name;

    protected Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // FIXME: will not call when leaving game.
    // ClientPlayNetworkHandler.onDisconnect() does not appear to call unless
    // exiting a server.
    public void onDisabled() {
    }

    public void onEnabled() {
    }

    public void onTick() {
    }

    public void setEnabled(boolean value) {
        enabled = value;
        if (Utils.isInGame()) {
            if (enabled)
                onEnabled();
            else
                onDisabled();
        }
    }

    public boolean toggle() {
        setEnabled(!enabled);
        return enabled;
    }
}
