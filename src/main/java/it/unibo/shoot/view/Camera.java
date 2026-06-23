package it.unibo.shoot.view;

import it.unibo.shoot.util.Constants;
import it.unibo.shoot.GameObjects.GameObject;



/**
 * Handles the camera position during the game, it keeps it centered on the player based on it's movements.
 */
public class Camera {
    private float x, y;

    /**
     * Creates a camera at a specified position (x, y).
     * 
     * @param x starting x position
     * @param y starting y position
     */
    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates camera position and keeps it centered on the object.
     * 
     * @param object the focus of the camera's center (usually the player).
     */
    public void tick(GameObject object) {
        x = object.getX() - Constants.SCREEN_WIDTH/2;
        y = object.getY() - Constants.SCREEN_HEIGHT/2;

        // Clamp
        
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > Constants.WORLD_WIDTH - Constants.SCREEN_WIDTH)
            x = Constants.WORLD_WIDTH - Constants.SCREEN_WIDTH;
        if (y > Constants.WORLD_HEIGHT - Constants.SCREEN_HEIGHT)
            y = Constants.WORLD_HEIGHT - Constants.SCREEN_HEIGHT;
    }

    // -------- Getters --------

    /**
     * Returns the camera x position
     * 
     * @return camera x position
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the camera y position
     * 
     * @return camera y position
     */
    public float getY() {
        return y;
    }

    // -------- Setters --------

    /**
     * Sets the x position of the camera
     * 
     * @param x new x position
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y position of the camera
     * 
     * @param y new x position
     */
    public void setY(float y) {
        this.y = y;
    }
}
