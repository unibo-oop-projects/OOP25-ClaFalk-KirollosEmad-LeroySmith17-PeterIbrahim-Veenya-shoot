package it.unibo.shoot.model;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.unibo.shoot.loader.BufferedImageLoader;
import it.unibo.shoot.loader.SpriteSheet;

class BossSpawnerTest {

    private SpriteSheet realSpriteSheet() {
        BufferedImageLoader loader = new BufferedImageLoader();
        return new SpriteSheet(loader.loadImage("/sprites/enemies.png"));
    }

    private BufferedImage realCrate() {
        BufferedImageLoader loader = new BufferedImageLoader();
        return loader.loadImage("/object/crate.png");
    }

    @Test
    void testSpawnBoss() {
        Handler handler = new Handler();
        BossSpawner bossSpawner = new BossSpawner(handler, realSpriteSheet(), realCrate(), new LevelManager(null));
        for (int i = 0; i < 1200; i++) {
            bossSpawner.tick();
        }
        Assertions.assertFalse(handler.getObjects().isEmpty());
    }

    @Test
    void testBossNonSpawna(){
        Handler handler = new Handler();
        BossSpawner bossSpawner = new BossSpawner(handler, realSpriteSheet(), realCrate(), new LevelManager(null));
        for (int i = 0; i < 1199; i++) {
            bossSpawner.tick();
        }
        Assertions.assertTrue(handler.getObjects().isEmpty());
    }

    @Test
    void testMorteBossECassa() {
        Handler handler = new Handler();
        BossSpawner bossSpawner = new BossSpawner(handler, realSpriteSheet(), realCrate(), new LevelManager(null));
        bossSpawner.spawnBoss();

        Boss boss = (Boss) handler.getObjects().stream().filter(o -> o instanceof Boss).findFirst().get();
        boss.hp = 0;
        boss.tick();

        Assertions.assertFalse(handler.getObjects().stream().anyMatch(o -> o instanceof Boss));
        Assertions.assertTrue(handler.getObjects().stream().anyMatch(o -> o.getId() == ID.Crate));
    }
    // Test per verificare che il Boss venga spawnato correttamente dopo il timer
    // Test per verificare che il Boss venga rimosso correttamente alla morte
    // Test per verificare che il timer si resetti dopo lo spawn del Boss
}