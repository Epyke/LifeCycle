package entities;

import structures.HabitatType;
import utils.Rand;

import java.awt.*;
import java.lang.classfile.instruction.StackInstruction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum AnimalType implements Edible {
    //Syntax: NAME(MaxHealth, MaxEnergy, MaxFood, MaxWater, MaxAge, EnergyReproduction, EnergyMovement, spendRate, vision, canPack, ageReproduction, caloriesValue, habitat, planDiet)
    FOX(150, 100,125, 100, 40, 1, 2,1,0.07, 5, true, 15, 30, HabitatType.DEN, 'W', 10, Color.ORANGE, "res/Fox2.png", "res/FoxDead.png"),
    BUNNY(100, 100, 75, 60, 20,50, 4, 1, 0.05, 3, false, 5, 60, HabitatType.BURROW, 'S', 50, Color.WHITE, "res/Bunny.png", "res/BunnyDead.png"),
    SHEEP(100, 100, 75, 60, 20,50, 4, 1, 0.05, 3, false, 5, 60, HabitatType.BURROW, 'S', 40, Color.PINK, null, null);

    private static final HashMap<AnimalType, HashSet<Edible>> huntMap = new HashMap<>();

    private static final HashMap<Edible, HashSet<AnimalType>> preyMap = new HashMap<>();

    static {
        huntMap.put(FOX, new HashSet<>(Set.of(BUNNY)));
        huntMap.put(BUNNY, new HashSet<>(Set.of(PlantType.CAROT)));
        huntMap.put(SHEEP, new HashSet<>(Set.of(PlantType.PLANT)));
        //Geracao do HashMap inverso ao huntMap, key: presa, values: cacadores
        for(AnimalType hunter : huntMap.keySet()) {
            for(Edible food: huntMap.get(hunter)) {
                //Em qualquer situação o metodo add vai atuar sobre o HashSet
                preyMap.computeIfAbsent(food, k -> new HashSet<>()).add(hunter);
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
    private int maxBabies;

    private double spendRate;

    private int vision;
    private boolean canPack;
    private int AgeReproduction;

    private int CaloriesValue;

    private HabitatType habitat;
    private double spawnRate;

    private char symb;

    private Color StatTitleColor;
    private String imgPath, deathImgPath;

    private AnimalType(int hp, int energy, int food, int water, int maxAge, int energyReproduction, int maxBabies,int energyMovement, double spendRate, int vision, boolean canPack, int ageReproduction, int CaloriesValue, HabitatType habitat, char symb, double spawnRate, Color StatTitleColor, String imgPath, String deathImgPath) {
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
        this.symb = symb;
        this.spawnRate = spawnRate;
        this.StatTitleColor = StatTitleColor;
        this.imgPath = imgPath;
        this.deathImgPath = deathImgPath;
        this.maxBabies = maxBabies;
        if(StatTitleColor == null){
            this.StatTitleColor = Rand.getAnyRandomColor();
        }
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
    public int getMaxBabies(){
        return maxBabies;
    }

    /**
     * Metodo que verifica a existencia de predadores da especie
     * @return retorna um HashSet com as espécies predadoras da espécie
     */
    public HashSet<AnimalType> getPredators() {
        return preyMap.getOrDefault(this, null);
    }

    /**
     * Metodo que verifica se o objeto que implementa a interface Edible, é comestivel pela especie de animal
     * @param food
     * @return
     */
    public boolean canEat(Edible food){
        Set<Edible> diet = huntMap.get(this);
        return diet != null && diet.contains(food);
    }

    /**
     * Obter uma especie de animal, consoante a sua percentagem de aparição
     * @return O tipo de especie que vai aparecer
     */
    public static AnimalType getRandomWeighted() {
        double r = utils.Rand.getDouble(100.0);
        double cumulative = 0.0;

        for (AnimalType type : AnimalType.values()) {
            cumulative += type.getSpawnRate();
            if (r <= cumulative) {
                return type;
            }
        }
        return AnimalType.values()[0];
    }

    public String getDeadImgPath() {
        return deathImgPath;
    }

    public Color getStatTitleColor(){
        return StatTitleColor;
    }

    public String getImgPath() {
        return imgPath;
    }
}
