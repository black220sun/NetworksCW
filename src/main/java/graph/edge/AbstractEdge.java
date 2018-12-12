package graph.edge;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

abstract class AbstractEdge implements Edge {
    protected final Vertex fromNode;
    protected final Vertex toNode;
    protected boolean used;
    protected int weight;
    protected double errors;
    private boolean selected;

    public AbstractEdge(@NotNull Vertex fromNode, @NotNull Vertex toNode, int weight, double errors) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
        this.errors = errors;
        used = false;
    }

    protected abstract String getDirection();

    @Override
    public Vertex getFromNode() {
        return fromNode;
    }

    @Override
    public Vertex getToNode() {
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
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
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
        AbstractEdge that = (AbstractEdge) o;
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
        String busy = "";
        if (isUsed()) {
            busy = ", busy";
        }
        return String.format("%s %s %s (weight=%d, errors=%.1f%%%s)",
                fromNode, getDirection(), toNode, weight, errors * 100, busy);
    }
}
