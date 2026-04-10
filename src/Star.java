import java.awt.*;
import java.util.Random;

public class Star extends Circle {
    private static final int MIN_RADIUS = 1;
    private static final int MAX_RADIUS = 3;
    private static final int TWINKLE_SPEED = 500;
    private long lastTwinkleTime;
    private Color color;

    public Star(Point position) {
        super(position, new Random().nextInt(MAX_RADIUS - MIN_RADIUS + 1) + MIN_RADIUS, 0);
        this.color = Color.white;
        this.lastTwinkleTime = System.currentTimeMillis();
    }

    @Override
    public void paint(Graphics brush) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTwinkleTime > TWINKLE_SPEED) {
            toggleTwinkle();
            lastTwinkleTime = currentTime;
        }

        brush.setColor(color);
        brush.fillOval((int) (position.x - getRadius()), (int) (position.y - getRadius()),
                getRadius() * 2, getRadius() * 2);
    }

    private void toggleTwinkle() {
        //Randomly change color to simulate twinkling
        Random rand = new Random();
        if (rand.nextBoolean()) {
            this.color = Color.white;
        } else {
            this.color = new Color(rand.nextInt(256), rand.nextInt(256),
                    rand.nextInt(256)); // Random color
        }
    }
}