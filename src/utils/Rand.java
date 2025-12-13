package utils;

import java.util.List;
import java.util.Random;
import java.util.List;

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

    //Uso do Google Gemini porque ainda não demos esta parte da matéria em prog 2

    /**
     * Generates a random value from any given Enum class.
     * Usage: Rand.getRandomEnum(PlantType.class)
     */
    public static <T extends Enum<?>> T getRandomEnum(Class<T> enumClass) {
        // Get all enum constants as an array
        T[] values = enumClass.getEnumConstants();

        // Pick a random index from 0 to length-1
        // We use your existing getRandomNmb method here
        int index = getRandomNmb(values.length - 1);

        return values[index];
    }


    /**
     * Returns a random element from a given List (ArrayList, LinkedList, etc).
     * @param list The list to pick from.
     * @return A random element from the list.
     */
    public static <T> T getRandomItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }

        // Generate a random index from 0 to size-1
        int index = getRandomNmb(list.size() - 1);

        return list.get(index);
    }
}
