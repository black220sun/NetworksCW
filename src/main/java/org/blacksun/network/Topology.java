package org.blacksun.network;

import org.blacksun.graph.GraphNode;

import java.util.List;

public interface Topology {
    List<GraphNode> createNetwork();
}
