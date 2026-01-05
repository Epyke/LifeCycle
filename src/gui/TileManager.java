package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import obstacles.Obstacle;
import obstacles.Rock;
import world.Cell;
import world.CellType;
import world.LayerType;
import entities.*;
import world.stat.GlobalStat;

public class TileManager {

    GamePanel gp;
    public Tile[] tiles;
    private HashMap<Object, BufferedImage> entityImages;
    private HashMap<Object, BufferedImage> deadEntityImages;
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        entityImages = new HashMap<>();
        deadEntityImages = new HashMap<>();
        getTileImage();
    }

    public void getTileImage() {
        try {
            // --- CARREGAR TERRENO (Estático) ---
            File fGrass = new File("res/Grass.png");
            File fRock = new File("res/Rock.png");
            File fWater = new File("res/Water.png");

            tiles[0] = new Tile(); // Vazio

            tiles[1] = new Tile();
            if (fGrass.exists()) tiles[1].image = ImageIO.read(fGrass);

            tiles[2] = new Tile();
            if (fRock.exists()) tiles[2].image = ImageIO.read(fRock);

            tiles[4] = new Tile(); // Usamos o 4 para água
            if (fWater.exists()) tiles[4].image = ImageIO.read(fWater);

            // --- CARREGAR IMAGENS DINÂMICAS (Dos Enums) ---

            // 1. Animais
            for (AnimalType type : AnimalType.values()) {
                // 1. Imagem Vivo
                if (type.getImgPath() != null) {
                    File f = new File(type.getImgPath());
                    if (f.exists()) entityImages.put(type, ImageIO.read(f));
                }

                // 2. Imagem Morto
                if (type.getDeadImgPath() != null) {
                    File f = new File(type.getDeadImgPath());
                    if (f.exists()) deadEntityImages.put(type, ImageIO.read(f));
                }
            }

            // 2. Plantas
            for (PlantType type : PlantType.values()) {
                String path = type.getImgPath();
                if (path != null) {
                    File f = new File(path);
                    if (f.exists()) {
                        entityImages.put(type, ImageIO.read(f));
                    } else {
                        System.out.println("Imagem não encontrada para " + type + ": " + path);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        var grid = gp.world.getGrid();
        var entitiesMap = gp.world.getEntities();
        int tileSize = gp.tileSize;
        ArrayList<Obstacle> obstacles = gp.world.getObstacles();

        if (grid == null) return;

        int entitySize = (int)(tileSize * 0.8);
        int offset = (tileSize - entitySize) / 2;

        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                Cell cell = grid.get(y).get(x);
                int worldX = x * tileSize;
                int worldY = y * tileSize;


                if (cell.getType() == CellType.WATER) {
                    if (tiles[4].image != null) g2.drawImage(tiles[4].image, worldX, worldY, tileSize, tileSize, null);
                    else { g2.setColor(Color.BLUE); g2.fillRect(worldX, worldY, tileSize, tileSize); }
                }
                else if (cell.getType() == CellType.GRASS) {
                    if (tiles[1].image != null) g2.drawImage(tiles[1].image, worldX, worldY, tileSize, tileSize, null);
                    else { g2.setColor(new Color(34, 139, 34)); g2.fillRect(worldX, worldY, tileSize, tileSize); }
                }
                else if (cell.getType() == CellType.ROCK) {
                    for(Obstacle o: obstacles) {
                        if(o instanceof Rock) {
                            Rock Tryrock = (Rock) o;
                            if(Tryrock.getStartCoord() .getX() == cell.getCoord().getX() &&  Tryrock.getStartCoord().getY() == cell.getCoord().getY()) {
                                g2.drawImage(tiles[2].image, worldX, worldY, tileSize * 2, tileSize * 2, null);
                            }
                        }
                    }
                }

                if (cell.getCurrentOcupant() != LayerType.NONE) {
                    Entity e = entitiesMap.get(cell.getCoord());

                    if (e instanceof Animal) {
                        Animal a = (Animal) e;
                        AnimalType type = a.getType();

                        // Lógica de seleção de imagem
                        BufferedImage imgToDraw = null;

                        if (a.getIsDead()) {
                            // Se está morto, tenta buscar a imagem de morto
                            imgToDraw = deadEntityImages.get(type);
                            // Fallback: Se não houver imagem de morto, usa a de vivo mas podes aplicar um filtro ou cor
                        } else {
                            // Se está vivo
                            imgToDraw = entityImages.get(type);
                        }

                        // DESENHAR
                        if (imgToDraw != null) {
                            g2.drawImage(imgToDraw, worldX + offset, worldY + offset, entitySize, entitySize, null);
                        } else {
                            // FALLBACK (Sem Imagem)
                            if (a.getIsDead()) g2.setColor(Color.BLACK); // Morto fica preto
                            else g2.setColor(type.getStatTitleColor()); // Vivo fica com a cor do tipo
                            g2.fillOval(worldX + offset, worldY + offset, entitySize, entitySize);
                        }
                    }
                    else if (e instanceof Plant) {
                        Plant p = (Plant) e;
                        PlantType type = p.getType();

                        if (entityImages.containsKey(type)) {
                            g2.drawImage(entityImages.get(type), worldX + offset, worldY + offset, entitySize, entitySize, null);
                        } else {
                            // FALLBACK: QUADRADO com cor aleatória definida
                            g2.setColor(type.getStatTitleColor());
                            g2.fillRect(worldX + offset, worldY + offset, entitySize, entitySize);
                        }
                    }
                }
            }
        }
    }
}