package it.unibo.shoot.model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import it.unibo.shoot.loader.SpriteSheet;
import java.util.Random;

public class Enemy extends GameObject{

    private Handler handler;
    private SpriteSheet ss;
    Random r = new Random();                                
    int choose = 0;                                         //scelta del movimento nemico
    int hp = 100;                                           //vita del nemico

    public Enemy (int x, int y, ID id,SpriteSheet ss, Handler handler){
        super(x, y, id, ss);
        this.handler = handler;
        this.ss = ss;
    }

    @Override
    public void tick()  {
        x += velX;
        y += velY;

        //choose = r.nextInt(10);
        GameObject player = null;

        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.Player) {                               //trova il player
                player = tempObject;
            }

            if(tempObject.getId() == ID.Block) {
                if(getBoundsBig().intersects(tempObject.getBounds())){          //se toccano un muro vengono rispediti indietro (non si "infila" nel muro)
                    //x += (velX*3) * -1;
                    //y += (velY*3) * -1;
                    //velX *= -1;
                    //velY *= -1;
                    x -= velX;
                    y -= velY;

                    velX *= -1;
                    velY *= -1;
                
                //}else{                                         //movimento del nemico
                    //velX =(r.nextInt(4 - -4) + -4);
                    //velY =(r.nextInt(4 - -4) + -4);
                }
            }

            


        }


        if(player != null) {
            float diffX = player.getX() - x;
            float diffY = player.getY() - y;

            float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));

            if(distance !=0) {
                float speed = 2.5f;
                velX = (diffX / distance) * speed;
                velY = (diffY / distance) * speed;
            }
        }

        //proiettile del giocatore che tocca il nemico (da ricontrollare)
        if(hp <= 0) {handler.removeObject(this);
        }
    }


    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);                                               //"base" del nemico (per ora e' un rettangolo giallo)
        g.fillRect(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {                                              //"rettangolo" del nemico
        return new Rectangle(x, y, 32, 32);
    }

    public Rectangle getBoundsBig() {                                           //"area" di collisione del nemico (serve per registrare la "hitbox" e non farlo schiantare dentro al muro)
        return new Rectangle(x-16, y-16, 64, 64);
    }

}