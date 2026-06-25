package it.unibo.shoot.model;
import java.awt.image.BufferedImage;
import java.util.Random;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;

public class Spawner {

    final private Handler handler;
    final private SpriteSheet ss;
    final private Random r = new Random();
    private int timer = 0;
    final private BufferedImage map;
    final private LevelManager levelManager;

    public Spawner(Handler handler, SpriteSheet ss, BufferedImage map, LevelManager levelManager){
        this.handler = handler;
        this.ss = ss;
        this.map = map;
        this.levelManager = levelManager;
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
            x = r.nextInt(map.getWidth()) * Constants.TILE_SIZE;                     //fa spawnare un nemico a caso nella mappa, se il punto di spawn non ? sopra un muro
            y = r.nextInt(map.getHeight()) * Constants.TILE_SIZE;
            maxAttempts--;
        } while (isWall(x,y) && maxAttempts > 0);

        if(isWall(x,y)){
            return;                                                                 //se dopo 30 tentativi non riesce, salta lo spawn
        }

        int enemyType = r.nextInt(3);                                       //spawna uno dei 3 nemici a caso
        switch (enemyType) {
            case 0:
                handler.addObject(new Enemy1(x, y, ID.Enemy, ss, handler, levelManager));
                break;
        
            case 1:
                handler.addObject(new Enemy2(x, y, ID.Enemy, ss, handler, levelManager));
                break;
            
            case 2:
                handler.addObject(new Enemy3(x, y, ID.Enemy, ss, handler, levelManager));
                break;
        }
    }

    private boolean isWall(int x, int y){                                                       //serve per lo spawn dei nemici, controlla se il tile ? rosso = muro
        int mapX = x / Constants.TILE_SIZE;
        int mapY = y / Constants.TILE_SIZE;

        for(int dX = -1; dX <= 1; dX++) {
            for(int dY = -1; dY <= 1; dY++){
                int checkX = mapX + dX;
                int checkY = mapY + dY;

                if (checkX < 0 || checkY < 0 || checkX >= map.getWidth() || checkY >= map.getHeight()) {
                    return true;
                }

                int pixel = map.getRGB(checkX, checkY);
                int red   = (pixel >> 16) & 0xff;
                if(red == 255){
                    return true;
                }
            }
        }
        return false;
    }

}