package world;

/**
 * A class CellType representa o tipo de cada célula presente no mundo, cada tipo está associado à um caracter.
 */
public enum CellType {
    GRASS('.', "res/Grass.png"),
    ROCK('#', "res/Rock.png"),
    WATER('~', "res/Water.png");

    private char symb;
    private String imgPath;
    private boolean colision;

    CellType(char symb, String imgPath){
        this.symb = symb;
        this.imgPath = imgPath;
    }

    public char getSymb(){
        return symb;
    }

    public String getImgPath(){
        return imgPath;
    }
}
