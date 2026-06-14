package it.unibo.shoot.model;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.GameObjects.GameObject;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.GameObjects.Bullet;

public class Enemy extends GameObject{

    private Handler handler;       
    private LevelManager levelManager;                       
    protected float speed;                                  //velocita nemico
    protected int hp;                                       //vita del nemico
    protected BufferedImage enemy_ss;
    protected enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    protected Direction dir = Direction.DOWN;
    protected int frame = 0;
    protected int frameDelay = 0;
    protected int COL_OFFSET = 0;
    protected int damage = 10;
    protected int xpValue = 10;

    public Enemy (int x, int y, ID id,SpriteSheet ss, Handler handler, float speed, LevelManager levelManager){
        super(x, y, id, ss);
        this.handler = handler;
        this.ss = ss;
        this.speed = speed;
        this.levelManager = levelManager;
    }


    public void tick()  {

        boolean collision = false;
        GameObject player = null;
        x += velX;
        y += velY;

        if (Math.abs(velX) > Math.abs(velY)) {                                  //se la velocita laterale > verticale, controlla se il modello guarda a sinistra o destra
            if (velX > 0) dir = Direction.RIGHT;                                // velX positiva, guarda a DESTRA
            else dir = Direction.LEFT;
        } 
        else {
            if (velY < 0) dir = Direction.DOWN;                                 //vel laterale < verticale, se velY positiva, guarda in BASSO
            else dir = Direction.UP;
        }

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Player) {                               //trova il player
                player = tempObject;
                if(getBounds().intersects(tempObject.getBounds())) {
                    ((Player) tempObject).takeDamage(damage);
                }
            }
        
            if (tempObject.getId() == ID.Block) {
                if (getBoundsBig().intersects(tempObject.getBounds())){          //se toccano un muro vengono rispediti indietro (non si "infila" nel muro)
                    x -= velX;
                    y -= velY;
                    
                    x += velX;
                    if (getBoundsBig().intersects(tempObject.getBounds())) {
                        x -= velX;
                    }

                    y += velY;
                    if (getBoundsBig().intersects(tempObject.getBounds())) {
                        y -= velY;
                    }

                    if (getBoundsBig().intersects(tempObject.getBounds())) {
                        x += (Math.random() > 0.5 ? 1 : -1) *2;
                        y += (Math.random() > 0.5 ? 1 : -1) *2;
                    }
                    
                    collision = true;
                }
            }

            if (tempObject.getId() == ID.Bullet) {                              //controlla se il nemico è colpito da un proiettile
                if (getBounds().intersects(tempObject.getBounds())){
                    hp -= ((Bullet) tempObject).getDamage();                               //se colpito, perde hp in base al danno del proiettile
                    handler.removeObject(tempObject);
                }
            }
        }

            if (hp <= 0){
                if(levelManager != null){
                    levelManager.addXP(xpValue);
                }
                 handler.removeObject(this);                                    //rimuove il nemico eliminato
            }
            

        if (player != null && !collision) {                                      //se non collide con un muro, si avvicina al player
            float diffX = player.getX() - x;
            float diffY = player.getY() - y;

            float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));

            if (distance !=0) {
                velX = (diffX / distance) * speed;                                //si avvicina verso il player a velocita costante
                velY = (diffY / distance) * speed;
            }
        }

        frameDelay++;
        if (frameDelay >= 10) {
            frameDelay = 0;
            frame++;                                                              //ogni 10 tick avanza di un frame
            if (frame >= 3) {
                frame = 0;                                                        //ritorna a frame 0 dopo aver eseguito completamente frame 2
            }
        }

    }

    public void render(Graphics g) {
        int row;
        switch (dir) {                                                             //switch per cambiare il "verso" del nemico in base a dove guarda
            case UP:
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
        g.drawImage(enemy_ss, x, y, 32, 32, null);     //prende lo spritesheet del nemico
    }

    public Rectangle getBounds() {                                              //hitbox del nemico
        return new Rectangle(x, y, 32, 32);
    }

    public Rectangle getBoundsBig() {                                           //"area" di collisione del nemico per non finire nel muro
        return new Rectangle(x-4, y-4, 40, 40);
    }

}