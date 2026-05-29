package it.unibo.shoot.GameObjects;

import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.model.ID;
import it.unibo.shoot.util.Constants; // Added import to use TILE_SIZE dynamically
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Crate extends GameObject {
    private BufferedImage crate_image;

    public Crate(int x, int y, ID id, BufferedImage img) {
        super(x, y, id, null);
        // CORRETTO: Adesso passa le dimensioni corrette della griglia per ritagliare l'immagine
        this.crate_image = img;
    }

    // Cambia in getHitbox() se la classe astratta GameObject dichiara getHitbox() invece di getBounds()
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    @Override
    public void render(Graphics g) {
        if (crate_image != null) {
            g.drawImage(crate_image, x, y, null);
        }
    }

    @Override
    public void tick() {
        // Le casse sono statiche, quindi la logica di aggiornamento è vuota
    }
}