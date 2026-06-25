package it.unibo.shoot.model;

import it.unibo.shoot.loader.SpriteSheet;

public class Enemy3 extends Enemy{

    public Enemy3(int x, int y, ID id, SpriteSheet ss, Handler handler, LevelManager levelManager) {
        super(x, y, id, ss, handler, 1.2f, levelManager);
        this.COL_OFFSET = 6;
        this.hp = 300;
        this.damage = 50;
        this.xpValue = 30;
    }
}