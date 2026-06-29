package it.unibo.shoot.model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import it.unibo.shoot.GameObjects.Crate;
import it.unibo.shoot.loader.SpriteSheet;

public class Boss extends Enemy {

    private static final int BOSSWH = 64;
    private static final int BOSS_BORDER = 4;
    private static final int BOSS_BIG_BOUNDS = BOSSWH + BOSS_BORDER * 2;
    private final BossSpawner bossSpawner;
    private final BufferedImage crate_image;

    public Boss(int x, int y, ID id, SpriteSheet ss, Handler handler, LevelManager levelManager, BufferedImage crate_image, BossSpawner bossSpawner) {
        super(x, y, id, ss, handler, 1.0f, levelManager);
        this.COL_OFFSET = 0;
        this.hp = 300;
        this.damage = 40;
        this.xpValue = 100;
        this.renderSize = BOSSWH;
        this.crate_image = crate_image;
        this.bossSpawner = bossSpawner;
    }

    @Override
    protected void bossDeath() {
        handler.addObject(new Crate(x, y, ID.Crate, crate_image));
        bossSpawner.onBossDeath();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, BOSSWH, BOSSWH);
    }

    @Override
    public Rectangle getBoundsBig() {
        return new Rectangle(x - BOSS_BORDER, y - BOSS_BORDER, BOSS_BIG_BOUNDS, BOSS_BIG_BOUNDS);
    }
}