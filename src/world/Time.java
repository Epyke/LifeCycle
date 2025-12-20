package world;

import entities.AnimalType;
import entities.PlantType;

public class Time {
    private World w;
    public Time(World w){
        this.w = w;
    }

    public void giveTime(int timeLimit){
        int yearsLimit = 50;
        for(int i = 0; i < timeLimit; i++){
            System.out.println("Years: " + i + "\n");
            w.updateEntities();
            System.out.println(w.worldView());
        }
        System.out.println(w.getStats().finalStats());
    }

    public void continuousTime() throws InterruptedException {
        int i = 0;
        while(true){
            System.out.println("Years: " + i + "\n");
            w.updateEntities();
            System.out.println(w.worldView());
            i++;
            wait(1000);
        }
    }

    public void specieExinction(AnimalType type){
        int i = 0;
        while (w.getStats().getSpecieStat().get(type.toString()).current_alive != 0){
            System.out.println("Years: " + i + "\n");
            w.updateEntities();
            System.out.println(w.worldView());
            i++;
        }
        System.out.println(w.getStats().finalStats());
    }

    public void specieExinction(PlantType type){
        int i = 0;
        while (w.getStats().getSpecieStat().get(type.toString()).current_alive != 0){
            System.out.println("Years: " + i + "\n");
            w.updateEntities();
            System.out.println(w.worldView());
            i++;
        }
        System.out.println(w.getStats().finalStats());
    }
    }
