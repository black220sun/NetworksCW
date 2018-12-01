package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.StringRepresentable;

/**
 * Represents connection between two nodes from one network.
 * <br>
 * Parameters of channel are mutable but connected nodes could not be changed.
 */
public interface Channel extends StringRepresentable {
    double ERRORS_AMOUNT = 0.1;

    GraphNode getFromNode();

    GraphNode getToNode();

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
