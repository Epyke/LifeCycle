package world.stat;

public class SpecieStat {


    private int current_alive;
    private int total_created;
    private int deaths;
    private int eaten;
    private int starved;
    private int thirst;
    private int natural;
    private int energy;

    public SpecieStat(){
        current_alive = 0;
        total_created = 0;
        deaths = 0;
        eaten = 0;
        starved = 0;
        thirst = 0;
        natural = 0;
        energy = 0;
    }

    public void increment_current_alive(){
        current_alive++;
    }

    public void decrease_current_alive(){
        current_alive--;
    }

    public void increment_total_created(){
        total_created++;
    }

    public void increment_deaths(){
        deaths++;
    }

    public void increment_starved(){
        starved++;
    }

    public void increment_thirst(){
        thirst++;
    }

    public void increment_natural(){
        natural++;
    }

    public void increment_energy(){
        energy++;
    }

    public void increment_total_eaten(){
        eaten++;
    }

    public int getCurrent_alive(){
        return current_alive;
    }

    public int getTotal_created() {
        return total_created;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getEaten() {
        return eaten;
    }

    public int getStarved() {
        return starved;
    }

    public int getThirst() {
        return thirst;
    }

    public int getNatural() {
        return natural;
    }

    public int getEnergy() {
        return energy;
    }
}
