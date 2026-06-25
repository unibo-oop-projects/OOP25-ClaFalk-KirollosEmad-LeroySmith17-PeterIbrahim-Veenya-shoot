package it.unibo.shoot.model;

import it.unibo.shoot.GameObjects.GameObject;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Central container for all active GameObject instances in the game.
 * 
 * Manages the game object lifecycle by providing methods to add, remove,
 * update, and render all objects.
 */
public class Handler {

    /**
     * The list of all active game objects.
     */
    public List<GameObject> object = new CopyOnWriteArrayList<>();

    /**
     * Updates the state of all active game objects for the current tick.
     */
    public void tick() {
        object.forEach(GameObject::tick);
    }

    /**
     * Renders all active game objects onto the provided graphics context.
     * 
     * @param g the graphics context to render onto
     */
    public void render(Graphics g) {
        object.forEach(o -> o.render(g));
    }

    /**
     * Adds a game object to the active object list.
     * 
     * @param object the game object to add.
     */
    public void addObject(GameObject object) {
        this.object.add(object);
    }

    /**
     * Removes a game object from the active object list.
     * 
     * @param object the game object to remove.
     */
    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

    /**
     * Returns the list of all active game objects.
     * 
     * @return the list of active game objects.
     */
    public List<GameObject> getObjects() {
        return object;
    }

    /**
     * Returns the player object from the active object list, or null if not found.
     * 
     * @return the GameObject Player, or null if the player is not present.
     */
    public GameObject getPlayer() {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            if (tempObject.getId() == ID.Player) {
                return tempObject; // Trovato! Lo restituisce alla telecamera
            }
        }
        return null;
    }

    /**
     * Removes all game objects from the active object list.
     */
    public void clearAllObjects() {
        this.object.clear();
    }
}