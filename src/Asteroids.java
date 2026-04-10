/*
CLASS: Asteroids
DESCRIPTION: Extending Game, Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class Asteroids extends Game {
    private Ship p1;
    private ArrayList<Asteroid> asteroids = new ArrayList<>();
    private Timer asteroidSpawner;
    private boolean shipDestroyed = false;
    private long respawnTime = 2;
    private static final long RESPAWN_DELAY = 4000;
    private boolean asteroidsStop = false;
    private static final long INVINCIBILITY_DURATION = 3000;
    private boolean invincible = false;
    private long invincibilityEndTime = 0;
    private ArrayList<Star> stars = new ArrayList<>();
    private int initialSpawnDelay = 2000;
    private int spawnRate = 5000;
    private int maxAsteroids = 30;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean gamePaused = false;

    //builds the objects
    public Asteroids() {
        super("Asteroids!", 800, 600);
        startGame();
    }
    private void startGame() {
        p1 = new Ship(new Point[]{new Point(0, 0),
                new Point(-5 , -5), //
                new Point(10, 0),
                new Point(-5, 5)}
                , new Point(400, 300), 0);
        addKeyListener(p1);
        asteroids.clear();
        stars.clear();
        score = 0;
        lives = 3;
        gameOver = false;

        //Spawns the stars
        for (int i = 0; i < 50; i++) {
            stars.add(new Star(new Point(new Random().nextInt(800), new Random().nextInt(600))));
        }

        //Spawn asteroids
        asteroidSpawner = new Timer();
        asteroidSpawner.schedule(new TimerTask() {
            @Override
            public void run() {
                if (asteroids.size() < maxAsteroids) {
                    spawnAsteroids();
                }
            }
        }, initialSpawnDelay, spawnRate);
        repaint();
    }

    //Spawn asteroid method
    private void spawnAsteroids() {
        int randomSize = new Random().nextInt(30) + 10;
        Point spawnPoint = getOffScreenSpawnPoint();
        asteroids.add(new Asteroid(spawnPoint, randomSize));
    }

    //Have asteroids spawn off-screen
    private Point getOffScreenSpawnPoint() {
        Random random = new Random();
        boolean proximity;
        int x = 0, y = 0;

        do {
            int side = random.nextInt(4);
            switch (side) {
                case 0: //Top
                    x = random.nextInt(1200);
                    y = -20;
                    break;
                case 1: //Bottom
                    x = random.nextInt(1200);
                    y = 620;
                    break;
                case 2: //Left
                    x = -20;
                    y = random.nextInt(1000);
                    break;
                case 3: //Right
                    x = 820;
                    y = random.nextInt(1000);
                    break;
            }
            double distance = Math.sqrt(Math.pow(x - 800, 2) + Math.pow(y - 600, 2));
            proximity = distance < 200;
        } while (proximity);
        return new Point(x, y);
    }

    //Paint the window and graphics
    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);

        //Game over screen
        if (gameOver) {
            brush.setColor(Color.white);
            brush.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            brush.drawString("GAME OVER", 400, 300);
            return;
        }

        //Pause screen
        if (gamePaused) {
            brush.setColor(Color.yellow);
            brush.setFont(new Font("Arial", Font.BOLD, 40));
            brush.drawString("PAUSED", 330, 300);
            return;
        }

        //Draw out the score
        brush.setColor(Color.white);
        brush.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        brush.drawString("Score: " + score, 20, 30);
        brush.drawString("Lives: " + lives, 20, 60);

        for (Star star : stars) {
            star.paint(brush);
        }

        //Checks if ship's destroyed
        if (!shipDestroyed) {
            if (invincible) {
                if (System.currentTimeMillis() / 100 % 2 == 0) {
                    p1.paint((brush));
                }
                if (invincible && System.currentTimeMillis() >= invincibilityEndTime) {
                    invincible = false;
                }
            } else {
                p1.paint(brush);
            }
            if (!gamePaused) {
                p1.move();
                p1.updateBullets();
            }
        } else {
            Respawn();
        }

        //Builds the bullets
        p1.updateBullets();
        for (Bullet bullet : p1.getBullets()) {
            bullet.paint(brush);
            bullet.move();
        }
        //Builds the asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.paint(brush);
            if (!asteroidsStop) {
                asteroid.move();
            }
        }
        //Have invincible frames for 3 seconds after dying
        checkCollisions();
        if (!invincible) {
            checkShipCollisions();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            gamePaused = !gamePaused;
            repaint();
        }
    }

    //Check is ship collides with asteroid
    private void checkShipCollisions() {
        if (shipDestroyed) return;
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        ArrayList<Asteroid> shatteredAsteroids = new ArrayList<>();

        for (Asteroid asteroid : asteroids) {
            if (p1.intersects(asteroid)) {
                shipDestroyed = true;
                respawnTime = System.currentTimeMillis() + RESPAWN_DELAY;
                asteroidsToRemove.add(asteroid);
                p1.setActive(false);
                p1.disableShooting();
                lives--;
                shatteredAsteroids.addAll(asteroid.split());
                shatteredShip(p1.getPosition());
                if (lives <= 0) {
                    gameOver = true;
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Game Over! Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        startGame();
                    } else {
                        System.exit(0);

                    }
                }
            }
        }
    }
    //Shows graphics of ship and asteroid shattering
    private void shatteredShip(Point position) {
        int numFragments = 5;
        Random rand = new Random();
        for (int i = 0; i < numFragments; i++) {
            int size = rand.nextInt(4) + 2;
            double angle = Math.toRadians(rand.nextInt(360));
            int speed = rand.nextInt(3) + 1;
            Point fragmentPosition = new Point(position.x + rand.nextInt(10) - 5,
                    position.y + rand.nextInt(10) - 5);
            Bullet fragment = new Bullet(fragmentPosition, size, Math.toDegrees(angle));
            fragment.setVelocity(speed * Math.cos(angle), speed * Math.sin(angle));
            p1.getBullets().add(fragment);
        }
    }

    //Respawn the ship after getting destroyed
    private void Respawn() {
        if (shipDestroyed && System.currentTimeMillis() >= respawnTime) {
            p1 = new Ship(new Point[]{new Point(0, 0),
                    new Point(-5 , -5), //
                    new Point(10, 0),
                    new Point(-5, 5)}
                    , new Point(400, 300), 0);
            addKeyListener(p1);
            shipDestroyed = false;
            asteroidsStop = false;
            invincible = true;
            invincibilityEndTime = System.currentTimeMillis() + INVINCIBILITY_DURATION;
            p1.setActive(true);
            p1.enableShooting();
        }
    }

    private void checkCollisions() {
        ArrayList<Asteroid> newAsteroids = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        Random rand = new Random();

        // p1 means ship
        for (Bullet bullet : p1.getBullets()) {
            for (Asteroid asteroid : asteroids) {
                if (bullet.intersects(asteroid)) {
                    score += rand.nextInt(251) + 50;
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