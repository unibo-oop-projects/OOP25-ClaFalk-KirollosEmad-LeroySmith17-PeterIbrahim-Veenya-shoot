package it.unibo.shoot.GameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.ID;
public class Bullet extends GameObject {

    private Handler handler;
    private int damage = 50;

    public Bullet(int x, int y, ID id, Handler handler, int mx, int my, SpriteSheet ss, int damage) {
        super(x, y, id, ss);
        this.handler = handler;
        this.damage = damage;

        // Logica semplice: calcoliamo la direzione verso il mouse (mx, my)
        // Per ora facciamo una velocità fissa verso destra/sinistra/su/giù 
        // basata su dove guarda il player, o verso il mouse
        float speed = 10;
        float diffX = mx - x;
        float diffY = my - y;
        float distance = (float) Math.sqrt((x-mx)*(x-mx) + (y-my)*(y-my));

        this.velX = (diffX / distance) * speed;
        this.velY = (diffY / distance) * speed;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        // 1. AUTO-DISTRUZIONE: Se il proiettile va fuori dai limiti del mondo, lo eliminiamo
        // (Imposta questi numeri in base alla grandezza della tua mappa)
        if (x < -1000 || x > 5000 || y < -1000 || y > 5000) {
            handler.removeObject(this);
            return; // Ferma subito l'esecuzione di questo tick!
        }

        // 2. COLLISIONI: Se tocca un blocco, sparisce
        for (int i = 0; i < handler.getObjects().size(); i++) {
            GameObject tempObject = handler.getObjects().get(i);
            
            if (tempObject.getId() == ID.Block) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(this); // Distruggi il proiettile
                    break; // FONDAMENTALE! Esci dal ciclo for, non controllare altri muri.
                }
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillOval((int)x, (int)y, 8, 8); // Un piccolo cerchio
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, 8, 8);
    }
}