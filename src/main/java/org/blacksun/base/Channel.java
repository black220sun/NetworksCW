package org.blacksun.base;

import org.blacksun.utils.StringRepresentable;

public class Channel implements StringRepresentable {
    public enum Type {
        DUPLEX, HALFDUPLEX
    }
    private static double ERRORS_AMOUNT = 0.1;

    private GraphNode fromNode;
    private GraphNode toNode;
    private int weight;
    private Type type;
    private double errors;

    public Channel(GraphNode from, GraphNode to, int weight) {
        this(from, to, weight, Type.DUPLEX, ERRORS_AMOUNT);
    }

    public Channel(GraphNode from, GraphNode to, int weight, Type type) {
        this(from, to, weight, type, ERRORS_AMOUNT);
    }

    public Channel(GraphNode from, GraphNode to, int weight, Type type, double errors) {
        fromNode = from;
        toNode = to;
        this.weight = weight;
        this.type = type;
        this.errors = errors;
    }

    public GraphNode getToNode() {
        return toNode;
    }

    public void reverse() {
        GraphNode tmp = fromNode;
        fromNode = toNode;
        toNode = tmp;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getErrors() {
        return errors;
    }

    public void setErrors(double errors) {
        this.errors = errors;
    }

    @Override
    public String stringRepresentation() {
        return toString();
    }

    // node1 <--> node2 (weight=7, errors=12%)
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(fromNode);
        switch (type) {
            case DUPLEX:
                builder.append(" <--> ");
                break;
            case HALFDUPLEX:
                builder.append(" ---> ");
                break;
        }
        builder.append(toNode)
                .append(" (weight=")
                .append(weight)
                .append(", errors=")
                .append(errors * 100)
                .append(" %)");
        return builder.toString();
    }
}
