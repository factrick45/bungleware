package ihm.bungleware.setting;

/**
 * A setting that can be stored and used by a module, rendered by MenuScreen,
 * and serialized into a string.
 */
public interface Setting {
    public String getName();
    public String getDesc();
    public void render();
    public String serialize();
    public void deserialize(String string);
}
