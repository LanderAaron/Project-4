import java.awt.*;

public class Circle extends Shape {
    private int radius;

    public Circle(Point inPosition, int inRadius, double rotation) {
        super(inPosition, 0);
        this.radius = inRadius;
    }

    @Override
    public void paint(Graphics brush) {
        brush.fillOval((int) (position.x - radius), (int) (position.y - radius),
                radius * 2, radius * 2);
    }

    public boolean contains(Point point) {
        double distance = Math.sqrt(Math.pow(point.x - position.x, 2) + Math.pow(point.y - position.y, 2));
        return distance <= radius;
    }

    public int getRadius() {
        return radius;
    }

    public boolean intersects(Shape other) {
        if (other instanceof Circle) {
            Circle c = (Circle) other;
            double distance = Math.sqrt(Math.pow(this.position.x - c.position.x, 2) +
                    Math.pow(this.position.y - c.position.y, 2));
            return distance < (this.getRadius() + c.getRadius());
        }
        return false;
    }
}
