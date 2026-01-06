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

    private GamePanel gp;
    public Tile[] tiles;
    private HashMap<CellType, BufferedImage> tileImages;
    private HashMap<Object, BufferedImage> entityImages;
    private HashMap<Object, BufferedImage> deadEntityImages;
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        entityImages = new HashMap<>();
        deadEntityImages = new HashMap<>();
        tileImages = new HashMap<>();
        getTileImage();
    }

    public void getTileImage() {
        try {
            for (CellType type : CellType.values()) {
                File f = new File(type.getImgPath());
                if (f.exists()) {
                    tileImages.put(type, ImageIO.read(f));
                } else {
                    System.out.println("Textura não encontrada: " + type.getImgPath());
                }
            }

            for (AnimalType type : AnimalType.values()) {
                if (type.getImgPath() != null) {
                    File f = new File(type.getImgPath());
                    if (f.exists()) entityImages.put(type, ImageIO.read(f));
                }

                if (type.getDeadImgPath() != null) {
                    File f = new File(type.getDeadImgPath());
                    if (f.exists()) deadEntityImages.put(type, ImageIO.read(f));
                }
            }

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
        var grid = gp.getWorld().getGrid();
        var entitiesMap = gp.getWorld().getEntities();
        int tileSize = gp.getTileSize();
        ArrayList<Obstacle> obstacles = gp.getWorld().getObstacles();

        if (grid == null) return;

        int entitySize = (int)(tileSize * 0.8);
        int offset = (tileSize - entitySize) / 2;

        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                Cell cell = grid.get(y).get(x);
                int worldX = x * tileSize;
                int worldY = y * tileSize;

                BufferedImage tileImg = tileImages.get(cell.getType());

                if (tileImg != null) {
                    // Caso especial para a Rocha que você desenha maior
                    if (cell.getType() == CellType.ROCK) {
                        // A sua lógica original de desenhar a rocha apenas na coordenada inicial
                        for(Obstacle o: obstacles) {
                            if(o instanceof Rock) {
                                Rock Tryrock = (Rock) o;
                                if(Tryrock.getStartCoord().getX() == x && Tryrock.getStartCoord().getY() == y) {
                                    // Desenha 2x maior
                                    g2.drawImage(tileImg, worldX, worldY, tileSize * 2, tileSize * 2, null);
                                }
                            }
                        }
                    } else {
                        // Desenho padrão (Grama, Água)
                        g2.drawImage(tileImg, worldX, worldY, tileSize, tileSize, null);
                    }
                } else {
                    // Fallback se não tiver imagem (usando cores)
                    if (cell.getType() == CellType.WATER) g2.setColor(Color.BLUE);
                    else if (cell.getType() == CellType.GRASS) g2.setColor(new Color(34, 139, 34));
                    else g2.setColor(Color.GRAY);

                    g2.fillRect(worldX, worldY, tileSize, tileSize);
                }

                if (cell.getCurrentOcupant() != LayerType.NONE) {
                    Entity e = entitiesMap.get(cell.getCoord());

                    if (e instanceof Animal) {
                        Animal a = (Animal) e;
                        AnimalType type = a.getType();

                        BufferedImage imgToDraw = null;

                        if (a.getIsDead()) {
                            imgToDraw = deadEntityImages.get(type);
                        } else {
                            imgToDraw = entityImages.get(type);
                        }

                        if (imgToDraw != null) {
                            g2.drawImage(imgToDraw, worldX + offset, worldY + offset, entitySize, entitySize, null);
                        } else {
                            if (a.getIsDead()) g2.setColor(Color.BLACK);
                            else g2.setColor(type.getStatTitleColor());
                            g2.fillOval(worldX + offset, worldY + offset, entitySize, entitySize);
                        }
                    }
                    else if (e instanceof Plant) {
                        Plant p = (Plant) e;
                        PlantType type = p.getType();

                        if (entityImages.containsKey(type)) {
                            g2.drawImage(entityImages.get(type), worldX + offset, worldY + offset, entitySize, entitySize, null);
                        } else {
                            g2.setColor(type.getStatTitleColor());
                            g2.fillRect(worldX + offset, worldY + offset, entitySize, entitySize);
                        }
                    }
                }
            }
        }
    }
}