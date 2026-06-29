package it.unibo.shoot.model;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import it.unibo.shoot.GameObjects.Bullet;
import it.unibo.shoot.GameObjects.GameObject;
import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;

public class Enemy extends GameObject{
    
    private static final int ENEMYWH = 32;
    private static final int ENEMY_BORDER = 4;
    private static final int ENEMY_BIG_BOUNDS = ENEMYWH + ENEMY_BORDER * 2;     //40
    private static final int FRAME_MAX = 3;
    private static final int FRAME_DELAY_MAX = 10;
    private final LevelManager levelManager;
    protected Handler handler;
    protected float speed;                                                      //velocita nemico
    protected int hp;                                                           //vita del nemico
    protected int damage = 10;
    protected int xpValue = 10;
    protected int renderSize = 32;
    protected int COL_OFFSET = 0;
    protected enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    protected Direction dir = Direction.DOWN;
    protected BufferedImage enemy_ss;
    protected int frame = 0;
    protected int frameDelay = 0;

    public Enemy (int x, int y, ID id,SpriteSheet ss, Handler handler, float speed, LevelManager levelManager){
        super(x, y, id, ss);
        this.handler = handler;
        this.ss = ss;
        this.speed = speed;
        this.levelManager = levelManager;
    }

    @Override
    public void tick()  {

        int oldX = x;
        int oldY = y;
        x += velX;
        y += velY;

        updateDirection();                                                                              //modifica il "verso" in cui il nemico guarda

        GameObject player = findPlayer();

        if (player != null && getBounds().intersects(player.getBounds())) {                             //controlla se il nemico colpisce il player
                ((Player) player).takeDamage(damage);                                                   //se colpisce, il player perde hp in base al danno del nemico
            }

        boolean collision = handler.object.stream()
            .anyMatch(obj -> obj.getId() == ID.Block && getBoundsBig().intersects(obj.getBounds()));    //controlla se il nemico tocca un muro

        handler.object.stream()
            .filter(obj -> obj.getId() == ID.Bullet && getBounds().intersects(obj.getBounds()))         //controlla se è colpito da un proiettile
            .findFirst().ifPresent(obj -> {
                hp -= ((Bullet) obj).getDamage();
                handler.removeObject(obj);
            });

        if (collision) {                                                                                //check se il nemico tocca un muro
            resolveCollision(oldX, oldY);
        }

        clampToWorld();                                                                                 //controlla se il nemico è uscito dai bordi della mappa
        onDeath();                                                                                      //si attiva quando il nemico muore
        moveToPlayer(player);                                                                           //si avvicina al giocatore
        updateAnimation();                                                                              //scorre i frame per le animazioni

    }

    private void updateDirection() {
        if (Math.abs(velX) > Math.abs(velY)) {                                  //se la velocita laterale > verticale, controlla se il modello guarda a sinistra o destra
            if (velX > 0) dir = Direction.RIGHT;                                // velX positiva, guarda a DESTRA
            else dir = Direction.LEFT;
        } 
        else {
            if (velY < 0) dir = Direction.DOWN;                                 //vel laterale < verticale, se velY positiva, guarda in BASSO
            else dir = Direction.UP;
        }
    }

    private void onDeath() {
        if (hp <= 0){
                if(levelManager != null){
                    levelManager.addXP(xpValue);
                }
                bossDeath();                                                    //azione speciale che si attiva solo se il nemico che muore è un Boss
                handler.removeObject(this);                                     //rimuove il nemico eliminato
            }
        }

        private void resolveCollision(int oldX, int oldY) {
                x = oldX;                                                       //salva le vecchie coordinate
                y = oldY;
                x += velX;                                                      //testa se è uscito dai bordi sull'asse X
                if (collidesWithBlock()) {
                    x = oldX;
                }
                y += velY;                                                      //testa se è uscito dai bordi sull'asse Y
                if (collidesWithBlock()) {
                    y = oldY;
                }
        }

    private boolean collidesWithBlock() {                                       //check per la collisione con un muro
                        return handler.object.stream()
                        .anyMatch(obj -> obj.getId() == ID.Block && getBoundsBig().intersects(obj.getBounds()));
                }

    private void clampToWorld() {
        if (x < 0) {
            x = 0; velX = 0;
        }
        if (y < 0) {
            y = 0; velY = 0;
        }
        if (x > Constants.WORLD_WIDTH - renderSize) {
            x = Constants.WORLD_WIDTH - renderSize; velX = 0;
        }
        if (y > Constants.WORLD_HEIGHT - renderSize) {
            y = Constants.WORLD_HEIGHT - renderSize; velY = 0;
        }
    }

    private GameObject findPlayer() {
        return handler.object.stream()
                .filter(obj -> obj.getId() == ID.Player)
                .findFirst()
                .orElse(null);
    }

    private void moveToPlayer(GameObject player) {
        if (player != null) {
            float diffX = player.getX() - x;
            float diffY = player.getY() - y;

            float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));

            if (distance !=0) {
                velX = (diffX / distance) * speed;                                //si avvicina verso il player a velocita costante
                velY = (diffY / distance) * speed;
            }
        }
    }

    private void updateAnimation() {
        frameDelay++;
        if (frameDelay >= FRAME_DELAY_MAX) {
            frameDelay = 0;
            frame++;                                                              //ogni 10 tick avanza di un frame
            if (frame >= FRAME_MAX) {
                frame = 0;                                                        //ritorna a frame 0 dopo aver eseguito completamente frame 2
            }
        }
    }

    @Override
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
        }

        enemy_ss = ss.grabImage(COL_OFFSET + frame, row, 16, 16);
        g.drawImage(enemy_ss, x, y, renderSize, renderSize, null);     //prende lo spritesheet del nemico
    }

    @Override
    public Rectangle getBounds() {                                              //hitbox del nemico
        return new Rectangle(x, y, ENEMYWH, ENEMYWH);
    }

    public Rectangle getBoundsBig() {                                           //"area" di collisione del nemico per non finire nel muro
        return new Rectangle(x - ENEMY_BORDER, y - ENEMY_BORDER, ENEMY_BIG_BOUNDS, ENEMY_BIG_BOUNDS);
    }

    protected void bossDeath() {
    }

}