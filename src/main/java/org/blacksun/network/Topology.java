package org.blacksun.network;

import org.blacksun.graph.node.GraphNode;

import java.util.List;

public interface Topology {
    List<GraphNode> createNetwork();
}
