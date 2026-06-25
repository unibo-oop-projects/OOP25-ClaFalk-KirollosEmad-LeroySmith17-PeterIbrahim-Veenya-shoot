package it.unibo.shoot.GameObjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.ID;

class BulletTest {
    private GameObject fakeBlock(int x, int y) {
        return new GameObject(x, y, ID.Block, (SpriteSheet) null) {
            @Override public void tick() {}
            @Override public void render(Graphics g) {}
            @Override public Rectangle getBounds() { return new Rectangle(x, y, 32, 32); }
        };
    }

    @Test
    void testProiettileInMovimento() {
        Handler handler = new Handler();
        Bullet bullet = new Bullet(100, 100, ID.Bullet, handler, 200, 100, null, 50);
        handler.addObject(bullet);

        float x1 = bullet.getX();
        bullet.tick();
        Assertions.assertTrue(bullet.getX() > x1);
    }

    @Test
    void testProiettileColpisceBlocco()  {
        Handler handler = new Handler();
        Bullet bullet = new Bullet(100, 100, ID.Bullet, handler, 200, 100, null, 50);
        handler.addObject(bullet);
        handler.addObject(fakeBlock(100, 100));
        bullet.tick();
        Assertions.assertFalse(handler.getObjects().contains(bullet));
    }
}