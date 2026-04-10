import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Example extends Polygon implements KeyListener {

    private boolean wPress;

    public Example(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
    }


    public void paint(Graphics brush) {
        brush.setColor(Color.white);
        int[] x = new int[getPoints().length];
        int[] y = new int[getPoints().length];
        Point[] points = getPoints();
        for (int i = 0; i < getPoints().length; i++) {
            x[i] = (int) points[i].x;
            y[i] = (int) points[i].y;
        }
        brush.fillPolygon(x, y, getPoints().length);
    }

    public void move() {
        if (wPress) {
            //move up the screen
            //don't use this in your project, this is an example code
            //to get you started
            position.y -= 10;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case (KeyEvent.VK_W):
                wPress = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case (KeyEvent.VK_W):
                wPress = false;
                break;
        }
    }
}
