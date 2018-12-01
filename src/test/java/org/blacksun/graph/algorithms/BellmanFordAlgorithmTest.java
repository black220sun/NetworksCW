package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.channel.DuplexChannelFactory;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.graph.node.NamedGraphNodeFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.junit.Assert.*;

public class BellmanFordAlgorithmTest {
    private static PathFindingAlgorithm algorithm;
    private static ArrayList<GraphNode> nodes = new ArrayList<>();

    @BeforeClass
    public static void init() {
        NamedGraphNodeFactory factory = new NamedGraphNodeFactory("Node", new DuplexChannelFactory());
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
        algorithm = new BFAlgorithmFactory().getAlgorithm(nodes);
    }

    private static void connect(int from, int to, int weight) {
        nodes.get(from).addConnectedNode(nodes.get(to), weight);
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

    @Test
    public void getPath() {
        assertFalse(algorithm.getPath(node(0), node(8)).exists());
        GraphPath path = algorithm.getPath(node(0), node(7));
        assertTrue(path.exists());
        assertEquals(3, path.getLength());
        assertEquals(8, path.getWeight());
        Iterator<Channel> it = path.iterator();
        Channel first = it.next();
        assertEquals(node(0), first.getFromNode());
        assertEquals(node(1), first.getToNode());
        assertEquals(node(3), it.next().getToNode());
        assertEquals(node(7), it.next().getToNode());
        assertFalse(it.hasNext());
    }
}