import javax.management.ListenerNotFoundException;
import java.awt.*;

public class Bullet extends Polygon {
    private static final int SPEED = 8;
    private static final int LIFETIME = 50;
    private int age = 0;
    public Bullet(Point inPosition, double inRotation) {
        super(generateBulletShape(), inPosition, inRotation);
    }

    private static Point[] generateBulletShape() {
        return new Point[]{new Point (0,0)};
    }

    @Override
    public boolean intersects(Polygon other) {
        return other.contains(position);
    }

    public void move() {
        position.x += SPEED * Math.cos(Math.toRadians(rotation));
        position.y += SPEED * Math.sin(Math.toRadians(rotation));
        age++;

        if (age > LIFETIME) {
            position.x = -1000; // Moves out of the screen
            position.y = -1000;
        }
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.red);
        brush.fillOval((int)position.x, (int) position.y, 3, 3);
    }


}
