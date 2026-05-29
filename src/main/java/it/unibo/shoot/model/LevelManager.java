package it.unibo.shoot.model;

// Questo file DEVE chiamarsi LevelManager.java
// Add this import at the top of LevelManager.java
import java.util.*;
import it.unibo.shoot.Upgrades.*;
public class LevelManager {
    private int currentLevel = 1;
    private int currentXP = 0;
    private int nextLevelXP = 100;
    
    private List<Upgrade> availableUpgrades;
    public static List<Upgrade> currentUpgradeOptions = new ArrayList<>();
    private Player player;

    public LevelManager(Player player) {
        this.player = player;
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
        triggerLevelUpMenu();
    }

    private void triggerLevelUpMenu() {
    
    // 1. Extract 3 random upgrades
    List<Upgrade> options = getRandomUpgrades(3);
    
    if (options.isEmpty()) {
        System.out.println("Tutti gli upgrade sono già al massimo!");
        Game.gameState = STATE.GAME;
        return;
    }

    // 2. Passa le opzioni estratte alla classe Game per renderizzarle a schermo
        Game.currentUpgradeOptions = options;
        
        // 3. Cambia lo stato per congelare la fisica di nemici/spawner ed aprire il menu overlay
        Game.gameState = STATE.LEVEL_UP;
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
}