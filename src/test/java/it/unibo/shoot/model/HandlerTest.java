package it.unibo.shoot.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.shoot.GameObjects.GameObject;
import java.awt.Graphics;
import java.awt.Rectangle;

class HandlerTest {

    // Helper: crea un GameObject finto con l'ID voluto
    private GameObject fakeObject(ID id) {
        return new GameObject(0, 0, id, null) {
            @Override public void tick() {}
            @Override public void render(Graphics g) {}
            @Override public Rectangle getBounds() { return new Rectangle(0, 0, 32, 32); }
        };
    }

    @Test
    void testAddObject() {
        Handler h = new Handler();
        h.addObject(fakeObject(ID.Crate));
        assertEquals(1, h.getObjects().size());
    }

    @Test
    void testRemoveObject() {
        Handler h = new Handler();
        GameObject obj = fakeObject(ID.Crate);
        h.addObject(obj);
        h.removeObject(obj);
        assertEquals(0, h.getObjects().size());
    }

    @Test
    void testGetPlayer_trovato() {
        Handler h = new Handler();
        GameObject player = fakeObject(ID.Player);
        h.addObject(player);
        assertEquals(player, h.getPlayer());
    }

    @Test
    void testGetPlayer_nonPresente() {
        Handler h = new Handler();
        assertNull(h.getPlayer());
    }

    @Test
    void testGetPlayer_restituisceIlPrimo() {
        Handler h = new Handler();
        GameObject p1 = fakeObject(ID.Player);
        GameObject p2 = fakeObject(ID.Player);
        h.addObject(p1);
        h.addObject(p2);
        assertEquals(p1, h.getPlayer());
    }

    @Test
    void testClearAllObjects() {
        Handler h = new Handler();
        h.addObject(fakeObject(ID.Crate));
        h.addObject(fakeObject(ID.Enemy));
        h.clearAllObjects();
        assertTrue(h.getObjects().isEmpty());
    }

    @Test
    void testListaInizialmenteVuota() {
        Handler h = new Handler();
        assertTrue(h.getObjects().isEmpty());
    }
}