package network;

import graph.vertex.Vertex;

import java.util.List;

public interface Topology {
    List<Vertex> createNetwork();
    Vertex createNode();
}
