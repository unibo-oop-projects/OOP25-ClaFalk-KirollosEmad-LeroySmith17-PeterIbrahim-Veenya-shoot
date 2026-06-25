package it.unibo.shoot.GameObjects;

import it.unibo.shoot.model.ID;
import it.unibo.shoot.loader.SpriteSheet;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class Block extends GameObject{

    private BufferedImage block_tile;

    public Block(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);
        block_tile = ss.grabImage(0, 0);
    }
    public void tick(){

    }
    public void render(Graphics g){
       g.drawImage(block_tile, x, y, null);
        
    }public Rectangle getBounds(){
        return new Rectangle(x,y,32,32);
        
    }

}
 