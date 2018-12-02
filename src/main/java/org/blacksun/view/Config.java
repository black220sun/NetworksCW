package org.blacksun.view;

public class Config {
    private static Config cfg = new Config();
    private int frameWidth = 2000;
    private int frameHeight = 1200;
    private int viewWidth = 1400;
    private int viewHeight = frameHeight;
    private boolean resizeGraph = true;
    private int graphWidth = viewWidth - 100;
    private int graphHeight = viewHeight - 100;

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

    public static Config getConfig() {
        return cfg;
    }
}
