package world;

public class Time {
    private World w;
    public Time(World w){
        this.w = w;
    }

    public void startTime(){
        int yearsLimit = 50;
        for(int i = 0; i < yearsLimit; i++){
            w.updateEntities();
            System.out.println(w.worldView());
        }
    }
    }
