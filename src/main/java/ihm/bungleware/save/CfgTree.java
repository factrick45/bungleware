package ihm.bungleware.save;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Tree that can be converted from and to a configuration file. */
public class CfgTree {
    public static class Section {
        public final String name;
        public final Section parent;
        public Map<String, Section> children;
        public Map<String, String> entries;

        private Section(String name, Section parent) {
            this.name = name;
            this.parent = parent;
            this.children = new HashMap<>();
            this.entries = new HashMap<>();
        }
    }

    public Section root;
    private Section currentSection;

    public CfgTree() {
        root = new Section("root", null);
        currentSection = root;
    }

    /** Creates a tree from the provided file data. */
    public CfgTree(String data) {
        this();
        String[] lines = data.split("\n");
        for (String line : lines)
            parseLine(line);
        setRoot();
    }

    public CfgTree(byte[] data) {
        this(new String(data));
    }

    private void parseLine(String line) {
        // comment
        if (line.charAt(0) == ';')
            return;
        // section
        if (line.charAt(0) == '[') {
            setSection(line.substring(1, line.length() - 1));
            return;
        }
        // keyval
        String[] keyval = line.split(": ", 2);
        put(keyval[0], keyval[1]);
    }

    /** Get a value from the current section */
    public String get(String key) {
        return currentSection.entries.get(key);
    }

    /** Add an entry to the current section */
    public void put(String key, String val) {
        currentSection.entries.put(key, val);
    }

    /** Set the current section to root */
    public void setRoot() {
        currentSection = root;
    }

    /** Returns the current section */
    public Section getSection() {
        return currentSection;
    }

    /** Returns the ID of a section */
    public String getSectionID(Section node) {
        if (node.parent == null)
            return "";
        String ret = node.name;
        while (node.parent.parent != null) {
            node = node.parent;
            ret = node.name + "." + ret;
        }
        return ret;
    }

    /** Returns the ID of the current section */
    public String getSectionID() {
        return getSectionID(currentSection);
    }

    /**
     * Set the current section from an ID such as "modules.Visual.ESP." If any
     * sections in the ID don't exist, they will be automatically created.
    */
    public void setSection(String id) {
        Section node = root;
        String[] secs = id.split("\\.");
        Section parent = root;
        for (String sec : secs) {
            if (!node.children.containsKey(sec))
                node.children.put(sec, new Section(sec, parent));
            node = node.children.get(sec);
            parent = node;
        }
        currentSection = node;
    }

    /** Relatively set the current section */
    public void setSectionCD(String id) {
        setSection(getSectionID() + "." + id);
    }

    private void toStringR(StringBuilder sb, String pwd, Section node) {
        for (Map.Entry<String, String> entry : node.entries.entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        for (Section section : node.children.values()) {
            sb.append("[" + pwd + section.name + "]\n");
            toStringR(sb, pwd + section.name + ".", section);
        }
    }

    /** Returns a string which can be interpreted as file data. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringR(sb, "", root);
        return sb.toString();
    }
}
