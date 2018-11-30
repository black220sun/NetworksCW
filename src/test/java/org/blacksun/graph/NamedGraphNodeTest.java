package org.blacksun.graph;

import org.blacksun.utils.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class NamedGraphNodeTest {
    private static GraphNode node;
    private static GraphNode node2;

    @Before
    public void init() {
        node = new NamedGraphNode("Test node");
        node2 = new NamedGraphNode("Tmp node");
    }

    @Test
    public void getConnectedNodes() {
        node.addConnectedNode(node2, 3);
        node.addConnectedNode(node2, 2);
        node.addConnectedNode(node2, 5);

        List<GraphNode> result = node.getConnectedNodes();

        assertEquals(3, result.size());
        result.forEach(node -> assertEquals(node2, node));
    }

    @Test
    public void getConnections() {
        node.addConnectedNode(node2, 3);
        node.addConnectedNode(node2, 2);
        node.addConnectedNode(node2, 5);

        ArrayList<Pair<GraphNode, Integer>> expected = new ArrayList<>();
        expected.add(new Pair<>(node2, 2));
        expected.add(new Pair<>(node2, 3));
        expected.add(new Pair<>(node2, 5));
        Iterator<Pair<GraphNode, Integer>> it = expected.stream().iterator();

        node.getConnections().stream()
                .sorted(Comparator.comparing(Pair::getSecond))
                .forEach(pair -> assertEquals(it.next(), pair));
    }

    //TODO(more nodes?)
    @Test
    public void isConnected() {
        assertFalse(node.isConnected(node2));
        assertFalse(node2.isConnected(node));
        assertFalse(node.isConnected(node));

        node.addConnectedNode(node2, 7);

        assertTrue(node.isConnected(node2));
        assertTrue(node2.isConnected(node));
        assertFalse(node.isConnected(node));
    }

    @Test
    public void getOrder() {
        assertEquals(0, node.getOrder());

        node.addConnectedNode(node2, 1);
        node.addConnectedNode(node2, 1);
        node.addConnectedNode(node2, 1);
        assertEquals(3, node.getOrder());
        assertEquals(3, node2.getOrder());

        node.removeConnectedNode(node2);
        assertEquals(2, node.getOrder());
        assertEquals(2, node2.getOrder());

        node.removeConnectedNode(node2);
        node.removeConnectedNode(node2);
        assertEquals(0, node.getOrder());
        assertEquals(0, node2.getOrder());

        node.removeConnectedNode(node2);
        assertEquals(0, node.getOrder());
        assertEquals(0, node2.getOrder());
    }

    @Test
    public void stringRepresentation() {
        assertEquals("Test node [\n]", node.stringRepresentation());
    }

    @Test
    public void stringRepresentation2() {
        node.addConnectedNode(node2, 7);

        String expected = "Test node [\n\t"
                + node + " <--> " + node2
                + " (weight=7, errors=10.0%)\n]";
        assertEquals(expected, node.stringRepresentation());
    }
}
