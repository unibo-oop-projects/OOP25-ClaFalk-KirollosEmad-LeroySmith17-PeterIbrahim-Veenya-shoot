package it.unibo.shoot.model;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class PlayerModel {
    private double x, y;
    private double speed;
    private int health;
    private int maxHealth;
    private boolean isDead = false;
    
    private final List<Integer> damageHistory = new ArrayList<>();
   
    
   
    private double damageMultiplier = 1.0; 
    private double dodgeChance = 0.0;      
    private double pickupRange = 1.0;      

    private float velX = 0, velY = 0;
    
   
    private int aniTick, aniIndex, aniSpeed = 10; 
    private boolean isMoving = false;
    private int row = 0; 

    
    private final int width = 32;
    private final int height = 32;


    private long lastDamageTime = 0; 
    private final int iFramesDuration = 1000; 

    public PlayerModel(double startX, double startY, double speed, int maxHealth) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

 
    public double getSpeed() { 
        return speed; 
    }
    
    public void setSpeed(double speed) { 
        this.speed = speed; 
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth; 
        }
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    public double getPickupRange() {
        return pickupRange;
    }

    public void setPickupRange(double pickupRange) {
        this.pickupRange = pickupRange;
    }


    public void setVelocity(float dx, float dy) {
        this.velX = dx * (float)speed;
        this.velY = dy * (float)speed;
        this.isMoving = (dx != 0 || dy != 0);

        if (dy > 0) row = 0;      
        else if (dy < 0) row = 1; 
        else if (dx < 0) row = 3; 
        else if (dx > 0) row = 2; 
    }

    public void updatePosition() {
        this.x += velX;
        this.y += velY;
    }

    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4) { 
                aniIndex = 0;
            }
        }
        
        if (!isMoving) {
            aniIndex = 0;
        }
    }

    public void takeDamage(int damage) {
       
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true; 
            System.out.println("GAME OVER! Vita: 0");
            return; 
        }

        
        if (Math.random() < this.dodgeChance) {
            System.out.println("Schivato! Nessun danno subito.");
            return; 
        }

        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDamageTime < iFramesDuration) {
            return;
        }

       
        this.health -= damage;
        this.lastDamageTime = currentTime; 
        this.damageHistory.add(damage);
        
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true; 
            System.out.println("GAME OVER! Vita: 0");
        } else {
            System.out.println("Danno subito! Vita attuale: " + this.health);
        }
    }


   
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    
    public int getRow() { return row; }
    public int getAniIndex() { return aniIndex; }
    public boolean isMoving() { return isMoving; }
    
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }
     public int getMaxHealth() { return maxHealth;}

    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    public boolean isDead() {
        return this.isDead;
    }

    public void setHealth(int health) {
        this.health = health ; 
    }
    
    public double getAverageDamageTaken() {
        return damageHistory.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
    }

    
    public int getMaxDamageTaken() {
        return damageHistory.stream()
                            .mapToInt(Integer::intValue)
                            .max()
                            .orElse(0);
    }
}
