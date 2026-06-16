package it.unibo.shoot.model;

import java.awt.image.BufferedImage;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;

public class BossSpawner {

    private Handler handler;
    private SpriteSheet ss;
    private BufferedImage crate_tex;
    private LevelManager levelManager;
    private int timer = 12000;
    private boolean bossAlive = false;

    private static final int BOSS_SPAWN_X = 32 * Constants.TILE_SIZE;
    private static final int BOSS_SPAWN_Y = 33 * Constants.TILE_SIZE;
    private static final int BOSS_TIMER = 0;

    public BossSpawner(Handler handler, SpriteSheet ss, BufferedImage crate_tex, LevelManager levelManager){
        this.handler = handler;
        this.ss = ss;
        this.crate_tex = crate_tex;
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
        Boss boss = new Boss(BOSS_SPAWN_X, BOSS_SPAWN_Y, ID.Boss, ss, handler, levelManager, crate_tex, this);
        handler.addObject(boss);
        bossAlive = true;
    }

    public void isBossDead() {
        bossAlive = false;
    }
}