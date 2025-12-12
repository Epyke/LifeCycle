package utils;

import java.util.Random;

/**
 * A class Rand é usada para gerar valores aleatorios
 */
public class Rand {
    private static Random rng = new Random();

    private Rand(){};

    /**
     * Metodo para gerar um numero aleatorio de 0 até um dado numero.
     */
    public static int getRandomNmb(int max){
        if(max < 0){
            throw new IllegalArgumentException("Argument needs to be > 0");
        }
        int min = 0;
        int range = max - min + 1;
        return rng.nextInt(range) + min;
    }

    /**
     * Metodo para gerar um numero aleatorio entre dois numeros dados.
     * @param min Valor mínimo
     * @param max Valor máximo
     * @return um numero aleatorio gerado entre un determinado máximo e mínimo
     */
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
