package graph;

import api.DrawingApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixGraph extends Graph {

    public MatrixGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }


    @Override
    public void readGraph(String path) {
        BufferedReader in;
        try {
            in = Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8"));

            int n = Integer.parseInt(in.readLine());
            graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
                List<String> collect = Arrays.stream(in.readLine().split(" ")).
                        filter(s -> !s.equals("")).collect(Collectors.toList());
                for (int j = 0; j < n; j++) {
                    int vij = Integer.parseInt(collect.get(j));
                    if (vij == 1) {
                        graph.get(i).add(j);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
