package ihm.bungleware.setting;

/** A setting that can be stored by a module and rendered by MenuScreen. */
public interface Setting {
    public String getName();
    public String getDesc();
    public void render();
}
