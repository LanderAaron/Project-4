import java.awt.*;

public abstract class Shape extends Canvas {
    protected Point position;
    protected double rotation;

    public Shape(Point position, double rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public abstract boolean contains(Point point);

    public abstract void paint(Graphics brush);
    public abstract boolean intersects(Shape other);

    public Point getPosition() {
        return position;
    }
}
