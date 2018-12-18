import graph.algorithm.PathFactory;
import graph.algorithm.Factory;
import graph.vertex.VertexFactory;
import graph.vertex.NodeFactory;
import network.Network;
import network.RegionalTopology;
import network.Topology;
import utils.WeightList;
import utils.PropertiesHandler;
import gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        WeightList weights = new WeightList(1, 2, 4, 5, 8, 11, 12, 13, 15, 17, 21, 23, 27);
        PropertiesHandler.getProps().setProperty("weights", weights);
        VertexFactory nodeFactory = new NodeFactory("");
        Topology topology = new RegionalTopology(12, 2.5, 2, weights, nodeFactory);
        Factory factory = new PathFactory();
        new MainFrame(new Network(topology, factory));
    }
}
