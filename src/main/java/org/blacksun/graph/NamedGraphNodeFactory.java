package org.blacksun.graph;

import org.jetbrains.annotations.NotNull;

public class NamedGraphNodeFactory implements GraphNodeFactory {
    private final String name;
    private int nodesCounter;

    public NamedGraphNodeFactory() {
        this("Node");
    }

    public NamedGraphNodeFactory(@NotNull String name) {
        this.name = name;
        nodesCounter = 0;
    }

    @Override
    public GraphNode createNode() {
        return new NamedGraphNode(name + nodesCounter++);
    }
}
