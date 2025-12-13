package entities;

public enum PlantType implements Edible {
    CARROT('C', 2, 10, 25, 50, 5),
    OAK('O', 10, 100, 80, 100,5);

    private char symb;
    private int ageReproduction;
    private int maxAge;
    private int energyReproduction;
    private int maxEnergy;
    private int getEnergy;

    PlantType(char symb, int ageReproduction, int maxAge, int energyReproduction, int maxEnergy, int getEnergy){
        this.symb = symb;
        this.ageReproduction = ageReproduction;
        this.maxAge = maxAge;
        this.energyReproduction = energyReproduction;
        this.maxEnergy = maxEnergy;
        this.getEnergy = getEnergy;
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
        if(this.symb == 'C'){
            return 20;
        }
        return 0;
    }
}
