package it.unibo.shoot.view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.shoot.GameObjects.GameObject;
import it.unibo.shoot.util.Constants;
import java.awt.Graphics;
import java.awt.Rectangle;

class CameraTest {

    private GameObject fakeObject(int x, int y) {
        return new GameObject(x, y, null, null) {
            @Override public void tick() {}
            @Override public void render(Graphics g) {}
            @Override public Rectangle getBounds() { return new Rectangle(x, y, 32, 32); }
        };
    }

    @Test
    void testCentratasuOggetto() {
        Camera cam = new Camera(0, 0);
        GameObject obj = fakeObject(500, 300);
        cam.tick(obj);
        assertEquals(500 - Constants.SCREEN_WIDTH / 2, cam.getX(), 1.0f);
        assertEquals(300 - Constants.SCREEN_HEIGHT / 2, cam.getY(), 1.0f);
    }

    @Test
    void testClampSinistraAlto() {
        Camera cam = new Camera(0, 0);
        // oggetto vicino all'angolo in alto a sinistra → camera non va sotto 0
        cam.tick(fakeObject(0, 0));
        assertEquals(0, cam.getX(), 0.0f);
        assertEquals(0, cam.getY(), 0.0f);
    }

    @Test
    void testClampDestraBasso() {
        Camera cam = new Camera(0, 0);
        // oggetto oltre i bordi del mondo → camera bloccata al massimo
        cam.tick(fakeObject(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        assertEquals(Constants.WORLD_WIDTH - Constants.SCREEN_WIDTH, cam.getX(), 1.0f);
        assertEquals(Constants.WORLD_HEIGHT - Constants.SCREEN_HEIGHT, cam.getY(), 1.0f);
    }

    @Test
    void testSetterGetterX() {
        Camera cam = new Camera(0, 0);
        cam.setX(123.5f);
        assertEquals(123.5f, cam.getX());
    }

    @Test
    void testSetterGetterY() {
        Camera cam = new Camera(0, 0);
        cam.setY(456.7f);
        assertEquals(456.7f, cam.getY());
    }
}