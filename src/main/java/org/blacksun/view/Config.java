package org.blacksun.view;

import guru.nidi.graphviz.attribute.Color;

public class Config {
    private static Config cfg = new Config();
    private int frameWidth = 2000;
    private int frameHeight = 1200;
    private int viewWidth = 1400;
    private int viewHeight = frameHeight;
    private boolean resizeGraph = true;
    private int graphWidth = viewWidth - 100;
    private int graphHeight = viewHeight - 100;
    private Color nodeColor = Color.BLACK;
    private Color selectedNodeColor = Color.BLUE;
    private Color channelColor = Color.BLACK;
    private Color selectedChannelColor = Color.BLUE;
    private Color connectedChannelColor = Color.RED;

    private Config() {
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public boolean isResizeGraph() {
        return resizeGraph;
    }

    public void setResizeGraph(boolean resizeGraph) {
        this.resizeGraph = resizeGraph;
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }

    public Color getNodeColor() {
        return nodeColor;
    }

    public void setNodeColor(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    public Color getSelectedNodeColor() {
        return selectedNodeColor;
    }

    public void setSelectedNodeColor(Color selectedNodeColor) {
        this.selectedNodeColor = selectedNodeColor;
    }

    public Color getChannelColor() {
        return channelColor;
    }

    public void setChannelColor(Color channelColor) {
        this.channelColor = channelColor;
    }

    public Color getSelectedChannelColor() {
        return selectedChannelColor;
    }

    public void setSelectedChannelColor(Color selectedChannelColor) {
        this.selectedChannelColor = selectedChannelColor;
    }

    public Color getConnectedChannelColor() {
        return connectedChannelColor;
    }

    public void setConnectedChannelColor(Color connectedChannelColor) {
        this.connectedChannelColor = connectedChannelColor;
    }

    public static Config getConfig() {
        return cfg;
    }
}
