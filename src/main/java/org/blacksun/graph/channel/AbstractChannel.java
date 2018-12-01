package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

abstract class AbstractChannel implements Channel {
    protected final GraphNode fromNode;
    protected final GraphNode toNode;
    protected boolean used;
    protected int weight;
    protected double errors;

    public AbstractChannel(@NotNull GraphNode fromNode, @NotNull GraphNode toNode, int weight, double errors) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
        this.errors = errors;
        used = false;
    }

    protected abstract String getDirection();

    @Override
    public GraphNode getFromNode() {
        return fromNode;
    }

    @Override
    public GraphNode getToNode() {
        return toNode;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setErrors(double errors) {
        this.errors = errors;
    }

    @Override
    public double getErrors() {
        return errors;
    }

    @Override
    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public void connect() {
        fromNode.addConnection(this);
    }

    @Override
    public void remove() {
        fromNode.removeConnection(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractChannel that = (AbstractChannel) o;
        return used == that.used &&
                weight == that.weight &&
                Double.compare(errors, that.errors) == 0 &&
                Objects.equals(fromNode, that.fromNode) &&
                Objects.equals(toNode, that.toNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromNode, toNode, used, weight, errors);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (weight=%d, errors=%.1f%%)",
                fromNode, getDirection(), toNode, weight, errors * 100);
    }
}
