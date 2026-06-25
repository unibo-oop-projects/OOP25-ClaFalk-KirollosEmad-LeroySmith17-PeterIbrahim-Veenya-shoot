package it.unibo.shoot.model;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.unibo.shoot.loader.BufferedImageLoader;
import it.unibo.shoot.loader.SpriteSheet;

class SpawnerTest{
    private SpriteSheet realSpriteSheet() {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage image = loader.loadImage("/sprites/enemies.png");
        return new SpriteSheet(image);
    }

    private BufferedImage fakeMap() {
        return new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
    }

    @Test
    void testNessunNemicoSpawnato() {
        Handler handler = new Handler();
        Spawner spawner = new Spawner(handler, realSpriteSheet(), fakeMap(), new LevelManager(null));
        for (int i = 0; i < 99; i++) {
            spawner.tick();
        }
        Assertions.assertTrue(handler.getObjects().isEmpty());
    }

    @Test
    void testSpawn() {
        Handler handler = new Handler();
        Spawner spawner = new Spawner(handler, null, fakeMap(), new LevelManager(null));
        for (int i = 0; i < 100; i++) {
            spawner.tick();
        }
        Assertions.assertFalse(handler.getObjects().isEmpty());
    }
}