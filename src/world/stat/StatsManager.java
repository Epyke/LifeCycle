package world.stat;

import java.util.HashMap;
import java.util.Map;

public class StatsManager {
    private int currYear;
    private GlobalStat globalStats;
    private YearStat currYearStat;
    private HashMap<String, SpecieStat> speciesStatsMap;

    public StatsManager() {
        this.currYear = 0;
        this.globalStats = new GlobalStat();
        this.currYearStat = new YearStat();
        this.speciesStatsMap = new HashMap<>();
    }

    public void incrementYear() {
        this.currYear++;
        this.currYearStat.reset();
    }

    private SpecieStat getSpecies(String speciesName) {
        if (!speciesStatsMap.containsKey(speciesName)) {
            speciesStatsMap.put(speciesName, new SpecieStat());
        }
        return speciesStatsMap.get(speciesName);
    }

    public void registerSpawn(String speciesName, boolean isReproduction) {
        SpecieStat s = getSpecies(speciesName);

        s.total_created++;
        s.current_alive++;

        globalStats.total_entities_ever_created++;
        globalStats.current_entities_alive++;

        currYearStat.born_this_turn++;

        if (isReproduction) {
            globalStats.total_born_reproduction++;
        }

    }

    public void registerDeath(String speciesName, String cause) {
        // Get or create the stats for this species
        SpecieStat s = speciesStatsMap.computeIfAbsent(speciesName, k -> new SpecieStat());

        // 1. General counters
        s.deaths++;
        s.current_alive--;

        globalStats.total_deaths++;
        globalStats.current_entities_alive--;
        currYearStat.died_this_turn++;

        // 2. Specific Cause counters
        switch (cause) {
            case "starved":
                s.starved++;
                break;
            case "thirst":
                s.thirst++;
                break;
            case "energy":
                s.energy++;
                break;
            case "natural":
                s.natural++;
                break;
            case "eaten":
                s.eaten++;
                globalStats.total_eaten++;
                currYearStat.eaten_this_turn++;
                break;
        }
    }

    public void registerEaten(String speciesName) {
        globalStats.total_eaten++;
        currYearStat.eaten_this_turn++;
        registerDeath(speciesName, "eaten");
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

        sb.append(String.format("  %-20s : %d\n", "Born", currYearStat.born_this_turn));
        sb.append(String.format("  %-20s : %d\n", "Died", currYearStat.died_this_turn));
        sb.append(String.format("  %-20s : %d\n", "Eaten", currYearStat.eaten_this_turn));
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
        sb.append(String.format("  %-20s : %d\n", "Current Alive", globalStats.current_entities_alive));
        sb.append(String.format("  %-20s : %d\n", "Total Created", globalStats.total_entities_ever_created));
        sb.append(String.format("  %-20s : %d\n", "Total Deaths", globalStats.total_deaths));
        sb.append(String.format("  %-20s : %d\n", "Total Born (Repro)", globalStats.total_born_reproduction));
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
                    data.current_alive,
                    data.total_created,
                    data.deaths,
                    data.starved,
                    data.thirst,
                    data.energy,
                    data.natural,
                    data.eaten));
        }
        sb.append(border);

        return sb.toString();
    }
}
