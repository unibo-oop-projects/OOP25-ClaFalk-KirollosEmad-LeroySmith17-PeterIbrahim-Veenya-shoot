package it.unibo.shoot.model;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import it.unibo.shoot.GameObjects.Block;
import it.unibo.shoot.GameObjects.Crate;
import it.unibo.shoot.Upgrades.Upgrade;
import it.unibo.shoot.controller.MouseInput;
import it.unibo.shoot.loader.BufferedImageLoader;
import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.util.Constants;
import it.unibo.shoot.view.Camera;
import it.unibo.shoot.view.Window;

public class Game extends Canvas implements Runnable {
    

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private Spawner spawner;
    private BossSpawner bossSpawner;
    private Player player;
    private LevelManager levelManager;
    public static java.util.List<it.unibo.shoot.Upgrades.Upgrade> currentUpgradeOptions = new java.util.ArrayList<>();
    // Variabili di classe (visibili a tutti i metodi)
    private SpriteSheet tile_ss;
    private SpriteSheet player_ss;
    private SpriteSheet enemy_ss;

    private BufferedImage level = null;
    private BufferedImage floor = null;
    private BufferedImage block = null;
    private BufferedImage crate_tex = null;
    public int ammo = 50 ;
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
crate_tex = loader.loadImage("/object/crate.png");
        
       floor = tile_ss.grabImage(0, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);
        block = tile_ss.grabImage(1, 0, Constants.TILE_SIZE, Constants.TILE_SIZE);

        // 3. Crea il mondo
        levelManager = new LevelManager(null);
        loadLevel(level);
        levelManager.setPlayer(player);
        //LevelManager levelManager = new LevelManager(player);
        spawner = new Spawner(handler, enemy_ss, level, levelManager);
        bossSpawner = new BossSpawner(handler, enemy_ss, crate_tex, levelManager);


        // 4. Crea la finestra
        new Window(width, height, title, this);
        this.addMouseListener(new MouseInput(handler, camera,this));
        
        
    }

    public void restartGame() {
    // 1. Clear out all left-over enemies, blocks, and entities from the previous run
    handler.clearAllObjects();
    
    // 2. Reset status metrics
    this.ammo = 50; 
    
    // 3. Reset the LevelManager to clear experience levels and upgrades
    // We pass null first because the player hasn't been parsed from the image map yet
    this.levelManager = new LevelManager(null);
    
    // 4. Reload the map image completely (this populates handler and instantiates the new player)
    loadLevel(level);
    
    // 5. Rebind the newly created player back into the fresh LevelManager instance
    if (this.player != null) {
        this.levelManager.setPlayer(this.player);
    }
    
    // 6. CRITICAL FIX: Re-instantiate the Spawner so it tracks the NEW levelManager and enemy configurations
    this.spawner = new Spawner(handler, enemy_ss, level, levelManager);
    this.bossSpawner = new BossSpawner(handler, enemy_ss, crate_tex, levelManager);
    
    // 7. Reset the game state back to active game tracking
    Game.gameState = STATE.GAME;
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
        if (bossSpawner != null) {
                bossSpawner.tick();
            }
        if (handler.getPlayer() != null){
              camera.tick((Player) handler.getPlayer()); // Supponendo che tu passi il player alla camera
            }
         
    } else if (gameState == STATE.MENU) {
        // Qui aggiornerai la logica del menu di Peter (se necessaria)
    } else if (gameState == STATE.GAME_OVER) {
        // La fisica è completamente congelata. Nessun movimento, nessun proiettile.
    } else if (gameState == STATE.LEVEL_UP){
        
    }

}

  private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        // 1. PULIZIA ASSOLUTA: Sfondo grigio chiaro
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        // 2. SMISTAMENTO DEGLI STATI
        if (gameState == STATE.GAME || gameState == STATE.LEVEL_UP) {
            
            // Disegna il mondo di gioco (se siamo in LEVEL_UP, rimane visibile sotto congelato)
            g.translate((int)-camera.getX(), (int)-camera.getY());
            handler.render(g);
          
            g.translate((int)camera.getX(), (int)camera.getY());
            Player p = (Player) handler.getPlayer();
            if (p != null) {
                int hp = p.getHealth();
                int maxHp = p.getMaxHealth();
                int currentXP = levelManager.getCurrentXP();                // Evita divisioni per zero se maxHp è nullo o negativo
                if (maxHp > 0) {
                    int barWidth = 200;  // Larghezza totale della barra in pixel
                    int barHeight = 20;  // Altezza della barra in pixel
                    int HPbarX = 0;       // Posizione X sullo schermo (in alto a sinistra)
                    int HPbarY = 0;       // Posizione Y sullo schermo
                    int EXPbarX = width - barWidth - 10; // Dynamically positions it near the right edge
                    int EXPbarY = 0;       // Posizione Y sullo schermo
                    // Calcola proporzionalmente i pixel rimanenti della vita attuale
                    int currentHPBarWidth = (int) (((double) hp / maxHp) * barWidth);
                    int currentEXPBarWidth = (int) (((double) currentXP / levelManager.getNextLevelXP()) * barWidth);
                    // 1. Sfondo Grigio Scuro / Rosso (Vita persa o mancante)
                    g.setColor(new Color(150, 0, 0));
                    g.fillRect(HPbarX, HPbarY, barWidth, barHeight);
                    
                    // 2. Barra Verde Dinamica (Vita attuale del giocatore)
                    g.setColor(new Color(0, 180, 0));
                    g.fillRect(HPbarX, HPbarY, currentHPBarWidth, barHeight);
                    
                    // 3. Contorno Nero Elegante attorno all'intera barra
                    g.setColor(Color.BLACK);
                    g.drawRect(HPbarX, HPbarY, barWidth, barHeight);
                    

                    // 1. Sfondo Grigio Scuro / Rosso (Vita persa o mancante)
                    g.setColor(new Color(100, 0, 0));
                    g.fillRect(EXPbarX, EXPbarY, barWidth, barHeight);
                    
                    // 2. Barra Verde Dinamica (Vita attuale del giocatore)
                    g.setColor(new Color(0, 0, 200));
                    g.fillRect(EXPbarX, EXPbarY, currentEXPBarWidth, barHeight);
                    
                    // 3. Contorno Nero Elegante attorno all'intera barra
                    g.setColor(Color.BLACK);
                    g.drawRect(EXPbarX, EXPbarY, barWidth, barHeight);


                    int AMMOx = 460; // Posizionato subito dopo la barra EXP (240 + 200 + 20 di spazio)
                    int AMMOy = 0;
                    
                    // Disegna un piccolo sfondo scuro rettangolare per far risaltare il testo delle munizioni
                    g.setColor(new Color(30, 30, 30, 200));
                    g.fillRoundRect(AMMOx, AMMOy, 90, barHeight, 5, 5);
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(AMMOx, AMMOy, 90, barHeight, 5, 5);
                    
                    // Colore dinamico: arancione/rosso se rimangono pochi colpi, altrimenti giallo lucido
                    if (ammo <= 10) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
                    g.drawString("AMMO: " + ammo, AMMOx + 15, AMMOy + 14); // Stampa "AMMO: 50"

                }
            }
        }

            // SE SIAMO IN LIVELLO SUPERIORE, DISEGNA L'OVERLAY DEL MENU UPGRADE
            if (gameState == STATE.LEVEL_UP) {
                // Sfondo oscurato semi-trasparente
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, width, height);

                // Titolo del Menu
                g.setColor(Color.YELLOW);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
                g.drawString("SCEGLI UN UPGRADE!", width / 2 - 150, 80);

                // Sottotitolo informativo
                g.setColor(Color.WHITE);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
                g.drawString("Clicca su una delle opzioni per potenziare il tuo eroe", width / 2 - 160, 110);

                // Disegna le 3 opzioni/pulsanti grafici
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
                for (int i = 0; i < currentUpgradeOptions.size(); i++) {
                    Upgrade u = currentUpgradeOptions.get(i);
                    int cardX = 120 + (i * 260); // Spaziatura orizzontale delle schede
                    int cardY = 180;
                    int cardW = 220;
                    int cardH = 250;

                    // Sfondo della scheda dell'upgrade
                    g.setColor(new Color(40, 40, 50));
                    g.fillRoundRect(cardX, cardY, cardW, cardH, 15, 15);
                    
                    // Bordo dorato lucido
                    g.setColor(new Color(218, 165, 32));
                    g.drawRoundRect(cardX, cardY, cardW, cardH, 15, 15);

                    // Nome dell'upgrade
                    g.setColor(Color.CYAN);
                    g.drawString(u.getName(), cardX + 20, cardY + 40);

                    // Livello Attuale
                    g.setColor(Color.LIGHT_GRAY);
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 13));
                    g.drawString("Livello Attuale: " + u.getCurrentLevel(), cardX + 20, cardY + 70);

                    // Descrizione degli effetti bonus
                    g.setColor(Color.WHITE);
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
                    // Dividiamo il testo per andare a capo se necessario
                    g.drawString(u.getDescription(), cardX + 20, cardY + 140);
                    
                    // Indicazione d'interazione in fondo alla scheda
                    g.setColor(Color.YELLOW);
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
                    g.drawString("[ CLICCA PER SCEGLIERE ]", cardX + 35, cardY + 220);
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16)); // ripristina font
                }
            }
        // IMPROVED GAME OVER MENU OVERLAY
            else if (gameState == STATE.GAME_OVER) {
                // Blur/Dim background simulation
                g.setColor(new Color(20, 0, 0, 180));
                g.fillRect(0, 0, width, height);

                // Elegant Border Frame Card
                int menuW = 1000;
                int menuH = 563;
                int menuX = (width / 2) - (menuW / 2);
                int menuY = (height / 2) - (menuH / 2);

                g.setColor(new Color(30, 10, 10));
                g.fillRoundRect(menuX, menuY, menuW, menuH, 20, 20);
                g.setColor(Color.RED);
                g.drawRoundRect(menuX, menuY, menuW, menuH, 20, 20);

                // Main Title Text
                g.setColor(new Color(255, 50, 50));
                g.setFont(new Font("Impact", Font.PLAIN, 48));
                g.drawString("SEI MORTO!", menuX + 380, menuY + 70);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                g.drawString("Il tuo viaggio si è concluso qui nell'arena.", menuX + 350, menuY + 115);

                
                int btnW = 260;
                int btnH = 45;
                int btnX = (width / 2) - (btnW / 2);
    
                // --- FIRST BOX: RESTART BUTTON ---
                int btnY1 = menuY + 160; 

                 g.setColor(new Color(80, 20, 20));
                g.fillRoundRect(btnX, btnY1, btnW, btnH, 10, 10);
                g.setColor(Color.YELLOW);
                g.drawRoundRect(btnX, btnY1, btnW, btnH, 10, 10);

                // Text for Restart
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("PREMI 'R' PER RICOMINCIARE", btnX + 18, btnY1 + 28);
                int btnY2 = btnY1 + 65; 

                g.setColor(new Color(80, 20, 20));
                g.fillRoundRect(btnX, btnY2, btnW+5, btnH, 10, 10);
                g.setColor(Color.YELLOW);
                g.drawRoundRect(btnX, btnY2, btnW+5, btnH, 10, 10);
                
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(Color.RED);
                g.drawString("PREMI 'X' PER USCIRE DAL GIOCO", btnX , btnY2 + 35);

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

                if(red == 255 && blue == 0 && green == 0) {
                    handler.addObject(new Block(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, ID.Block, tile_ss));
                }

                else if (blue == 255 && red == 0 && green == 0) {
                    // Creiamo il Player passando player_ss
                    player = new Player(xx*32, yy*32, ID.Player, this, player_ss, handler, this);
                    handler.addObject(player);
                    //handler.addObject(new Player(xx*32, yy*32, ID.Player, this, player_ss, handler));
                }

                else if (green == 255 && blue == 0 && red == 0) {                                 //creo i nemici base
                    handler.addObject(new Enemy1(xx * Constants.TILE_SIZE, yy * Constants.TILE_SIZE, ID.Enemy, enemy_ss, handler, levelManager));
                }

                else if (green == 255 && red == 255 && blue == 0) {                                 //creo i nemici base
                    handler.addObject(new Enemy2(xx * Constants.TILE_SIZE, yy * Constants.TILE_SIZE, ID.Enemy, enemy_ss, handler, levelManager));
                }

                else if (green == 255 && blue == 255 && red == 0) {                                 //creo i nemici base
                    handler.addObject(new Enemy3(xx * Constants.TILE_SIZE, yy * Constants.TILE_SIZE, ID.Enemy, enemy_ss, handler, levelManager));
                }

                else if (red == 255 && blue == 255 && green == 0) {
                    handler.addObject(new Crate(xx*Constants.TILE_SIZE, yy*Constants.TILE_SIZE, ID.Crate, crate_tex));
                }

            }
        }
    }

    public void start() {
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
    
    
}