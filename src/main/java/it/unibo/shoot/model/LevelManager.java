package it.unibo.shoot.model;

import java.util.*;
import it.unibo.shoot.Upgrades.*;
import it.unibo.shoot.audio.Sound;
import it.unibo.shoot.model.Game;


public class LevelManager {
    private int currentLevel = 1;
    private int currentXP = 0;
    private int nextLevelXP = 100;
    
    private List<Upgrade> availableUpgrades;
    private List<Upgrade> currentUpgradeOptions = new ArrayList<>();
    private Player player;
    Game game;

    public LevelManager(Game game) {
        this.game = game;
        this.availableUpgrades = new ArrayList<>();
        
        // Inizializzo il pool con i 5 upgrade che abbiamo creato
        availableUpgrades.add(new SpeedUpgrade());
        availableUpgrades.add(new HealthUpgrade());
        availableUpgrades.add(new DamageUpgrade());
        availableUpgrades.add(new EvasionUpgrade());
    }

    public void addXP(int amount) {
        currentXP += amount;
        if (currentXP >= nextLevelXP) {
            levelUp();
        }
    }

    private void levelUp() {
        currentXP -= nextLevelXP;
        currentLevel++;
        nextLevelXP = (int) (nextLevelXP * 1.25);
        game.getSound().play(Sound.SoundType.LEVEL_UP);
        triggerLevelUpMenu();
    }
    public int getCurrentLevel() {
        return this.currentLevel;}
    private void triggerLevelUpMenu() {
    Game.levelUpTime = System.currentTimeMillis();
    // 1. Extract 3 random upgrades
    List<Upgrade> options = getRandomUpgrades(3);
    
    if (options.isEmpty()) {
        System.out.println("Tutti gli upgrade sono già al massimo!");
        game.setGameState(STATE.GAME);
        return;
    }

    // 2. Passa le opzioni estratte alla classe Game per renderizzarle a schermo
        //Game.currentUpgradeOptions = options;
        game.setUpgradeOptions(options);
        
        // 3. Cambia lo stato per congelare la fisica di nemici/spawner ed aprire il menu overlay
        game.setGameState(STATE.LEVEL_UP);
}

    public List<Upgrade> getRandomUpgrades(int count) {
        List<Upgrade> eligible = new ArrayList<>(availableUpgrades.stream()
                .filter(u -> !u.isMaxed())
                .toList());
        
        Collections.shuffle(eligible);
        return eligible.subList(0, Math.min(count, eligible.size()));
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public int getCurrentXP() {
        return this.currentXP;
    }

    public int getNextLevelXP() {
        return this.nextLevelXP;
    }

}
