/*
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Asteroids extends Game {
    private Ship p1;
    private ArrayList<Asteroid> asteroids = new ArrayList<>();
    private Timer asteroidSpawner;

    //builds the objects
    public Asteroids() {
        super("Asteroids!", 800, 600);
        p1 = new Ship(new Point[]{new Point(0, 0),
                new Point(0 , 10), //
                new Point(-10, -10),
                new Point(10, 0)}
                , new Point(400, 400), 0);
        addKeyListener(p1);
        for (int i = 0; i < 5; i++) {
            asteroids.add(new Asteroid(new Point(Math.random() * 800, Math.random() * 600), 20));
        }

        asteroidSpawner = new Timer();
        asteroidSpawner.schedule(new TimerTask() {
            @Override
            public void run() {
                spawnAsteroids();
            }
        }, 3000, 5000);
        repaint();
    }

    private void spawnAsteroids() {
        int maxAsteroids = 20;
        if (asteroids.size() < maxAsteroids) {
            int randomSize = new Random().nextInt(30) + 10;
            Point spawnPoint = getOffScreenSpawnPoint();
            asteroids.add(new Asteroid(spawnPoint, randomSize));
        }
    }

    private Point getOffScreenSpawnPoint() {
        Random random = new Random();
        int side = random.nextInt(4);
        int x = 0, y = 0;

        switch (side) {
            case 0: // Top
                x = random.nextInt(800);
                y = -20; // Just above the screen
                break;
            case 1: // Bottom
                x = random.nextInt(800);
                y = 620; // Just below the screen
                break;
            case 2: // Left
                x = -20; // Just left of the screen
                y = random.nextInt(600);
                break;
            case 3: // Right
                x = 820; // Just right of the screen
                y = random.nextInt(600);
                break;
        }
        return new Point(x, y);
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);
        if (p1 != null) {
            p1.paint(brush);
            p1.move();
        }

        p1.updateBullets();
        for (Bullet bullet : p1.getBullets()) {
            bullet.paint(brush);
            bullet.move();
        }
        for (Asteroid asteroid : asteroids) {
            asteroid.paint(brush);
            asteroid.move();
        }

        checkCollisions();
    }

    private void checkCollisions() {
        ArrayList<Asteroid> newAsteroids = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();

        for (Bullet bullet : p1.getBullets()) {
            for (Asteroid asteroid : asteroids) {
                if (bullet.intersects(asteroid)) {
                    asteroidsToRemove.add(asteroid);
                    bulletsToRemove.add(bullet);
                    newAsteroids.addAll(asteroid.split());
                }
            }
        }
        asteroids.removeAll(asteroidsToRemove);
        p1.getBullets().removeAll(bulletsToRemove);
        asteroids.addAll(newAsteroids);
    }

    public static void main(String[] args) {
        new Asteroids();
    }
}