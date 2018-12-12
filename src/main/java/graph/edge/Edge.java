package graph.edge;

import graph.vertex.Vertex;
import utils.Selectable;
import utils.Stringable;

/**
 * Represents connection between two nodes from one network.
 * <br>
 * Parameters of edge are mutable but connected nodes could not be changed.
 */
public interface Edge extends Stringable, Selectable {
    double ERRORS_AMOUNT = 0.1;

    Vertex getFromNode();

    Vertex getToNode();

    void setWeight(int weight);

    int getWeight();

    void setErrors(double errors);

    double getErrors();

    void setUsed(boolean used);

    boolean isUsed();

    void connect();

    void remove();

    @Override
    default String stringRepresentation() {
        return toString();
    }
}
