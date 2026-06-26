package it.unibo.shoot.model;

import java.util.*;
import it.unibo.shoot.Upgrades.*;
import it.unibo.shoot.audio.Sound;

public class LevelManager {
    private int currentLevel = 1;
    private int currentXP = 0;
    private int nextLevelXP = 100;
    private long lastLevelUpTime = 0;
    private List<Upgrade> availableUpgrades;
    private List<Upgrade> currentUpgradeOptions = new ArrayList<>();
    private Player player;
    Game game;

    public LevelManager(Game game) {
        this.game = game;
        this.availableUpgrades = new ArrayList<>();
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
        this.lastLevelUpTime = System.currentTimeMillis();
    }

    public int getCurrentLevel() {
        return this.currentLevel;
    }

    private void triggerLevelUpMenu() {
        List<Upgrade> options = getRandomUpgrades(3);

        if (options.isEmpty()) {
            System.out.println("Tutti gli upgrade sono già al massimo!");
            game.setGameState(STATE.GAME);
            return;
        }

        game.setUpgradeOptions(options);
        game.setGameState(STATE.LEVEL_UP);
    }

    public List<Upgrade> getRandomUpgrades(int count) {
        List<Upgrade> eligible = new ArrayList<>(availableUpgrades.stream()
                .filter(u -> !u.isMaxed())
                .toList());
        Collections.shuffle(eligible);
        return eligible.subList(0, Math.min(count, eligible.size()));
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCurrentXP() {
        return this.currentXP;
    }

    public int getNextLevelXP() {
        return this.nextLevelXP;
    }

    public long getLastLevelUpTime() {
        return this.lastLevelUpTime;
    }
}