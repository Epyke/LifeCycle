package entities;

import world.Coord;
import world.World;

public class Animal extends Entity {
    private final AnimalType type;
    private int currentHealth;
    private int currentEnergy;
    private int currentFood;
    private int currentWater;
    private int currentAge = 0;
    private boolean isDead = false;

    public Animal(World w, Coord coord, AnimalType type){
        super(w,coord);
        this.type = type;
        this.currentHealth = type.getMaxHealth();
        this.currentEnergy = type.getMaxEnergy();
        this.currentFood = type.getMaxHunger();
        this.currentWater = type.getMaxThirst();
    }

    public void update() {
        if (isDead) return;

        currentAge++;

        currentEnergy -= (int)(type.getSpendRate() * type.getMaxEnergy());
        currentFood -= (int)((type.getSpendRate()*1.5) * type.getMaxHunger());
        currentWater -= (int)((type.getSpendRate()*2) * type.getMaxThirst());

        if (currentEnergy <= 0 || currentHealth <= 0 || currentAge >= type.getMaxAge()) {
            die();
        }
    }

    private void die() {
        this.isDead = true;
        type.setSymb('X');
        System.out.println(type + " died at " + "[" + super.getCoords().getX() + "," + super.getCoords().getY() + "]");
    }

    public boolean reproduction(){
        if(currentEnergy >= type.getReproEnergy()){
            //Falta a lógica da reprodução
            return true;
        }
        return false;
    }
}
