package it.unibo.shoot.model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import it.unibo.shoot.GameObjects.Crate;
import it.unibo.shoot.loader.SpriteSheet;

public class Boss extends Enemy {

    private BossSpawner bossSpawner;
    private BufferedImage crate_image;

    public Boss(int x, int y, ID id, SpriteSheet ss, Handler handler, LevelManager levelManager, BufferedImage crate_image, BossSpawner bossSpawner) {
        super(x, y, id, ss, handler, 0.5f, levelManager);
        this.COL_OFFSET = 0;
        this.hp = 300;
        this.damage = 40;
        this.xpValue = 100;
        this.crate_image = crate_image;
        this.bossSpawner = bossSpawner;
    }

    @Override
    protected void onDeath() {
        handler.addObject(new Crate(x, y, ID.Crate, crate_image));
        bossSpawner.isBossDead();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 64, 64);
    }

    @Override
    public Rectangle getBoundsBig() {
        return new Rectangle(x-4, y-4, 72, 72);
    }

    @Override
    public void tick() {
        super.tick();
    }
    
}