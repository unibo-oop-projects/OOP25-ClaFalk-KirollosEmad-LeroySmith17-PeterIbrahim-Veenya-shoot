package it.unibo.shoot.model;

import java.awt.image.BufferedImage;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;

public class BossSpawner {

    final private Handler handler;
    final private SpriteSheet ss;
    final private BufferedImage crate_image;
    final private LevelManager levelManager;
    private int timer = 0;
    private boolean bossAlive = false;

    private static final int BOSS_SPAWN_X = 32 * Constants.TILE_SIZE;
    private static final int BOSS_SPAWN_Y = 33 * Constants.TILE_SIZE;
    private static final int BOSS_TIMER = 1200;

    public BossSpawner(Handler handler, SpriteSheet ss, BufferedImage crate_image, LevelManager levelManager){
        this.handler = handler;
        this.ss = ss;
        this.crate_image = crate_image;
        this.levelManager = levelManager;
    }

    public void tick() {
        if (bossAlive) {
            return;
        }

        timer++;
        if (timer >= BOSS_TIMER) {
            timer = 0;
            spawnBoss();
        }
    }

    public void spawnBoss() {
        handler.addObject(new Boss(BOSS_SPAWN_X, BOSS_SPAWN_Y, ID.Boss, ss, handler, levelManager, crate_image, this));
        bossAlive = true;
    }

    public void onBossDeath() {
        bossAlive = false;
    }
}