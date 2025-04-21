package entity;

import java.awt.Color;
import java.awt.Graphics2D;

public class Buff {
    public int x, y, width, height;
    public String type; // Type of buff (e.g., "speed", "jumpReset")

    public Buff(int x, int y, int width, int height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public void draw(Graphics2D g2, int cameraY) {
        // Set color based on buff type
        switch (type) {
            case "speed":
                g2.setColor(Color.GREEN);
                break;
            case "jumpReset":
                g2.setColor(Color.BLUE);
                break;
            default:
                g2.setColor(Color.GRAY);
                break;
        }
        g2.fillRect(x, y - cameraY, width, height);
    }
}
