package org.blacksun.base;

import org.blacksun.utils.WeightList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SimpleTopologyTest {
    private static Topology topology;
    private static final int NODES = 50;
    private static final double ORDER = 5.5;

    @Before
    public void init() {
        topology = new SimpleTopology(NODES, ORDER, new WeightList(1));
    }

    @Test
    public void createNetwork() {
        assertEquals(NODES, topology.createNetwork().size());
    }

    @Test
    public void correctOrder() {
        // TODO(skip max and min values)
        double average = DoubleStream.generate(() -> new Network(topology).getAvgOrder())
                .limit(300)
                .average().orElse(0);

        assertTrue(average > ORDER - 0.4);
        assertTrue(average < ORDER + 0.6);
    }
}