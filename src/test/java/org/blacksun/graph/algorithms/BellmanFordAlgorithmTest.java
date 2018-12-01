package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.graph.node.NamedGraphNodeFactory;
import org.blacksun.graph.channel.SimplexChannelFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BellmanFordAlgorithmTest {
    private static BellmanFordAlgorithm algorithm;
    private static ArrayList<GraphNode> nodes = new ArrayList<>();
    private static ArrayList<Channel> links = new ArrayList<>();

    @BeforeClass
    public static void init() {
        NamedGraphNodeFactory factory = new NamedGraphNodeFactory("Node", new SimplexChannelFactory());
        for (int i = 0; i < 9; ++i)
            nodes.add(factory.createNode());
        connect(0, 1, 3);
        connect(0, 4, 5);
        connect(0, 6, 7);
        connect(1, 2, 1);
        connect(1, 3, 4);
        connect(2, 6, 2);
        connect(3, 7, 1);
        connect(4, 5, 2);
        connect(5, 6, 3);
        connect(5, 7, 4);
        algorithm = new BellmanFordAlgorithm(nodes, links);
    }

    private static void connect(int from, int to, int weight) {
        links.add(nodes.get(from).addConnectedNode(nodes.get(to), weight));
        links.add(nodes.get(to).addConnectedNode(nodes.get(from), weight));
    }

    private GraphNode node(int index) {
        return nodes.get(index);
    }

    @Test
    public void isAccessible() {
        assertTrue(algorithm.isAccessible(node(0), node(7)));
        assertTrue(algorithm.isAccessible(node(1), node(5)));

        assertTrue(algorithm.isAccessible(node(8), node(8)));

        assertFalse(algorithm.isAccessible(node(0), node(8)));
        assertFalse(algorithm.isAccessible(node(5), node(8)));
    }

    @Test
    public void getMinDistance() {
        assertEquals(7, algorithm.getMinDistance(node(0), node(5)));
        assertEquals(0, algorithm.getMinDistance(node(8), node(8)));
        assertEquals(4, algorithm.getMinDistance(node(0), node(2)));
        assertEquals(6, algorithm.getMinDistance(node(2), node(7)));
        assertEquals((int)algorithm.INFINITY, algorithm.getMinDistance(node(0), node(8)));
        assertEquals((int)algorithm.INFINITY, algorithm.getMinDistance(node(8), node(2)));
    }

    @Test
    public void getDistances() {
        HashMap<GraphNode, Integer> map8 = algorithm.getDistances(node(8));
        assertEquals(new Integer(0), map8.get(node(8)));
        for (int i = 0; i < 8; ++i) {
            assertEquals(algorithm.INFINITY, map8.get(node(i)));
        }

        HashMap<GraphNode, Integer> map0 = algorithm.getDistances(node(0));
        assertEquals(new Integer(0), map0.get(node(0)));
        assertEquals(new Integer(3), map0.get(node(1)));
        assertEquals(new Integer(4), map0.get(node(2)));
        assertEquals(new Integer(7), map0.get(node(3)));
        assertEquals(new Integer(5), map0.get(node(4)));
        assertEquals(new Integer(7), map0.get(node(5)));
        assertEquals(new Integer(6), map0.get(node(6)));
        assertEquals(new Integer(8), map0.get(node(7)));
        assertEquals(algorithm.INFINITY, map0.get(node(8)));
    }

    @Test(expected = NotImplementedException.class)
    public void getPath() {
        assertFalse(algorithm.getPath(node(0), node(8)).exists());
        assertTrue(algorithm.getPath(node(0), node(7)).exists());
    }
}