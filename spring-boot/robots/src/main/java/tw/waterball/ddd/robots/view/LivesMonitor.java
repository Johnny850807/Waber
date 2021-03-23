package tw.waterball.ddd.robots.view;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.robots.Framework;
import tw.waterball.ddd.robots.life.AbstractUserBot;
import tw.waterball.ddd.robots.life.DriverBot;
import tw.waterball.ddd.robots.life.Life;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class LivesMonitor extends JFrame implements Framework.LivesListener {
    private Canvas canvas;
    private static final int CANVAS_WIDTH = 1000;
    private static final int CANVAS_HEIGHT = 800;
    private Collection<Life> lives = Collections.emptyList();

    public void launch() {
        setupCanvas();
    }

    private void setupCanvas() {
        canvas = new Canvas();
        setContentPane(canvas);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setLocation(200, 200);
        setVisible(true);
    }

    @Override
    public void onTick(Collection<Life> lives) {
        this.lives = lives;
        if (canvas != null) {
            canvas.repaint();
        }
    }

    private class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

            lives.forEach(life -> {
                if (life instanceof AbstractUserBot) {
                    ((AbstractUserBot) life).getLocation()
                            .map(LivesMonitor.this::normalize)
                            .ifPresent(location -> {
                                Point point = convertLocationToPoint(location);
                                g.setColor(life instanceof DriverBot ? Color.red : Color.green);
                                g.fillRect(point.x, point.y, 10, 10);
                            });
                }
            });
        }

        private Point convertLocationToPoint(Location location) {
            //TODO
            return new Point((int) location.getLatitude(), (int) location.getLongitude());
        }
    }

    private Location normalize(Location location) {
        return new Location(minmaxNormalize(0, CANVAS_WIDTH, location.getLatitude()),
                minmaxNormalize(0, CANVAS_HEIGHT, location.getLongitude()));
    }

    private double minmaxNormalize(double min, double max, double value) {
        double dataHigh = 250;
        double dataLow = -250;
        return ((value - dataLow)
                / (dataHigh - dataLow))
                * (max - min) + min;
    }

}