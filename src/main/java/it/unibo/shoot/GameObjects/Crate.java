package it.unibo.shoot.GameObjects;

import it.unibo.shoot.model.ID;
import it.unibo.shoot.util.Constants;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


 //Rappresenta un oggetto di gioco di tipo Crate.
public class Crate extends GameObject {
    
    /** L'immagine texture utilizzata per il rendering grafico del crate. */
    private BufferedImage crate_image;

    /**
     * Costruttore per la creazione di una nuova istanza di Crate.
     * @param x   la coordinata X iniziale nel mondo di gioco.
     * @param y   la coordinata Y iniziale nel mondo di gioco.
     * @param id  l'identificatore univoco del tipo di oggetto (es. ID.Crate).
     * @param img la texture {@link BufferedImage} da associare al Crate.
     */
    public Crate(int x, int y, ID id, BufferedImage img) {
        // Invoca il costruttore della classe base GameObject. 
        // Viene passato 'null' per lo SpriteSheet in quanto questo oggetto utilizza un'immagine statica singola.
        super(x, y, id, null);
        this.crate_image = img;
    }


    @Override
    public Rectangle getBounds() {
        // La hitbox viene calcolata dinamicamente usando la costante globale della dimensione dei tasselli (TILE_SIZE).
        return new Rectangle(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    /**
     * Gestisce il rendering grafico della cassa all'interno del contesto software fornito.
     * * @param g il contesto grafico {@link Graphics} su cui disegnare l'oggetto.
     */
    @Override
    public void render(Graphics g) {
        // Verifica preventiva per evitare NullPointerException nel caso in cui la texture non sia caricata correttamente
        if (crate_image != null) {
            g.drawImage(crate_image, x, y, null);
        }
    }

    @Override
    public void tick() {
        // Nessuna logica di aggiornamento periodico richiesta per questo oggetto statico.
    }
}
