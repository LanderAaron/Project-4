import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid extends Polygon {
    private Point velocity;
    private static final int SPEED = 1;
    private int size;
    private static final int MIN_SIZE = 15;
    private static final int MAX_SIZE = 40;

    public Asteroid(Point position, int size) {
        super(generateShape(size), position, 0);
        this.size = size;
        Random random = new Random();
        double angle = Math.atan2(300 - position.y, 400 - position.x);
        double speed = random.nextDouble() * SPEED + 1;
        velocity = new Point(speed * Math.cos(angle), speed * Math.sin(angle));
    }

    public static Point[] generateShape(int size) {
        Random random = new Random();
        int numPoints = random.nextInt(4) + 5;

        Point[] shape = new Point[numPoints];
        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double radius = size * (0.75 + random.nextDouble() * 0.5);
            shape[i] = new Point((int) (radius * Math.cos(angle)),
                    (int) (radius * Math.sin(angle)));
        }
        return shape;
    }

    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;

        if (position.x < 0) position.x = 800;
        if (position.x > 800) position.x = 0;
        if (position.y < 0) position.y = 600;
        if (position.y > 600) position.y = 0;
    }

    public ArrayList<Asteroid> split() {
        ArrayList<Asteroid> newAsteroids = new ArrayList<>();
        if (size > MIN_SIZE) {
            for (int i = 0; i < 2; i++) {
                int newSize = size / 2;
                Point newPosition = new Point(position.x + (int) (Math.random() * 10 - 5),
                        position.y + (int) (Math.random() * 10 - 5));
                Asteroid fragment = new Asteroid(newPosition, newSize);
                newAsteroids.add(fragment);
            }
        }
        return newAsteroids;
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.gray);
        int[] x = new int[getPoints().length];
        int[] y = new int[getPoints().length];
        Point[] points = getPoints();
        for (int i = 0; i < points.length; i++) {
            x[i] = (int) points[i].x;
            y[i] = (int) points[i].y;
        }
        brush.fillPolygon(x, y, points.length);
    }
}