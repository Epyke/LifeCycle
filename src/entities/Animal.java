package entities;

import structures.HabitatType;
import utils.Adjacent;
import utils.Rand;
import world.*;

import java.util.ArrayList;

public class Animal extends Entity {
    private final AnimalType type;
    private int currentHealth;
    private int currentEnergy;
    private int currentFood;
    private int currentWater;

    public Animal(World w, Coord coord, AnimalType type){
        super(w,coord);
        this.type = type;
        this.currentHealth = type.getMaxHealth();
        this.currentEnergy = type.getMaxEnergy();
        this.currentFood = type.getMaxHunger();
        this.currentWater = type.getMaxThirst();
    }

    public boolean move(){
        Cell actualCell = super.getWorld().getGrid().get(super.getCoords().getY()).get(super.getCoords().getX());
        ArrayList<Cell> adjs = Adjacent.getAdjacents(super.getWorld(), actualCell, CellType.GRASS, LayerType.NONE, HabitatType.NONE);
        if(adjs.isEmpty()){
            return false;
        }
        int randIndex = Rand.getRandomNmb(adjs.size());
        super.getWorld().getGrid().get(adjs.get(randIndex).getCoord().getY()).get(adjs.get(randIndex).getCoord().getX()).setCurrentOcupant(LayerType.ANIMAL);
        actualCell.setCurrentOcupant(LayerType.NONE);
        return true;
    }

    public void updateStats() {
        if (super.getIsDead()) return;

        currentEnergy -= (int)(type.getSpendRate() * type.getMaxEnergy());
        currentFood -= (int)((type.getSpendRate()*1.5) * type.getMaxHunger());
        currentWater -= (int)((type.getSpendRate()*2) * type.getMaxThirst());

        if(currentEnergy <= 0 || currentHealth <= 0){
            die();
        }
    }

    public void updateAge(){
        if (super.getIsDead()){
            super.incrementDecompose();
            return;
        }

        super.incrementAge();
        if (super.getAge() >= type.getMaxAge()) {
            die();
        }
    }

    public void die() {
        super.setDied();
        type.setSymb('x');
        System.out.println(type + " died at " + "[" + super.getCoords().getX() + "," + super.getCoords().getY() + "]");
    }

    public boolean reproduction(){
        if(currentEnergy >= type.getReproEnergy()){
            //Falta a lógica da reprodução
            return true;
        }
        return false;
    }

    public AnimalType getType(){
        return type;
    }
}
