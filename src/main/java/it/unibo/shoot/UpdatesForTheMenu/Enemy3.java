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
/*
    @Override
    public void tick(){
        super.tick();

        frameDelay++;
        if (frameDelay >= 10) {
            frameDelay = 0;
            frame++;                                                    //animazione avanza di 1
            if (frame >= 3) {
                frame = 0;                                              //ritorna al frame 1
            }
        }
    }

    @Override
    public void render(Graphics g) {
        int row;
        switch (dir) {
            case DOWN:
                row = 0;
                break;
            case LEFT:  
                row = 1;
                break;
            case RIGHT:
                row = 2;
                break;
            default:
                row = 3;
                break;
        };

        enemy_ss = ss.grabImage(COL_OFFSET + frame, row, 16, 16);
        g.drawImage(enemy_ss, (int)x, (int)y, null);
    }*/
}