package api;

import config.Config;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


public class JavaAWTDrawingApi extends Frame implements DrawingApi {

    private final int width;

    private final int height;

    private static final List<Line2D> lines = new ArrayList<>();

    private static final List<Ellipse2D> circles = new ArrayList<>();

    private final int down;


    public JavaAWTDrawingApi() {
        this.width = Config.width;
        this.height = Config.height;
        this.down = new Double(height * 0.05).intValue();
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
        circles.add(new Ellipse2D.Double(x - r, y - r + down, r * 2, r * 2));
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        lines.add(new Line2D.Double(x1, y1 + down, x2, y2 + down));
    }


    @Override
    public void showPicture() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        setSize(width, height + down);
        setVisible(true);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D ga = (Graphics2D)g;
        ga.setPaint(Color.black);
        for (Line2D l: lines) {
            ga.draw(l);
        }

        for (Ellipse2D c: circles) {
            ga.draw(c);
        }

    }
}
