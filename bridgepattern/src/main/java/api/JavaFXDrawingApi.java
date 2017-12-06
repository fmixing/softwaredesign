package api;

import config.Config;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Function;

public class JavaFXDrawingApi extends Application implements DrawingApi {

    private static Function<GraphicsContext, GraphicsContext> drawingFunction = Function.identity();

    private int width;

    private int height;


    public JavaFXDrawingApi() {
        this.width = Config.width;
        this.height = Config.height;
    }


    @Override
    public int getDrawingAreaWidth() {
        return width;
    }


    @Override
    public int getDrawingAreaHeight() {
        return height;
    }


    @Override
    public void drawCircle(int x, int y, int r) {
        drawingFunction = drawingFunction.andThen(gc -> {
            gc.fillOval(x - r, y - r, r * 2, r * 2);
            return gc;
        });
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        drawingFunction = drawingFunction.andThen(gc -> {
            gc.strokeLine(x1, y1, x2, y2);
            return gc;
        });
    }


    @Override
    public void showPicture() {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GRAY);

        drawingFunction.apply(gc);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
