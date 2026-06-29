package it.unibo.shoot.model;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import it.unibo.shoot.Upgrades.Upgrade;
import it.unibo.shoot.controller.GameOverController;
import it.unibo.shoot.controller.MouseInput;
import it.unibo.shoot.controller.PauseController;
import it.unibo.shoot.loader.LevelLoader;
import it.unibo.shoot.loader.ResourceLoader;
import it.unibo.shoot.util.Constants;
import it.unibo.shoot.view.Camera;
import it.unibo.shoot.view.GameRenderer;
import it.unibo.shoot.view.Window;
import it.unibo.shoot.audio.Sound;

/**
 * Core game class. Manages the game loop, game state, sound, and coordinates
 * the main subsystems: handler, camera, renderer, spawners, and level loading.
 * 
 * The game behavior changes depending on the current STATE
 * (MENU, GAME, LEVEL_UP, GAME_OVER).
 */
public class Game extends Canvas implements Runnable {
    

    public STATE gameState = STATE.MENU;
    private List<Upgrade> currentUpgradeOptions = new ArrayList<>();

    public int ammo = 50;

    private boolean isRunning = false;
    private Thread thread;

    private final Handler handler;
    private final Camera camera;
    private final LevelManager levelManager;
    private final GameRenderer renderer;
    private final ResourceLoader resources;
    private final Sound sound;

    private Spawner spawner;
    private BossSpawner bossSpawner;
    private Player player;

    /**
     * Initializes all game subsystems and creates the window.
     */
    public Game() {
        handler = new Handler();
        camera = new Camera(0, 0);
        resources = new ResourceLoader();
        sound = new Sound();

        levelManager = new LevelManager(this);
        LevelLoader levelLoader = new LevelLoader(handler, resources, levelManager, this);
        player = levelLoader.load(resources.getLevelImage());
        levelManager.setPlayer(player);

        spawner = new Spawner(handler, resources.getEnemySS(), resources.getLevelImage(), levelManager);
        bossSpawner = new BossSpawner(handler, resources.getEnemySS(), resources.getCrateImage(), levelManager);
        renderer = new GameRenderer(handler, camera, levelManager, this);

        new Window(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Constants.TITLE, this);
        //TODO: Make a single central class
        this.addMouseListener(new MouseInput(handler, camera, this));
        this.addKeyListener(new PauseController(this));
        this.addKeyListener(new GameOverController(this));
    }

    /**
     * Resets the game to its initial state and restarts the match.
     * Reinitializes player, level manager, spawners, and clears all entities.
     */
    public void restartGame() {
        handler.clearAllObjects();
        this.ammo = 100;

        LevelManager freshLevelManager = new LevelManager(this);
        LevelLoader levelLoader = new LevelLoader(handler, resources, freshLevelManager, this);
        player = levelLoader.load(resources.getLevelImage());

        if (player != null) {
            freshLevelManager.setPlayer(player);
        }

        spawner = new Spawner(handler, resources.getEnemySS(), resources.getLevelImage(), freshLevelManager);
        bossSpawner = new BossSpawner(handler, resources.getEnemySS(), resources.getCrateImage(), freshLevelManager);

        this.setGameState(STATE.GAME);
    }

    /**
     * Main game loop. Runs at a fixed rate of 60 ticks per second using delta time.
     * Tick and render are decoupled: tick runs at fixed rate, render as fast as possible.
     */
    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        final double ns = 1_000_000_000 / 60.0;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }

            renderer.render(getGameState(), ammo, currentUpgradeOptions);

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    /**
     * Updates game logic depending on the current state.
     * Active only during GAME state; other states pause gameplay updates.
     */
    private void tick() {
        if (gameState == STATE.GAME) {
            handler.tick();
            if (spawner != null)     spawner.tick();
            if (bossSpawner != null) bossSpawner.tick();
            if (handler.getPlayer() != null) {
                camera.tick((Player) handler.getPlayer());
            }
        }
    }

    /**
     * Starts the game thread.
     */
    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the game loop and waits for the thread to terminate.
     */
    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the state of the game.
     * 
     * @param state of the game we want to set.
     */
    public void setGameState(STATE state) {
        this.gameState = state;

        if (state==STATE.GAME_OVER) {
            sound.stopBackgroundMusic();
        } else {
            sound.startBackgroundMusic();
        }
    }

    /**
     * Returs the current state of the game.
     * 
     * @return the state of the game.
     */
    public STATE getGameState() {
        return this.gameState;
    }

    /**
     * Sets the upgrade options in the level-up menu.
     * 
     * @param options list of selected upgrades that will be shown to player.
     */
    public void setUpgradeOptions(List<Upgrade> options) {
        this.currentUpgradeOptions = new ArrayList<>(options);
    }

    /**
     * Returns the list of options currently available.
     * 
     * @return list of currently available options.
     */
    public List<Upgrade> getUpgradeOptions() {
        return currentUpgradeOptions;
    }

    
    /**
     * Returns the Sound used to manage audio in game.
     * 
     * @return sound object
     */
    public Sound getSound() {
        return sound;
    }
    /**
     * Ritorna l'istanza del LevelManager attualmente utilizzata nel gioco.
     * @return il levelManager corrente
     */
    public LevelManager getLevelManager() {
    return this.levelManager;
    }
}
