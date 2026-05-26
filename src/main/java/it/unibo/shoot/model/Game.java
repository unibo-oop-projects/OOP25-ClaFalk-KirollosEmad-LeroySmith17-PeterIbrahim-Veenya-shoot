package it.unibo.shoot.model;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import it.unibo.shoot.view.Window;
import it.unibo.shoot.controller.MouseInput;
import it.unibo.shoot.loader.*;  
import it.unibo.shoot.model.block.Block;
import it.unibo.shoot.view.Camera;
import it.unibo.shoot.util.Constants;

public class Game extends Canvas implements Runnable {

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private Spawner spawner;
    
    // Variabili di classe (visibili a tutti i metodi)
    private SpriteSheet tile_ss;
    private SpriteSheet player_ss;
    private SpriteSheet enemy_ss;

    private BufferedImage level = null;
    private BufferedImage floor = null;
    private BufferedImage block = null;

    int width = Constants.SCREEN_WIDTH;
    int height = Constants.SCREEN_HEIGHT;
    String title = Constants.TITLE;
    // Impostiamo il MENU come stato iniziale all'avvio del gioco
public static STATE gameState = STATE.GAME;

    public Game() {
        // 1. Inizializza Handler e Camera
        handler = new Handler();
        camera = new Camera(0, 0);

        // 2. Carica Immagini e SpriteSheets
        // 2. Carica Immagini e SpriteSheets
BufferedImageLoader loader = new BufferedImageLoader();
level = loader.loadImage("/maps/map1.png");

// CORREZIONE QUI: Usa il loader per trasformare il testo in un'immagine vera e propria
tile_ss = new SpriteSheet(loader.loadImage("/tiles/tileset.png"));
player_ss = new SpriteSheet(loader.loadImage("/sprites/player.png"));
enemy_ss = new SpriteSheet(loader.loadImage("/sprites/enemies.png"));
        
       floor = tile_ss.grabImage(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        block = tile_ss.grabImage(1, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

        // 3. Crea il mondo
        loadLevel(level);
        spawner = new Spawner(handler, enemy_ss, level);

        // 4. Crea la finestra
        new Window(width, height, title, this);
        this.addMouseListener(new MouseInput(handler, camera));
        // 5. Avvia il gioco
        start();
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta --;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
    if (gameState == STATE.GAME) {
        handler.tick();
        if (spawner != null) {
                spawner.tick();
            }
        camera.tick(handler.getPlayer()); // Supponendo che tu passi il player alla camera
    } else if (gameState == STATE.MENU) {
        // Qui aggiornerai la logica del menu di Peter (se necessaria)
    } else if (gameState == STATE.GAME_OVER) {
        // La fisica è completamente congelata. Nessun movimento, nessun proiettile.
    }
}

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // 1. PULIZIA ASSOLUTA: Questo deve avvenire PRIMA di muovere la telecamera
        // 1. PULIZIA ASSOLUTA: Sostituisci Color.BLACK con il grigio chiaro
    g.setColor(Color.LIGHT_GRAY); // <--- CAMBIA QUESTO! (Oppure Color.GRAY)
    g.fillRect(0, 0, 1000, 563);// Sostituisci con le dimensioni reali della tua finestra

        // 2. SMISTAMENTO DEGLI STATI
        if (gameState == STATE.GAME) {
            
            // FONDAMENTALE: La telecamera si muove QUI, solo dentro lo stato GAME
            g.translate((int)-camera.getX(), (int)-camera.getY());
            
            // Disegna tutto il mondo, la mappa, i player e i nemici
            handler.render(g);
            
            // FONDAMENTALE: La telecamera torna indietro. 
            // Se non lo fai, Peter non potrà disegnare l'HUD fermo sullo schermo!
            g.translate((int)camera.getX(), (int)camera.getY());
            
        } else if (gameState == STATE.MENU) {
            g.setColor(Color.WHITE);
            g.drawString("MENU PRINCIPALE - Clicca per giocare", 200, 200);
            
        } else if (gameState == STATE.GAME_OVER) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER - SEI MORTO", 200, 200);
        }

        g.dispose();
        bs.show();
    }

    private void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255) {
                    handler.addObject(new Block(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, ID.Block, tile_ss));
                }

                if (blue == 255) {
                    // Creiamo il Player passando player_ss
                    handler.addObject(new Player(xx*32, yy*32, ID.Player, this, player_ss, handler));
                }

                if (green == 255) {                                 //creo i nemici base
                    handler.addObject(new Enemy1(xx * Constants.TILE_SIZE, yy * Constants.TILE_SIZE, ID.Enemy, enemy_ss, handler));
                }
            }
        }
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}