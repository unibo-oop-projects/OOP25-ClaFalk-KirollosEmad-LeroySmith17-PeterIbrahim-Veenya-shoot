package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.*;

// 1. SCARPE RAPIDE 
public class SpeedUpgrade extends Upgrade {
    public SpeedUpgrade() {
         super("Scarpe Rapide", "+10% Velocità di movimento", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) { 
        p.setSpeed(p.getSpeed() * 1.10); 
    }
}

