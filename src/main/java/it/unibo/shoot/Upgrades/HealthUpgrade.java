package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.*;

public class HealthUpgrade extends Upgrade {
    public HealthUpgrade() {
         super("Armatura Pesante", "+20 HP Massimi", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) {
         p.setMaxHealth(p.getMaxHealth() + 20); 
         p.setHealth(p.getMaxHealth());
    }
}