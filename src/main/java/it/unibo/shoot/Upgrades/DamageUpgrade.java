package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.*;

public class DamageUpgrade extends Upgrade {
    public DamageUpgrade() {
         super("Proiettili Affilati", "+15% Danno inflitto", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) {
         p.setDamageMultiplier(p.getDamageMultiplier() * 1.15); 
    }
}