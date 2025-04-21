package main;

import entity.Buff;
import entity.Particle;
import entity.Platform;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

//THIGNS YOU COULD ADD: LAVA SPEED UP DEBUFF, coins, enemies, weapons or no?, parallax,;

public class GamePanel extends JPanel implements Runnable {

    // set screen settings
    final int originTileSize = 16;
    final int scale = 3;

    public final int tileSize = originTileSize * scale; // 48px tile
    final int maxScreenX = 16;
    final int maxScreenY = 12;
    final int screenWidth = tileSize * maxScreenX; // 768 pixels
    final int screenHeight = tileSize * maxScreenY; // 576 pixels

    // FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler();

    public Camera camera = new Camera(screenHeight);

    public UI ui = new UI(this);

    Thread gameThread;
    Player player = new Player(this, keyH);

    // Player player2 = new Player(this, null);

    // Add physics-related variables
    double gravity = 0.5; // Gravity force
    double jumpStrength = -10; // Upward velocity for jumping
    double maxFallSpeed = 10; // Limit for falling speed

    // set player in default position
    int playerX = 100;
    int playerY = 100;

    ArrayList<Platform> platforms = new ArrayList<>();
    ArrayList<Buff> buffs = new ArrayList<>();
    ArrayList<Particle> particles = new ArrayList<>();

    int lavaY = screenHeight; // Start at the bottom of the screen
    public float lavaSpeed = 2; // Speed at which the lava rises

    int cameraY = 0; // Camera's vertical offset

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Generate initial platforms
        platforms.add(new Platform(200, screenHeight - 200, 400, 20)); // Add a starting platform

        // find the first platform
        Platform firstPlatform = platforms.get(0);

        // Place the first player on the first platform
        player.x = firstPlatform.x + firstPlatform.width / 4 - player.width / 2;
        player.y = firstPlatform.y - player.height;

        // Place the second player on the first platform
        /*
        player2.x = firstPlatform.x + 3 * firstPlatform.width / 4 - player2.width / 2;
        player2.y = firstPlatform.y - player2.height;
        */
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Delta game loop
    public void run() {
        double drawInterval = 1000000000 / FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        // Adjust lava speed based on the camera's Y position
        lavaSpeed = 2 + Math.abs((camera.getCameraY() / 1252)); // Increase speed as the camera moves up

        // First player movement
        if (keyH.upPressed && player.jumpCount < player.maxJumps) {
            player.velocityY = jumpStrength; // Apply upward velocity
            player.jumpCount++; // Increment jump count
            generateParticles(player); // Generate particles when jumping
        }
        if (keyH.leftPressed) {
            player.x -= player.speed;
        }
        if (keyH.rightPressed) {
            player.x += player.speed;
        }

        // Apply gravity to the player
        player.velocityY += gravity;

        // Limit falling speed for the player
        if (player.velocityY > maxFallSpeed) {
            player.velocityY = maxFallSpeed;
        }

        // Update the player's position
        player.y += player.velocityY;

        // Check for collision with platforms
        for (Platform platform : platforms) {
            if (player.y + player.height >= platform.y && player.y + player.height <= platform.y + platform.height &&
                player.x + player.width > platform.x && player.x < platform.x + platform.width) {
                player.y = platform.y - player.height; // Place player on top of the platform
                player.velocityY = 0; // Stop falling
                player.jumpCount = 0; // Reset jump count
            }
        }

        // Check for buff collection
        Iterator<Buff> buffIterator = buffs.iterator();
        while (buffIterator.hasNext()) {
            Buff buff = buffIterator.next();
            if (player.x < buff.x + buff.width && player.x + player.width > buff.x &&
                player.y < buff.y + buff.height && player.y + player.height > buff.y) {
                applyBuff(player, buff.type);
                buffIterator.remove(); // Remove the buff after collection
            }
        }

        // Update camera position
        camera.update(player.y, player.y); // Second parameter should be player2.y if using two players

        // Update other game elements
        lavaY -= lavaSpeed; // Move lava upward
        generatePlatforms();
        removeOffScreenPlatforms();
        generateBuffs();
        removeOffScreenBuffs();
        updateParticles();
    }

    public void generatePlatforms() {
        while (platforms.size() < 10) { // Ensure there are always 10 platforms
            int x = (int) (Math.random() * (screenWidth)); // Random x position
            int y = camera.getCameraY() - (int) (Math.random() * 200); // Random y position above the camera
            platforms.add(new Platform(x, y, 200, 20));
        }
    }

    public void generateBuffs() {
        while (buffs.size() < 3) { // Ensure there are always 3 buffs on the screen
            int x = (int) (Math.random() * (screenWidth)); // Random x position
            int y = camera.getCameraY() - (int) (Math.random() * 200); // Random y position above the camera
            String[] types = {"speed", "jumpReset"};
            String type = types[(int) (Math.random() * types.length)]; // Random buff type
            buffs.add(new Buff(x, y, 30, 30, type)); // Add a new buff
        }
    }

    public void removeOffScreenPlatforms() {
        platforms.removeIf(platform -> platform.y - camera.getCameraY() > screenHeight);
    }

    public void removeOffScreenBuffs() {
        buffs.removeIf(buff -> buff.y - camera.getCameraY() > screenHeight);
    }

    public void applyBuff(Player player, String type) {
        switch (type) {
            case "speed":
                player.speed += 2; // Increase speed temporarily
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        player.speed -= 2; // Reset speed after 5 seconds
                    }
                }, 5000); // 5 seconds duration
                break;
            case "jumpReset":
                player.jumpCount = 0; // Reset jump count
                break;
        }
    }

    public void generateParticles(Player player) {
        for (int i = 0; i < 5; i++) { // Generate 5 particles per frame
            int x = player.x + player.width / 2 + (int) (Math.random() * 10 - 5); // Random x offset
            int y = player.y + player.height; // Below the player
            int size = (int) (Math.random() * 5 + 5); // Random size between 5 and 10
            int life = (int) (Math.random() * 20 + 10); // Random life between 10 and 30 frames
            Color color = new Color(200, 200, 200, 150); // Semi-transparent gray
            int velocityX = (int) (Math.random() * 2 - 1); // Random horizontal velocity
            int velocityY = (int) (Math.random() * 2 + 1); // Random downward velocity
            particles.add(new Particle(x, y, size, life, color, velocityX, velocityY));
        }
    }

    public void updateParticles() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();
            if (particle.isDead()) {
                iterator.remove(); // Remove dead particles
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw particles
        for (Particle particle : particles) {
            particle.draw(g2, camera.getCameraY());
        }

        // Draw other elements (lava, platforms, players, etc.)
        g2.setColor(Color.RED);
        g2.fillRect(0, lavaY - camera.getCameraY(), screenWidth, screenHeight - (lavaY - camera.getCameraY()));

        for (Buff buff : buffs) {
            buff.draw(g2, camera.getCameraY());
        }
        for (Platform platform : platforms) {
            platform.draw(g2, camera.getCameraY());
        }
        player.draw(g2, camera.getCameraY());
        ui.draw(g2);

        g2.dispose();
    }
}
