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
    private int trueMaxAge;

    public Animal(World w, Coord coord, AnimalType type){
        super(w,coord);
        this.type = type;
        this.currentHealth = type.getMaxHealth();
        this.currentEnergy = type.getMaxEnergy();
        this.currentFood = type.getMaxHunger();
        this.currentWater = type.getMaxThirst();
        gender = Rand.getRandomEnum(Gender.class);
        trueMaxAge = Rand.getRandomNmb((int)(type.getMaxAge()*0.8), type.getMaxAge());
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

                    e.die("eaten");

                    super.getWorld().removeEntity(e.getCoords());

                    super.getWorld().moveEntity(this.getCoords(), e.getCoords());

                    return true;
                }
            }
        }
        return false;
    }

    public boolean searchPartner(){
        if (this.gender != Gender.MALE) return false;

        if (currentEnergy < type.getReproEnergy() || getAge() < type.getAgeReproduction()) {
            return false;
        }

        int vision = type.getVision();
        Coord myPos = super.getCoords();
        World w = super.getWorld();

        Coord bestTarget = null;
        int minDistance = Integer.MAX_VALUE;


        for (Entity e : w.getEntities().values()) {
            if (!(e instanceof Animal)) continue;
            Animal target = (Animal) e;

            if (target.getType() == this.type
                    && target.getGender() == Gender.FEMALE
                    && !target.getIsDead() && target.getEnergy() >= type.getReproEnergy()){

                //Calculo da distancia de Manhatan
                int dist = Math.abs(target.getCoords().getX() - myPos.getX())
                        + Math.abs(target.getCoords().getY() - myPos.getY());

                if (dist <= vision && dist < minDistance) {
                    minDistance = dist;
                    bestTarget = target.getCoords();
                }
            }
        }

        if (bestTarget != null) {
            return moveToTarget(bestTarget);
        }

        return false;
    }

    private boolean moveToTarget(Coord target) {
        int dx = target.getX() - super.getCoords().getX();
        int dy = target.getY() - super.getCoords().getY();

        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);

        if (stepX != 0) {
            if (tryMove(super.getCoords().getX() + stepX, super.getCoords().getY())) return true;
        }

        if (stepY != 0) {
            if (tryMove(super.getCoords().getX(), super.getCoords().getY() + stepY)) return true;
        }

        return false;
    }

    private boolean tryMove(int x, int y) {
        World w = super.getWorld();
        if (!Adjacent.verifyCoords(w, x, y)) return false;

        Cell targetCell = CellUtils.findCell(w, x, y);

        if (targetCell.getCurrentOcupant() == LayerType.NONE
                && targetCell.getType() == CellType.GRASS) {
            w.moveEntity(super.getCoords(), new Coord(x, y));
            currentEnergy -= type.getEnergyMovement();
            return true;
        }
        return false;
    }

    public boolean move(){
        if(getIsDead()) return false;
        if(eat()){
            return true;
        }

        if (searchPartner()) {
            return true;
        }

        Cell actualCell = CellUtils.findCell(super.getWorld(), super.getCoords());
        ArrayList<Cell> adjs = Adjacent.getAdjacents(super.getWorld(), actualCell, CellType.GRASS, LayerType.NONE, HabitatType.NONE);
        if(adjs.isEmpty()){
            return false;
        }
        int randIndex = Rand.getRandomNmb(adjs.size() - 1);
        super.getWorld().moveEntity(super.getCoords(), adjs.get(randIndex).getCoord());
        currentEnergy -= type.getEnergyMovement();
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
            currentHealth -= 10;
        }

        if(currentFood <= 0){
            currentHealth -= 25;
        }

        if(currentWater <= 0){
            currentHealth -= 25;
        }

        currentHealth = Math.max(0, currentHealth);

        if(currentHealth <= 0){
            String cause = "energy";

            if (currentWater <= 0) {
                cause = "thirst";
            } else if (currentFood <= 0) {
                cause = "starved";
            }

            die(cause);
        }
    }

    public void updateAge(){
        if (super.getIsDead()){
            super.incrementDecompose();
            return;
        }

        super.incrementAge();
        if (super.getAge() >= trueMaxAge) {
            die("natural");
        }
    }

    public void die(String cause) {
        if (super.getIsDead()) return;
        super.setDied();
        super.getWorld().getStats().registerDeath(this.type.toString(), cause);
    }

    public boolean reproduction(){
        if(gender == Gender.MALE){
            return false;
        }
        if(currentEnergy >= type.getReproEnergy() && super.getAge() >= type.getAgeReproduction()){
            if(Rand.checkPercentage(30)){
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
                                    return true;
                                }
                            return false;
                        }
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
