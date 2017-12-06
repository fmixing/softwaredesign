import api.DrawingApi;
import api.JavaAWTDrawingApi;
import api.JavaFXDrawingApi;
import config.Config;
import graph.EdgeListGraph;
import graph.Graph;
import graph.MatrixGraph;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        DrawingApi api;
        Graph graph;

        if (args.length != 5) {
            System.err.println("The length of arguments should be equal to 5");
            return;
        }

        int width;
        int height;
        try {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
            Config.setWidth(width);
            Config.setHeight(height);
        } catch (NumberFormatException e) {
            System.err.println("Can't parse width and height");
            return;
        }

        switch (args[2]) {
            case "javafx":
                api = new JavaFXDrawingApi();
                break;
            case "awt":
                api = new JavaAWTDrawingApi();
                break;
            default:
                System.err.println("Type of drawing api should be javafx or awt");
                return;
        }

        switch (args[3]) {
            case "matrix":
                graph = new MatrixGraph(api);
                break;
            case "edgelist":
                graph = new EdgeListGraph(api);
                break;
            default:
                System.err.println("Type of graph should be matrix or edgelist");
                return;
        }

        String path = args[4];

        try {
            Paths.get(path);
        } catch (InvalidPathException e) {
            System.err.println("Can't parse a path");
        }

        graph.readGraph(path);
        graph.drawGraph();
    }
}
