package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.Game;
public class DamageUpgrade extends Upgrade {
    public DamageUpgrade() {
         super("Proiettili Affilati", "+15% Danno inflitto", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) {
         p.setDamageMultiplier(p.getDamageMultiplier() * 1.15); 
         Game game = p.getGame();
          if (game != null) {
            game.ammo += 50; // Aggiunge 50 munizioni
          }
    }
}