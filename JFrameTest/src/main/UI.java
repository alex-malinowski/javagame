package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class UI {
	
	GamePanel gp;
	Font arial_40;
	
	public UI(GamePanel gp) {
		
		this.gp = gp;
		
		arial_40 = new Font("Arial", Font.PLAIN, 20);
	}
	
	public void draw(Graphics2D g2) {
		
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		g2.drawString("UI", 375, 48);
		g2.drawString("p1jct: " + gp.player.jumpCount, 10, 48);
		g2.drawString("pltct:"+ gp.platforms.size(), 10, 68);
		g2.drawString("camY: " + gp.camera.getCameraY(), 10, 88);
		g2.drawString("p1spd: " + gp.player.speed, 10, 108);
		g2.drawString("lspd: " + gp.lavaSpeed, 10, 128);
		//g2.drawString("p2jct: " + gp.player2.jumpCount, 10, 68);
		
	}

}
