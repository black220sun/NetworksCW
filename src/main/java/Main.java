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
        WeightList weights = new WeightList(3, 5, 6, 8, 10, 12, 17, 19, 20, 25, 27, 28);
        PropertiesHandler.getProps().setProperty("weights", weights);
        VertexFactory nodeFactory = new NodeFactory("");
        Topology topology = new RegionalTopology(12, 3.0, 2, weights, nodeFactory);
        Factory factory = new PathFactory();
        new MainFrame(new Network(topology, factory));
    }
}
