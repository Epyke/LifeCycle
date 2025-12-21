package world.stat;

public class GlobalStat {
    private int total_entities_ever_created;
    private int current_entities_alive;
    private int total_deaths;
    private int total_eaten;
    private int total_born_reproduction;

    public  GlobalStat() {
        total_entities_ever_created = 0;
        current_entities_alive = 0;
        total_deaths = 0;
        total_eaten = 0;
        total_born_reproduction = 0;
    }

    public void increment_total_entities_ever_created() {
        total_entities_ever_created++;
    }

    public void increment_total_deaths() {
        total_deaths++;
    }

    public void increment_total_eaten() {
        total_eaten++;
    }

    public void increment_total_born_reproduction() {
        total_born_reproduction++;
    }

    public void increment_current_entities_alive() {
        current_entities_alive++;
    }

    public void decrease_current_entities_alive() {
        current_entities_alive--;
    }

    public int getTotal_entities_ever_created() {
        return total_entities_ever_created;
    }

    public int getCurrent_entities_alive() {
        return current_entities_alive;
    }

    public int getTotal_deaths() {
        return total_deaths;
    }

    public int getTotal_eaten() {
        return total_eaten;
    }

    public int getTotal_born_reproduction() {
        return total_born_reproduction;
    }
}
