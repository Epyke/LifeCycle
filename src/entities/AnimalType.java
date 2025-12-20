package entities;

import structures.HabitatType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum AnimalType implements Edible {
    //Syntax: NAME(MaxHealth, MaxEnergy, MaxFood, MaxWater, MaxAge, EnergyReproduction, EnergyMovement, spendRate, vision, canPack, ageReproduction, caloriesValue, habitat, planDiet)
    WOLF(150, 100,125, 100, 20, 1, 1,0.07, 5, true, 15, 30, HabitatType.DEN, null, 'W', 30),
    SHEEP(100, 100, 75, 60, 10,50, 1, 0.05, 3, false, 5, 60, HabitatType.BURROW, Set.of(PlantType.PLANT), 'S', 70);

    private static final HashMap<AnimalType, Set<AnimalType>> huntMap = new HashMap<>();

    private static final HashMap<AnimalType, Set<AnimalType>> preyMap = new HashMap<>();

    static {
        huntMap.put(WOLF, Set.of(SHEEP));

        for(AnimalType hunter : huntMap.keySet()) {
            for(AnimalType prey: huntMap.get(hunter)) {
                //Em qualquer situação o metodo add vai atuar sobre o HashSet
                preyMap.computeIfAbsent(prey, k -> new HashSet<>()).add(hunter);
            }
        }
    }

    private int Maxhealth;
    private int MaxEnergy;
    private int MaxHunger;
    private int MaxThirst;
    private int MaxAge;
    private int EnergyReproduction;
    private int EnergyMovement;

    private double spendRate;

    private int vision;
    private boolean canPack;
    private int AgeReproduction;

    private int CaloriesValue;

    private HabitatType habitat;
    private Set<PlantType> plantDiet;
    private double spawnRate;

    private char symb;

    private AnimalType(int hp, int energy, int food, int water, int maxAge, int energyReproduction, int energyMovement, double spendRate, int vision, boolean canPack, int ageReproduction, int CaloriesValue, HabitatType habitat, Set<PlantType> plantDiet, char symb, double spawnRate) {
        this.Maxhealth = hp;
        this.MaxEnergy = energy;
        this.MaxHunger = food;
        this.MaxThirst = water;
        this.MaxAge = maxAge;
        this.EnergyReproduction = energyReproduction;
        this.EnergyMovement = energyMovement;
        this.spendRate = spendRate;
        this.vision = vision;
        this.canPack = canPack;
        this.AgeReproduction = ageReproduction;
        this.CaloriesValue = CaloriesValue;
        this.habitat = habitat;
        this.plantDiet = plantDiet;
        this.symb = symb;
        this.spawnRate = spawnRate;
    }

    public Set<AnimalType> getPredators() {
        return huntMap.getOrDefault(this, null);
    }

    @Override
    public int getCalories(){
        return CaloriesValue;
    }

    public int getMaxHealth() { return Maxhealth; }
    public int getMaxEnergy() {return MaxEnergy;}
    public int getMaxHunger() {return MaxHunger; }
    public int getMaxThirst() {return MaxThirst; }
    public int getMaxAge() {return MaxAge;}
    public int getVision() { return vision; }
    public double getSpendRate() { return spendRate; }
    public int getReproEnergy(){return EnergyReproduction;}
    public char getSymb() { return symb; }
    public int getAgeReproduction(){
        return AgeReproduction;
    }
    public int getEnergyMovement(){
        return EnergyMovement;
    }
    public double getSpawnRate(){return spawnRate;}

    public boolean canEat(Edible food){
        if(food instanceof PlantType) {
            if (this.plantDiet == null) return false;
            return plantDiet.contains((PlantType)food);
        }

        if (food instanceof AnimalType) {
            Set<AnimalType> myPrey = huntMap.get(this);
            return myPrey != null && myPrey.contains(food);
        }
        return false;
    }


}
