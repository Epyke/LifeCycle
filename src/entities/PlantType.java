package entities;

import utils.Rand;

import java.awt.*;

public enum PlantType implements Edible {
    PLANT('P', 2, 15, 25, 50, 5, 20, 10.0, Color.GREEN, "res/Plants.png"),
    CAROT('C', 2, 15, 25, 50, 5, 20, 90.0, null, "res/Carrot.png");
    private char symb;
    private int ageReproduction;
    private int maxAge;
    private int energyReproduction;
    private int maxEnergy;
    private int getEnergy;
    private int calories;
    private double SpawnRate;
    private Color StatTitleColor;
    private String imgPath;

    PlantType(char symb, int ageReproduction, int maxAge, int energyReproduction, int maxEnergy, int getEnergy, int calories, double SpawnRate, Color StatTitleColor, String imgPath) {
        this.symb = symb;
        this.ageReproduction = ageReproduction;
        this.maxAge = maxAge;
        this.energyReproduction = energyReproduction;
        this.maxEnergy = maxEnergy;
        this.getEnergy = getEnergy;
        this.calories = calories;
        this.SpawnRate = SpawnRate;
        this.StatTitleColor = StatTitleColor;
        this.imgPath = imgPath;
        if (StatTitleColor == null){
            this.StatTitleColor = Rand.getAnyRandomColor();
        }
    }

    public char getSymb(){
        return symb;
    }
    public int getMaxAge(){return maxAge;}
    public int getAgeReproduction(){return ageReproduction;};
    public int getEnergyReproduction(){return energyReproduction;}
    public int getMaxEnergy(){return maxEnergy;}
    public int getEnergyRate(){
        return getEnergy;
    }
    public double getSpawnRate(){return SpawnRate;}
    public Color getStatTitleColor(){
        return StatTitleColor;
    }
    @Override
    public int getCalories() {
        return calories;
    }

    /**
     * Obter uma especie de planta, consoante a sua percentagem de aparição
     * @return O tipo de especie que vai aparecer
     */
    public static PlantType getRandomWeighted() {
        double r = utils.Rand.getDouble(100.0);
        double cumulative = 0.0;

        for (PlantType type : PlantType.values()) {
            cumulative += type.getSpawnRate(); // Ensure this getter exists
            if (r <= cumulative) {
                return type;
            }
        }
        return PlantType.values()[0];
    }

    public String getImgPath() {
        return imgPath;
    }
}

