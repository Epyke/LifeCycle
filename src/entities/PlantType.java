package entities;

public enum PlantType implements Edible {
    PLANT('P', 2, 15, 25, 50, 5, 20);

    private char symb;
    private int ageReproduction;
    private int maxAge;
    private int energyReproduction;
    private int maxEnergy;
    private int getEnergy;
    private int calories;

    PlantType(char symb, int ageReproduction, int maxAge, int energyReproduction, int maxEnergy, int getEnergy, int calories){
        this.symb = symb;
        this.ageReproduction = ageReproduction;
        this.maxAge = maxAge;
        this.energyReproduction = energyReproduction;
        this.maxEnergy = maxEnergy;
        this.getEnergy = getEnergy;
        this.calories = calories;
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

    @Override
    public int getCalories() {
        return calories;
    }
}
