package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.*;

public class EvasionUpgrade extends Upgrade {
    public EvasionUpgrade() { 
        super("Mantello Schivata", "+5% Possibilità di schivata", 4); 
    }

    @Override 
    protected void executeUpgradeLogic(Player p) { 
        p.setDodgeChance(p.getDodgeChance() + 0.05); 
    }
}
