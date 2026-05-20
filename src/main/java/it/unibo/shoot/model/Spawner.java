package it.unibo.shoot.model;
import java.util.Random;
import it.unibo.shoot.loader.SpriteSheet;

public class Spawner {

    private Handler handler;
    private SpriteSheet ss;
    private Random r = new Random();
    private int timer = 0;

    public Spawner(Handler handler, SpriteSheet ss){
        this.handler = handler;
        this.ss = ss;
    }

    public void tick() {
        timer++;
        if(timer >= 1000){
            timer = 0;
            int x = r.nextInt(1000);
            int Y = r.nextInt(700);
            handler.addObject(new Enemy(x,y,ID.Enemy,ss,handler));
        }
    }
}