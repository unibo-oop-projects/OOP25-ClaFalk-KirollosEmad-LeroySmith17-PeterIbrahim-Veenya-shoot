// Questo file DEVE chiamarsi Upgrade.java
package it.unibo.shoot.Upgrades;

import it.unibo.shoot.model.Player;

public abstract class Upgrade {
    protected String name;
    protected String description;
    protected int currentLevel = 0; 
    protected int maxLevel=10;


    public Upgrade(String name, String description, int maxLevel) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
    }

    public final void apply(Player player) {
        if (!isMaxed()) {
            executeUpgradeLogic(player);
            this.currentLevel++; // Increment safely here
        }
    }

    protected abstract void executeUpgradeLogic(Player player);

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isMaxed() { return currentLevel >= maxLevel; }
    
}
