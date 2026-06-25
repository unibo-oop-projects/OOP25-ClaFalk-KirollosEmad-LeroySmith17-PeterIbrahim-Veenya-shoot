package it.unibo.shoot.model;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.unibo.shoot.GameObjects.GameObject;
import it.unibo.shoot.loader.SpriteSheet;

class EnemyTest {
    private Enemy FakeEnemy(int x, int y, Handler handler, LevelManager levelManager) {
        return new Enemy (x, y, ID.Enemy, null, handler, 1.0f, levelManager) {
            @Override public void render(Graphics g) {}
        };
    }

    private GameObject fakePlayer(int x, int y) {
        return new GameObject(x, y, ID.Player, (SpriteSheet) null) {
            @Override public void tick() {}
            @Override public void render(Graphics g) {}
            @Override public Rectangle getBounds() { return new Rectangle(x, y, 32, 32); 
            }
        };
    }


    @Test
    void TestAvvicinamentoNemico() {
        Handler handler = new Handler();
        LevelManager levelManager = new LevelManager(null);
        Enemy enemy = FakeEnemy(100, 100, handler, levelManager);
        handler.addObject(enemy);
        handler.addObject(fakePlayer(200,100));
        enemy.tick();
        float x1 = enemy.getX();
        enemy.tick();

        Assertions.assertTrue(enemy.getX() > x1);
    }

    @Test
    void testNemicoUcciso() {
        Handler handler = new Handler();
        Enemy enemy = FakeEnemy(100, 100, handler, new LevelManager(null));
        handler.addObject(enemy);
        enemy.hp = 0;
        enemy.tick();
        Assertions.assertFalse(handler.getObjects().contains(enemy));
    }

    @Test
    void testXPAggiunti() {
        Handler handler = new Handler();
        LevelManager levelManager = new LevelManager(null);
        Enemy enemy = FakeEnemy(100, 100, handler, levelManager);
        enemy.xpValue = 10;
        handler.addObject(enemy);
        enemy.hp = 0;
        enemy.tick();
        Assertions.assertEquals(10, levelManager.getCurrentXP());
    }

    @Test
    void testNemicoInVitaNonRimosso() {
        Handler handler = new Handler();
        Enemy enemy = FakeEnemy(100, 100, handler, new LevelManager(null));
        handler.addObject(fakePlayer(200, 100));
        handler.addObject(enemy);
        enemy.hp = 100;
        enemy.tick();
        Assertions.assertTrue(handler.getObjects().contains(enemy));
    }
    
}
