package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import main.KeyHandler;

public class Player {
    GamePanel gp;
    KeyHandler keyH;

    public int x, y; // Position
    public int width = 48, height = 48; // Dimensions
    public double velocityY = 0; // Vertical velocity
    public int jumpCount = 0; // Tracks the number of jumps
    public static int maxJumps = 20; // Maximum number of jumps allowed
    public static int speed = 5;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.x = 100;
        this.y = 100;
    }

    public void update() {
        // Additional player-specific updates can go here
    }

    public void draw(Graphics2D g2, int cameraY) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y - cameraY, width, height);
    }
}
