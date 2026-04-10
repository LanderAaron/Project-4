import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Ship extends Polygon implements KeyListener {
    private boolean wPress, aPress, sPress, dPress;
    private Point velocity;
    private static final double ACCELERATE = 0.05;
    private static final double DEACCELERATE = 0.02;
    private static final double MAX_SPEED = 1.0;
    private static final double REVERSE_THRUST = 0.02;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private boolean active = true;
    private boolean canShoot = true;

    public Ship(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
        velocity = new Point(0, 0);
    }

    public void enableShooting() {
        canShoot = true;
    }

    public void disableShooting() {
        canShoot = false;
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.white);
        int[] x = new int[getPoints().length];
        int[] y = new int[getPoints().length];
        Point[] points = getPoints();
        for (int i = 0; i < points.length; i++) {
            x[i] = (int) points[i].x;
            y[i] = (int) points[i].y;
        }
        brush.drawPolygon(x, y, points.length);
    }
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    public void setActive(boolean state) {
        this.active = state;
    }

    public void move() {
        if (wPress)
            accelerate(ACCELERATE);
        if (sPress)
            accelerate(-REVERSE_THRUST);
        if (aPress)
            rotate(-5);
        if (dPress)
            rotate(5);

        velocity.x *= (1 - DEACCELERATE);
        velocity.y *= (1 - DEACCELERATE);

        double speed = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
        if (speed > MAX_SPEED) {
            velocity.x = (velocity.x / speed) * MAX_SPEED;
            velocity.y = (velocity.y / speed) * MAX_SPEED;
        }

        position.x += velocity.x;
        position.y += velocity.y;

        if (position.x < 0) position.x = 800;
        if (position.x > 800) position.x = 0;
        if (position.y < 0) position.y = 600;
        if (position.y > 600) position.y = 0;
    }

    public void accelerate(double acceleration) {
        velocity.x += acceleration * Math.cos(Math.toRadians(rotation));
        velocity.y += acceleration * Math.sin(Math.toRadians(rotation));
    }


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) wPress = true;
        if (key == KeyEvent.VK_A) aPress = true;
        if (key == KeyEvent.VK_S) sPress = true;
        if (key == KeyEvent.VK_D) dPress = true;
        if (key == KeyEvent.VK_SPACE && canShoot) {
            double bulletX = position.x + 10 * Math.cos(Math.toRadians(rotation));
            double bulletY = position.y + 10 * Math.sin(Math.toRadians(rotation));
            bullets.add(new Bullet(new Point((int) bulletX, (int) bulletY), 3, rotation));

        }
    }
    public void updateBullets() {
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) wPress = false;
        if (key == KeyEvent.VK_A) aPress = false;
        if (key == KeyEvent.VK_S) sPress = false;
        if (key == KeyEvent.VK_D) dPress = false;
    }

    public void keyTyped(KeyEvent e) {}
}
