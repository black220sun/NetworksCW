package utils;

import guru.nidi.graphviz.attribute.Color;
import graph.edge.HalfDuplexChannelFactory;

import java.util.HashMap;
import java.util.function.Supplier;

public class PropertiesHandler {
    private static PropertiesHandler cfg = new PropertiesHandler();

    private final HashMap<String, Object> properties;

    private PropertiesHandler() {
        properties = new HashMap<> ();
        properties.put("frameW", 2000);
        properties.put("frameH", 1050);
        properties.put("viewW", 1400);
        properties.put("viewH", (Supplier) () -> getInt("frameH"));
        properties.put("graphW", (Supplier) () -> getInt("viewW") - 20);
        properties.put("graphH", (Supplier) () -> getInt("viewH") - 70);
        properties.put("resize", true);
        properties.put("vertex", Color.LIGHTBLUE);
        properties.put("edge", Color.BLACK);
        properties.put("selectedN", Color.GREEN);
        properties.put("selectedC", Color.BLUE);
        properties.put("connected", Color.YELLOW);
        properties.put("terminal", Color.RED);
        properties.put("utility", 16); // package for VC initiation
        properties.put("package", 128); // package size
        properties.put("message", 1024); // average message size (+/- 50%)
        //properties.put("render", true);
        properties.put("ticks", 2000); // min ticks for a single test
        properties.put("delay", 25); // delay between messages appearance
        properties.put("amount", 1); // amount of appeared messages
        properties.put("channelFactory", new HalfDuplexChannelFactory());
    }

    public static PropertiesHandler getProps() {
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
        if (value instanceof Supplier)
            return (T) ((Supplier) value).get();
        return (T) value;
    }

    public <T> T getProperty(String property, T defaultValue) {
        T value = getProperty(property);
        return value == null ? defaultValue : value;
    }

    public void setProperty(String property, Object value) {
        properties.put(property, value);
    }
}
