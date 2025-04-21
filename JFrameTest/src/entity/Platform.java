package entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class Platform {
    public int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2, int cameraY) {
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y - cameraY, width, height);
    }
}