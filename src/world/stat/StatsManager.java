package world.stat;

import entities.AnimalType;

import java.util.HashMap;
import java.util.Map;

public class StatsManager {
    private int currYear;
    private GlobalStat globalStats;
    private HashMap<String, SpecieStat> speciesStatsMap;

    private YearStat recordingStat;
    private YearStat displayStat;

    public StatsManager() {
        this.currYear = 0;
        this.globalStats = new GlobalStat();
        this.recordingStat = new YearStat();
        this.displayStat = new YearStat();
        this.speciesStatsMap = new HashMap<>();
    }

    public void incrementYear() {
        this.currYear++;
        this.displayStat = this.recordingStat;
        this.recordingStat = new YearStat();
    }

    private SpecieStat getSpecies(String speciesName) {
        if (!speciesStatsMap.containsKey(speciesName)) {
            speciesStatsMap.put(speciesName, new SpecieStat());
        }
        return speciesStatsMap.get(speciesName);
    }

    public void registerSpawn(String speciesName, boolean isReproduction) {
        SpecieStat s = getSpecies(speciesName);

        s.increment_total_created();
        s.increment_current_alive();

        globalStats.increment_total_entities_ever_created();
        globalStats.increment_current_entities_alive();

        recordingStat.increment_born_this_turn();

        if (isReproduction) {
            globalStats.increment_total_born_reproduction();
        }

    }

    public void registerDeath(String speciesName, String cause) {
        SpecieStat s = speciesStatsMap.computeIfAbsent(speciesName, k -> new SpecieStat());

        s.increment_deaths();
        s.decrease_current_alive();

        globalStats.increment_total_deaths();
        globalStats.decrease_current_entities_alive();
        recordingStat.increment_died_this_turn();

        // 2. Specific Cause counters
        switch (cause) {
            case "starved":
                s.increment_starved();
                break;
            case "thirst":
                s.increment_thirst();
                break;
            case "energy":
                s.increment_energy();
                break;
            case "natural":
                s.increment_natural();
                break;
            case "eaten":
                s.increment_total_eaten();
                globalStats.increment_total_eaten();
                recordingStat.increment_eaten_this_turn();
                break;
        }
    }

    public void registerEaten(String speciesName) {
        registerDeath(speciesName, "eaten");
    }

    public YearStat getCurrYearStat() {
        return displayStat;
    }

    public HashMap<String, SpecieStat> getSpecieStat(){
        return speciesStatsMap;
    }

    public String currYearStats(){
        StringBuilder sb = new StringBuilder();
        String border = "======================================\n";
        String subBorder = "--------------------------------------\n";

        sb.append(border);
        sb.append("       STATISTICS - YEAR ").append(currYear).append("\n");
        sb.append(border);

        sb.append(String.format("  %-20s : %d\n", "Born", displayStat.getBorn_this_turn()));
        sb.append(String.format("  %-20s : %d\n", "Died", displayStat.getDied_this_turn()));
        sb.append(String.format("  %-20s : %d\n", "Eaten", displayStat.getEaten_this_turn()));
        sb.append(subBorder);
        return sb.toString();
    }

    public String finalStats() {
        StringBuilder sb = new StringBuilder();
        String border = "======================================\n";
        String subBorder = "--------------------------------------\n";

        sb.append(border);
        sb.append("       FINAL STATISTICS ").append("\n");
        sb.append(border);

        // 2. Global Totals
        sb.append(" [GLOBAL]\n");
        sb.append(String.format("  %-20s : %d\n", "Current Alive", globalStats.getCurrent_entities_alive()));
        sb.append(String.format("  %-20s : %d\n", "Total Created", globalStats.getTotal_entities_ever_created()));
        sb.append(String.format("  %-20s : %d\n", "Total Deaths", globalStats.getTotal_deaths()));
        sb.append(String.format("  %-20s : %d\n", "Total Born (Repro)", globalStats.getTotal_born_reproduction()));
        sb.append(subBorder);

        // 3. Species Breakdown
        sb.append(" [SPECIES]\n");

        // Table Header
        sb.append(String.format("  %-10s | %-7s | %-7s | %-7s | %-7s | %-7s | %-7s | %-7s | %-7s \n", "TYPE", "ALIVE", "TOTAL", "DEAD", "STARVED", "THIRST", "ENERGY", "NATURAL", "EATEN"));
        sb.append("  ").append("-".repeat(34)).append("\n");

        for (Map.Entry<String, SpecieStat> entry : speciesStatsMap.entrySet()) {
            String name = entry.getKey();
            SpecieStat data = entry.getValue();

            sb.append(String.format("  %-10s | %-7d | %-7d | %-7d | %-7d | %-7d | %-7d | %7d | %7d \n",
                    name,
                    data.getCurrent_alive(),
                    data.getTotal_created(),
                    data.getDeaths(),
                    data.getStarved(),
                    data.getThirst(),
                    data.getEnergy(),
                    data.getNatural(),
                    data.getEaten()));
        }
        sb.append(border);

        return sb.toString();
    }

    public GlobalStat getGlobalStats(){
        return globalStats;
    }

    public int getCurrYear(){
        return currYear;
    }
}
