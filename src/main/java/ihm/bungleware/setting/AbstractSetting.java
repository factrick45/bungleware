package ihm.bungleware.setting;

/** Base implementation of a setting that stores a value. */
public abstract class AbstractSetting<T> implements Setting {
    private String name;
    private String desc = null;
    private T val;

    protected AbstractSetting(String name, T val) {
        this.name = name;
        this.val = val;
    }

    protected AbstractSetting(String name, String desc, T val) {
        this.name = name;
        this.desc = desc;
        this.val = val;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    @Override
    public boolean shouldSave() {
        return true;
    }
}
