package ihm.bungleware.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Container used to collect and initialize modules. */
public class Category implements Iterable<Module> {
    private List<Module> modules = new ArrayList<>();
    private String name;

    protected Category(String name) {
        this.name = name;
    }

    protected void add(Module mod) {
        modules.add(mod);
    }

    public String getName() {
        return name;
    }

    public Iterator iterator() {
        return modules.iterator();
    }
}
