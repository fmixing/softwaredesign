package graph;

import api.DrawingApi;

import java.util.List;

public abstract class Graph {

    List<List<Integer>> graph;


    /**
     * Bridge to drawing api
     */
    private DrawingApi drawingApi;


    Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }


    public void drawGraph() {
        int centerX = drawingApi.getDrawingAreaWidth() / 2;
        int centerY = drawingApi.getDrawingAreaHeight() / 2;

        int graphRadius = new Double(Math.min(centerX, centerY) * 0.9).intValue();
        int vertexRadius = new Double(Math.min(centerX, centerY) * 0.05).intValue();

        int n = graph.size();
        int x[] = new int[n];
        int y[] = new int[n];

        for (int i = 0; i < n; i++) {
            double angle = (2 * Math.PI / n) * i;
            x[i] = centerX + (int) (Math.cos(angle) * graphRadius);
            y[i] = centerY + (int) (Math.sin(angle) * graphRadius);
            drawingApi.drawCircle(x[i], y[i], vertexRadius);
        }

        for (int i = 0; i < n; i++) {
            List<Integer> edges = graph.get(i);

            int v = i;
            edges.forEach(u -> drawingApi.drawLine(x[v], y[v], x[u], y[u]));
        }

        drawingApi.showPicture();

    }

    public abstract void readGraph(String path);
}