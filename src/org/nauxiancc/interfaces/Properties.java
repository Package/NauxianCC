package org.nauxiancc.interfaces;

import java.util.Map;

public class Properties {

    private static final String[] PROPERTIES = new String[]{"name", "version", "author", "description", "category", "parameter", "method", "return"};
    private static final String SKELETON = "public class $name {\n\t\n\tpublic $return $method($parameter){\n\t\t\n\t}\n\t\n}";

    private final Map<String, String> map;
    private final String skeleton;

    public Properties(final Map<String, String> map) {
        String skeleton = SKELETON;
        for (final String property : PROPERTIES) {
            if (map.get(property) == null) {
                throw new NoSuchFieldError("Missing property: " + property + " not found in XML file");
            }
            skeleton = skeleton.replace("$" + property, map.get(property));
        }
        this.map = map;
        this.skeleton = skeleton;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public double getVersion() {
        return Double.parseDouble(map.get("version"));
    }

    public String getName() {
        return map.get("name");
    }

    public String getAuthor() {
        return map.get("author");
    }

    public String getDescription() {
        return map.get("description");
    }

    public int getCategory() {
        return Math.min(Math.max(Integer.parseInt(map.get("category")), 1), 5);
    }

    public String getMethod() {
        return map.get("method");
    }

}
