package entities;

import structures.HabitatType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum AnimalType implements Edible {
    //Syntax: NAME(MaxHealth, EnergyReproduction, EnergyMovement, spendRate, vision, canPack, ageReproduction, caloriesValue, habitat, planDiet)
    WOLF(150, 65, 1, 0.07, 5, true, 15, 30, HabitatType.DEN, null),
    RABBIT(100, 50, 1, 0.05, 3, false, 5, 60, HabitatType.BURROW, Set.of(PlantType.CARROT));

    private static final HashMap<AnimalType, Set<AnimalType>> huntMap = new HashMap<>();

    private static final HashMap<AnimalType, Set<AnimalType>> preyMap = new HashMap<>();

    //Rever esta parte do static{} e porque usar Set e não HashSet
    static {
        //Set basic hunter --> preys
        huntMap.put(WOLF, Set.of(RABBIT));

        //Get auto the inversed map prey ---> hunters
        for(AnimalType hunter : huntMap.keySet()) {
            for(AnimalType prey: huntMap.get(hunter)) {
                //Get the list of this prey, if it doesn't exist create a new HashSet
                //Em qualquer situação o metodo add vai atuar sobre o HashSet
                preyMap.computeIfAbsent(prey, k -> new HashSet<>()).add(hunter);
            }
        }
    }

    //Basic values variables
    private int health;
    private int EnergyReproduction;
    private int EnergyMovement;

    //Spend rate of each variable by day passed
    private double spendRate;

    //More complex value variables
    private int vision;
    private boolean canPack;
    private int AgeReproduction;

    //If something eat it
    private int CaloriesValue;

    //Enum Type variables
    private HabitatType habitat;
    private Set<PlantType> plantDiet;

    private AnimalType(int hp, int energyReproduction, int energyMovement, double spendRate, int vision, boolean canPack, int ageReproduction, int CaloriesValue, HabitatType habitat, Set<PlantType> plantDiet){
        this.health = hp;
        this.EnergyReproduction = energyReproduction;
        this.EnergyMovement = energyMovement;
        this.spendRate = spendRate;
        this.vision = vision;
        this.canPack = canPack;
        this.AgeReproduction = ageReproduction;
        this.CaloriesValue = CaloriesValue;
        this.habitat = habitat;
        this.plantDiet = plantDiet;
    }

    public Set<AnimalType> getPredators() {
        return huntMap.getOrDefault(this, null);
    }

    @Override
    public int getCalories(){
        return CaloriesValue;
    }

    public boolean canEat(Edible food){
        if(food instanceof PlantType) {
            return plantDiet.contains((PlantType)food);
        }

        if (food instanceof AnimalType) {
            return preyMap.getOrDefault(this, null).contains(food);
        }
        return false;
    }


}
