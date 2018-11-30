package org.blacksun.graph.algorithms;

import org.blacksun.graph.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class GraphPath implements Iterable<Channel> {
    private ArrayList<Channel> links;

    public GraphPath() {
        links = new ArrayList<>();
    }

    public GraphPath add(@NotNull Channel channel) {
        links.add(channel);
        return this;
    }

    public GraphPath clear() {
        links.clear();
        return this;
    }

    public GraphPath remove() {
        if (!links.isEmpty())
            links.remove(links.size() - 1);
        return this;
    }

    public int getLength() {
        return links.size();
    }

    public int getWeight() {
        return links.stream().mapToInt(Channel::getWeight).sum();
    }

    public boolean exists() {
        return !links.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Channel> iterator() {
        return links.iterator();
    }

    @Override
    public void forEach(Consumer<? super Channel> action) {
        links.forEach(action);
    }

    @Override
    public Spliterator<Channel> spliterator() {
        return links.spliterator();
    }
}
