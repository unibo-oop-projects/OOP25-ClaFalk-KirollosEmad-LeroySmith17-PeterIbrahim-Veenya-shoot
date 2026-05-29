package it.unibo.shoot.model;

import it.unibo.shoot.loader.SpriteSheet;

public class Enemy1 extends Enemy{

    public Enemy1(int x, int y, ID id, SpriteSheet ss, Handler handler, LevelManager levelManager) {
        super(x, y, id, ss, handler, 2.5f, levelManager);
        this.hp = 100;
        this.xpValue = 10; // Gives 10 XP on death  
        }
}