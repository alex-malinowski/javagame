package main;

public class Camera {
    public int cameraY; // Vertical offset
    public int screenHeight; // Visible height of the camera
    private final int defaultScreenHeight; // Original screen height

    public Camera(int screenHeight) {
        this.cameraY = 0;
        this.screenHeight = screenHeight;
        this.defaultScreenHeight = screenHeight; // Store the default screen height
    }

    public int getCameraY() {
        return cameraY;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void update(int player1Y, int player2Y) {
        // Determine the higher player
        int higherPlayerY = Math.max(player1Y, player2Y);

        // Update camera position to follow the higher player
        if (higherPlayerY < cameraY + screenHeight / 2) {
            cameraY = higherPlayerY - screenHeight / 2;
        }

        // Check if either player is below the camera view
        int lowerPlayerY = Math.min(player1Y, player2Y);
        if (lowerPlayerY > cameraY + screenHeight) {
            // Zoom out by increasing the screen height
            screenHeight += 50; // Increase the visible area by 50 pixels
        } else if (screenHeight > defaultScreenHeight) {
            // Gradually zoom back in to the default screen height
            screenHeight -= 2; // Decrease the visible area by 2 pixels
            if (screenHeight < defaultScreenHeight) {
                screenHeight = defaultScreenHeight; // Ensure it doesn't go below the default
            }
        }
    }
}