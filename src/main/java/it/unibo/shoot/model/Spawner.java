package it.unibo.shoot.model;
import java.util.Random;
import java.awt.image.BufferedImage;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;

public class Spawner {

    private Handler handler;
    private SpriteSheet ss;
    private Random r = new Random();
    private int timer = 0;
    private BufferedImage map;

    public Spawner(Handler handler, SpriteSheet ss, BufferedImage map){
        this.handler = handler;
        this.ss = ss;
        this.map = map;
    }

    public void tick() {
        timer++;
        if(timer >= 100){                                      //quando il timer raggiunge il tempo si azzera e fa spawnare un nemico
            timer = 0;
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        int x = 0;
        int y = 0;
        int maxAttempts = 30;

        do {
            x = r.nextInt(map.getWidth()) * Constants.TILE_SIZE;                     //fa spawnare un nemico a caso nella mappa, se il punto di spawn non è sopra un muro
            y = r.nextInt(map.getHeight()) * Constants.TILE_SIZE;
            maxAttempts--;
        } while (isWall(x,y) && maxAttempts > 0);

        if(isWall(x,y)){
            return;                                                                 //se dopo 30 tentativi non riesce, salta lo spawn
        };

        int enemyType = r.nextInt(3);                                       //spawna uno dei 3 nemici a caso
        switch (enemyType) {
            case 0:
                handler.addObject(new Enemy1(x, y, ID.Enemy, ss, handler));
                break;
        
            case 1:
                handler.addObject(new Enemy2(x, y, ID.Enemy, ss, handler));
                break;
            
            case 2:
                handler.addObject(new Enemy3(x, y, ID.Enemy, ss, handler));
                break;
        }
    }

    private boolean isWall(int x, int y){                                                       //serve per lo spawn dei nemici, controlla se il tile è rosso = muro
        int mapX = x / Constants.TILE_SIZE;
        int mapY = y / Constants.TILE_SIZE;

        if (mapX < 0 || mapY < 0 || mapX >= map.getWidth() || mapY >= map.getHeight()) {
            return true;
        }   

    int pixel = map.getRGB(mapX, mapY);
        int red   = (pixel >> 16) & 0xff;

    return red == 255;
    }

}