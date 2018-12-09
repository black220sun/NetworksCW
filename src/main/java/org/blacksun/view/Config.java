package org.blacksun.view;

import guru.nidi.graphviz.attribute.Color;

import java.util.HashMap;

public class Config {
    private final class Link {
        private final String name;

        Link(String name) {
            this.name = name;
        }
    }

    private static Config cfg = new Config();

    private final HashMap<String, Object> properties;

    private Config() {
        properties = new HashMap<> ();
        properties.put("frameW", 2000);
        properties.put("frameH", 1050);
        properties.put("viewW", 1400);
        properties.put("viewH", new Link("frameH"));
        properties.put("graphW", 1380);
        properties.put("graphH", 980);
        properties.put("resize", true);
        properties.put("selectedN", Color.BLUE);
        properties.put("selectedC", Color.BLUE);
        properties.put("connected", Color.RED);
        properties.put("package", 256);
        properties.put("message", 1024);
        //properties.put("render", true);
        properties.put("ticks", 2000);
    }

    public static Config getConfig() {
        return cfg;
    }

    public Color getColor(String property) {
        return getProperty(property, Color.BLACK);
    }

    public int getInt(String property) {
        return getProperty(property, 0);
    }

    public boolean getBoolean(String property) {
        return getProperty(property, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String property) {
        Object value = properties.get(property);
        if (value instanceof Link)
            return (T) this.getProperty(((Link) value).name);
        return (T) value;
    }

    public <T> T getProperty(String property, T defaultValue) {
        T value = getProperty(property);
        return value == null ? defaultValue : value;
    }

    public void setProperty(String property, Object value) {
        properties.put(property, value);
    }

    // to simplify toolbar
    public void setFrameWidth(Integer integer) {
        setProperty("frameW", integer);
    }
    public void setFrameHeight(Integer integer) {
        setProperty("frameH", integer);
    }
    public void setViewWidth(Integer integer) {
        setProperty("viewW", integer);
    }
    public void setViewHeight(Integer integer) {
        setProperty("viewH", integer);
    }
    public void setGraphWidth(Integer integer) {
        setProperty("graphW", integer);
    }
    public void setGraphHeight(Integer integer) {
        setProperty("graphH", integer);
    }
}
