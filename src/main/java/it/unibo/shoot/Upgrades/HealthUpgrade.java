package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.Game;

public class HealthUpgrade extends Upgrade {
     Game game;
    public HealthUpgrade() {
         super("Armatura Pesante", "+20 HP Massimi", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) {
         p.setMaxHealth(p.getMaxHealth() + 20); 
         p.setHealth(p.getMaxHealth());
          Game game = p.getGame();
          if (game != null) {
            game.ammo += 50; // Aggiunge 50 munizioni
          }
         
    }
}