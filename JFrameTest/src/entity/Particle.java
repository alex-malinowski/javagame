package entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    public int x, y; // Position
    public int size; // Size of the particle
    public int life; // Remaining life of the particle
    public Color color; // Color of the particle
    public int velocityX, velocityY; // Movement speed of the particle
    public int offset = Math.random() > 0.5 ? 1 : -1; // Random offset for particle movement

    public Particle(int x, int y, int size, int life, Color color, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.life = life;
        this.color = color;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void update() {
        x += velocityX + offset; // Add random offset to x velocity
        y += velocityY;
        life--; // Decrease life over time
    }

    public void draw(Graphics2D g2, int cameraY) {
        g2.setColor(color);
        g2.fillOval(x, y - cameraY, size, size);
    }

    public boolean isDead() {
        return life <= 0;
    }
}
