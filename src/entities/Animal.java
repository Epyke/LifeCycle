package entities;

import structures.HabitatType;
import utils.Adjacent;
import utils.CellUtils;
import utils.Rand;
import world.*;

import java.util.ArrayList;

public class Animal extends Entity implements Edible{
    private final AnimalType type;
    private int currentHealth;
    private int currentEnergy;
    private int currentFood;
    private int currentWater;
    private Gender gender;

    public Animal(World w, Coord coord, AnimalType type){
        super(w,coord);
        this.type = type;
        this.currentHealth = type.getMaxHealth();
        this.currentEnergy = type.getMaxEnergy();
        this.currentFood = type.getMaxHunger();
        this.currentWater = type.getMaxThirst();
        gender = Rand.getRandomEnum(Gender.class);
    }

    private Edible getEdibleType(Entity e) {
        if (e instanceof Animal) return ((Animal) e).getType();
        if (e instanceof Plant) return ((Plant) e).getType();
        return null;
    }

    public boolean eat(){
        if(currentFood > (type.getMaxHunger()*0.65)){
            return false;
        }
        Cell currentCell = CellUtils.findCell(super.getWorld(), super.getCoords());
        ArrayList<Cell> neighbors = Adjacent.getOccupiedAdjacents(super.getWorld(), currentCell);
        if(neighbors.isEmpty()) return false;
        for(Cell c:neighbors){
            Entity e = super.getWorld().getEntities().get(c.getCoord());

            if(e instanceof Edible) {
                Edible food = (Edible) e;


                if (type.canEat(getEdibleType(e))) {
                    currentFood += food.getCalories();

                    if (this.currentFood > type.getMaxHunger()) {
                        this.currentFood = type.getMaxHunger();
                    }

                    super.getWorld().removeEntity(e.getCoords());

                    super.getWorld().moveEntity(this.getCoords(), e.getCoords());

                    System.out.println(this.type + " ate a " + getEdibleType(e) + " [" + e.getCoords().getX() + "," + e.getCoords().getY() + "]");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean move(){
        if(getIsDead()) return false;
        if(eat()){
            return true;
        }
        Cell actualCell = CellUtils.findCell(super.getWorld(), super.getCoords());
        ArrayList<Cell> adjs = Adjacent.getAdjacents(super.getWorld(), actualCell, CellType.GRASS, LayerType.NONE, HabitatType.NONE);
        if(adjs.isEmpty()){
            return false;
        }
        int randIndex = Rand.getRandomNmb(adjs.size() - 1);
        super.getWorld().moveEntity(super.getCoords(), adjs.get(randIndex).getCoord());
        return true;
    }

    public void updateStats() {
        if (super.getIsDead()) return;

        int energyCost = (int)(type.getSpendRate() * type.getMaxEnergy());
        int foodCost = (int)((type.getSpendRate() * 1.5) * type.getMaxHunger());
        int waterCost = (int)((type.getSpendRate() * 2) * type.getMaxThirst());

        currentEnergy = Math.max(0, currentEnergy - energyCost);
        currentFood   = Math.max(0, currentFood - foodCost);
        currentWater  = Math.max(0, currentWater - waterCost);

        if(currentFood > (type.getMaxHunger()*0.65) && currentWater > (type.getMaxThirst()*0.65)){
            int energyGain = (int)((type.getSpendRate() * 2) * type.getMaxEnergy());
            currentEnergy += energyGain;

            if(currentEnergy > type.getMaxEnergy()) {
                currentEnergy = type.getMaxEnergy();
            }
        }

        if(currentEnergy <= 0){
            currentHealth -= 25;
            currentHealth = Math.max(0, currentHealth);
        }

        if(currentHealth <= 0){
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
        System.out.println(type + " died at " + "[" + super.getCoords().getX() + "," + super.getCoords().getY() + "]");
    }

    public boolean reproduction(){
        if(gender == Gender.MALE){
            return false;
        }
        if(currentEnergy >= type.getReproEnergy() && super.getAge() >= type.getAgeReproduction()){
            Cell currCell = CellUtils.findCell(super.getWorld(), super.getCoords());
            ArrayList<Cell> neighbors = Adjacent.getOccupiedAdjacents(super.getWorld(), currCell);
            if (neighbors.isEmpty()){
                return false;
            }
            for(Cell c: neighbors){
                Entity e = super.getWorld().getEntities().get(c.getCoord());
                if(e instanceof Animal){
                    Animal partner = (Animal) e;
                    if(partner.type == type && partner.getAge() >= type.getAgeReproduction() && partner.getEnergy() >= type.getReproEnergy() && partner.getGender() == Gender.MALE){
                        Cell babySlot = Adjacent.getFirstAdjacent(super.getWorld(), currCell, CellType.GRASS, LayerType.NONE, HabitatType.NONE);
                        if(babySlot == null){
                            return false;
                        }
                            if(super.getWorld().bornEntity(babySlot.getCoord(), type)){
                                partner.costEnergy(type.getReproEnergy());
                                currentEnergy -= type.getReproEnergy();
                                System.out.println("New " + this.type + " born at [" + babySlot.getCoord().getX() + "," + babySlot.getCoord().getY() + "]");
                                return true;
                            }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public AnimalType getType(){
        return type;
    }

    @Override
    public int getCalories() {
        return type.getCalories();
    }

    public Gender getGender() {
        return gender;
    }

    public int getEnergy(){
        return currentEnergy;
    }

    public void costEnergy(int cost){
        currentEnergy -= cost;
    }
}
