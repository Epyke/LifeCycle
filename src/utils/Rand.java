package utils;

import java.util.Random;

public class Rand {
    private static Random rng = new Random();

    private Rand(){};

    public static int getRandomNmb(int max){
        if(max < 0){
            throw new IllegalArgumentException("Argument needs to be > 0");
        }
        int min = 0;
        int range = max - min + 1;
        return rng.nextInt(range) + min;
    }

    public static int getRandomNmb(int min, int max){
        if(min >= max){
            throw new IllegalArgumentException("Argument min needs to be < than max");
        }

        if(min < 0){
            throw new IllegalArgumentException("Argument min can't be < 0");
        }

        int range = max - min + 1;
        return rng.nextInt(range) + min;
    }
}
