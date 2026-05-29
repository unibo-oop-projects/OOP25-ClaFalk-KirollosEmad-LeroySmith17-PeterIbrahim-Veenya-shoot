package it.unibo.shoot.model;

import it.unibo.shoot.loader.SpriteSheet;

public class Enemy2 extends Enemy{

    public Enemy2(int x, int y, ID id, SpriteSheet ss, Handler handler, LevelManager levelManager) {
        super(x, y, id, ss, handler, 1.5f, levelManager);
        this.COL_OFFSET = 3;
        this.hp = 200;
        this.damage = 25;
        this.xpValue = 20; // Gives 10 XP on death
    }
}