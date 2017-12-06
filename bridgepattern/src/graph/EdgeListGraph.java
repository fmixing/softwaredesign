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

public class EdgeListGraph extends Graph {

    public EdgeListGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }


    @Override
    public void readGraph(String path) {
        BufferedReader in;
        try {
            in = Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8"));

            List<String> nm = Arrays.stream(in.readLine().split(" ")).
                    filter(s -> !s.equals("")).collect(Collectors.toList());

            int n = Integer.parseInt(nm.get(0));
            int m = Integer.parseInt(nm.get(1));
            graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < m; i++) {
                List<String> ft = Arrays.stream(in.readLine().split(" ")).
                        filter(s -> !s.equals("")).collect(Collectors.toList());
                int from = Integer.parseInt(ft.get(0));
                for (int j = 1; j < ft.size(); j++) {
                    int to = Integer.parseInt(ft.get(j));
                    graph.get(from).add(to);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
