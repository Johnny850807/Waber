package tw.waterball.ddd.robots.view;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import lombok.SneakyThrows;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.robots.Framework;
import tw.waterball.ddd.robots.life.AbstractUserBot;
import tw.waterball.ddd.robots.life.DriverBot;
import tw.waterball.ddd.robots.life.Life;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class LivesMonitor extends JFrame implements Framework.LivesListener {
    private Image driverIcon;
    private Image driverMatchedIcon;
    private Image passengerIcon;
    private Image destinationIcon;
    private Canvas canvas;
    private static final int CANVAS_WIDTH = 1000;
    private static final int CANVAS_HEIGHT = 800;
    private Collection<Life> lives = Collections.emptyList();

    public void launch() {
        setupCanvas();
    }

    @SneakyThrows
    private void setupCanvas() {
        driverIcon = ImageIO.read(new File(Thread.currentThread().getContextClassLoader().getResource("driver.png").getFile()));
        driverMatchedIcon = ImageIO.read(new File(Thread.currentThread().getContextClassLoader().getResource("driver-matched.png").getFile()));
        passengerIcon = ImageIO.read(new File(Thread.currentThread().getContextClassLoader().getResource("passenger.png").getFile()));
        destinationIcon = ImageIO.read(new File(Thread.currentThread().getContextClassLoader().getResource("destination.png").getFile()));

        canvas = new Canvas();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
                                var state = ((AbstractUserBot) life).getState().toCharArray();
                                g.setColor(Color.white);
                                g.drawChars(state, 0, state.length, (int) location.getLatitude(), (int) location.getLongitude() - 10);
                                Point point = convertLocationToPoint(location);
                                if (life instanceof DriverBot) {
                                    drawDriver(g, (DriverBot) life, point);
                                } else {
                                    drawPassenger(g, point);
                                }
                            });
                }
            });
        }

        private void drawPassenger(Graphics g, Point point) {
            g.drawImage(passengerIcon, point.x + 30, point.y,
                    30, 30, null);
        }

        private void drawDriver(Graphics g, DriverBot driver, Point point) {
            if (driver.getStatus() == Driver.Status.AVAILABLE) {
                g.drawImage(driverIcon, point.x, point.y, 30, 30, null);
            } else {
                g.drawImage(driverMatchedIcon, point.x, point.y, 30, 30, null);
            }
            driver.getDestination()
                    .map(LivesMonitor.this::normalize)
                    .map(this::convertLocationToPoint)
                    .ifPresent(p -> g.drawImage(destinationIcon, p.x, p.y,
                            30, 30, null));
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