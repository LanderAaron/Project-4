import java.awt.*;

public class Bullet extends Circle {
    private static final int SPEED = 8;
    private static final int LIFETIME = 50;
    private int age = 0;
    private double velocityX;
    private double velocityY;

    public Bullet(Point inPosition, int radius, double rotation) {
        super(inPosition, radius, rotation);
        velocityX = SPEED * Math.cos(Math.toRadians(rotation));
        velocityY = SPEED * Math.sin(Math.toRadians(rotation));
    }

    private static Point[] generateBulletShape() {
        return new Point[]{new Point (0,0)};

    }

    @Override
    public boolean intersects(Shape other) {
        return other.contains(position);
    }

    public void move() {
        position.x += velocityX;
        position.y += velocityY;
        age++;
        if (age > LIFETIME) {
            position.x = -1000; // Moves out of the screen
            position.y = -1000;
        }
    }

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.red);
        brush.fillOval((int)position.x, (int) position.y, 3, 3);
    }
}
