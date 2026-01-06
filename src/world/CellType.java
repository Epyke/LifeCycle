package world;

/**
 * A class CellType representa o tipo de cada célula presente no mundo, cada tipo está associado à um caracter.
 */
public enum CellType {
    GRASS('.', "res/Grass.png", false),
    ROCK('#', "res/Rock.png", true),
    WATER('~', "res/Water.png", true);

    private char symb;
    private String imgPath;
    private boolean colision;

    CellType(char symb, String imgPath, boolean colision){
        this.symb = symb;
        this.imgPath = imgPath;
        this.colision = colision;
    }

    public char getSymb(){
        return symb;
    }

    public String getImgPath(){
        return imgPath;
    }
}
