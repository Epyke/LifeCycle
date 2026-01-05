package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {
        try {
            // --- CARREGAR IMAGENS ---
            // Certifica-te que os nomes coincidem exatamente com os ficheiros na pasta res
            File fGrass = new File("res/Grass.png");
            File fRock = new File("res/Rock.png"); // Imagem 2x2 (128px)
            File fPlant = new File("res/Plants.png");
            File fWater = new File("res/Water.png");
            File fFox = new File("res/Fox.png");     // Lobo
            File fBunny = new File("res/Bunny.png"); // Ovelha
            File fCarrot = new File("res/Carrot.png");

            // --- INICIALIZAR TILES ---

            // TILE 0: Vazio/Default
            tiles[0] = new Tile();
            tiles[0].collision = true;

            // TILE 1: RELVA
            tiles[1] = new Tile();
            if (fGrass.exists()) tiles[1].image = ImageIO.read(fGrass);
            else System.out.println("Erro: Grass.png não encontrado.");

            // TILE 2: ROCHA (Multitile)
            tiles[2] = new Tile();
            if (fRock.exists()) tiles[2].image = ImageIO.read(fRock);
            else System.out.println("Erro: Rock.png não encontrado.");
            tiles[2].collision = true;

            // TILE 3: PLANTA (Arbusto/Berry)
            tiles[3] = new Tile();
            if (fPlant.exists()) tiles[3].image = ImageIO.read(fPlant);
            else System.out.println("Aviso: Plants.png não encontrada.");

            // TILE 4: ÁGUA
            tiles[4] = new Tile();
            if (fWater.exists()) tiles[4].image = ImageIO.read(fWater);
            else System.out.println("Aviso: Water.png não encontrada.");
            tiles[4].collision = true;

            // TILE 5: LOBO (Fox sprite)
            tiles[5] = new Tile();
            if (fFox.exists()) tiles[5].image = ImageIO.read(fFox);
            else System.out.println("Aviso: Fox.png não encontrada.");

            // TILE 6: OVELHA (Bunny sprite)
            tiles[6] = new Tile();
            if (fBunny.exists()) tiles[6].image = ImageIO.read(fBunny);
            else System.out.println("Aviso: Bunny.png não encontrada.");

            // TILE 7: CENOURA
            tiles[7] = new Tile();
            if (fCarrot.exists()) tiles[7].image = ImageIO.read(fCarrot);
            else System.out.println("Aviso: Carrot.png não encontrada.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Auxiliares mantêm-se iguais
    private boolean isRock(int x, int y, ArrayList<ArrayList<Cell>> grid) {
        if (x < 0 || y < 0 || y >= grid.size() || x >= grid.get(y).size()) return false;
        return grid.get(y).get(x).getType() == CellType.ROCK;
    }

    private boolean isBigRockTopLeft(int x, int y, ArrayList<ArrayList<Cell>> grid) {
        return isRock(x, y, grid) && isRock(x + 1, y, grid) && isRock(x, y + 1, grid) && isRock(x + 1, y + 1, grid);
    }

    public void draw(Graphics2D g2) {
        var grid = gp.world.getGrid();
        var entitiesMap = gp.world.getEntities();
        ArrayList<Obstacle> obstacles = gp.world.getObstacles();
        int tileSize = gp.tileSize;

        if (grid == null) return;

        // --- CÁLCULO PARA CENTRAR AS ENTIDADES ---
        // Define o tamanho da entidade como 80% do tamanho do quadrado do chão
        int entitySize = (int)(tileSize * 0.8);
        // Calcula a margem necessária para centrar a entidade no quadrado
        int offset = (tileSize - entitySize) / 2;

        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {

                Cell cell = grid.get(y).get(x);
                int worldX = x * tileSize;
                int worldY = y * tileSize;

                // ---------------------------
                // CAMADA 1: TERRENO
                // ---------------------------

                if (cell.getType() == CellType.WATER) {
                    if (tiles[4].image != null) {
                        g2.drawImage(tiles[4].image, worldX, worldY, tileSize, tileSize, null);
                    } else {
                        g2.setColor(Color.BLUE); // Fallback se não houver imagem
                        g2.fillRect(worldX, worldY, tileSize, tileSize);
                    }
                }
                else if (cell.getType() == CellType.GRASS) {
                    if (tiles[1].image != null) {
                        g2.drawImage(tiles[1].image, worldX, worldY, tileSize, tileSize, null);
                    } else {
                        g2.setColor(new Color(34, 139, 34)); // Fallback Verde
                        g2.fillRect(worldX, worldY, tileSize, tileSize);
                    }
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

                // ---------------------------
                // CAMADA 2: ENTIDADES
                // ---------------------------
                // Usamos (worldX + offset) para garantir que ficam no meio

                if (cell.getCurrentOcupant() == LayerType.ANIMAL) {
                    Entity e = entitiesMap.get(cell.getCoord());
                    if (e instanceof Animal) {
                        Animal a = (Animal) e;

                        // LOBO (Usa a sprite da Raposa - Tile 5)
                        if (a.getType() == AnimalType.FOX) {
                            if (tiles[5].image != null) {
                                g2.drawImage(tiles[5].image, worldX + offset, worldY + offset, entitySize / 2, entitySize / 2, null);
                            } else {
                                g2.setColor(Color.DARK_GRAY);
                                g2.fillOval(worldX + offset, worldY + offset, entitySize, entitySize);
                            }
                        }
                        // OVELHA (Usa a sprite do Coelho - Tile 6)
                        else if (a.getType() == AnimalType.BUNNY) {
                            if (tiles[6].image != null) {
                                g2.drawImage(tiles[6].image, worldX + offset, worldY + offset, entitySize / 2, entitySize / 2, null);
                            } else {
                                g2.setColor(Color.WHITE);
                                g2.fillOval(worldX + offset, worldY + offset, entitySize, entitySize);
                            }
                        }
                    }
                }
                else if (cell.getCurrentOcupant() == LayerType.PLANT) {
                    Entity e = entitiesMap.get(cell.getCoord());
                    if(e instanceof Plant){
                        Plant p = (Plant) e;

                        // ARBUSTO (Tile 3)
                        if(p.getType() == PlantType.PLANT){
                            if(tiles[3].image != null) {
                                g2.drawImage(tiles[3].image, worldX + offset, worldY + offset, entitySize, entitySize, null);
                            } else {
                                g2.setColor(Color.GREEN);
                                g2.fillRect(worldX + offset, worldY + offset, entitySize, entitySize);
                            }
                        }
                        // CENOURA (Tile 7) - Nota: Verifica se no teu Enum é CAROT ou CARROT
                        // Tenta usar CARROT se tiveres erro no CAROT
                        else if (p.getType().toString().equals("CAROT") || p.getType().toString().equals("CARROT")){
                            if(tiles[7].image != null) {
                                g2.drawImage(tiles[7].image, worldX + offset, worldY + offset, entitySize, entitySize, null);
                            } else {
                                g2.setColor(Color.ORANGE);
                                g2.fillRect(worldX + offset, worldY + offset, entitySize, entitySize);
                            }
                        }
                    }
                }
            }
        }
    }
}