package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import world.World;
import world.stat.SpecieStat;

public class GamePanel extends JPanel implements Runnable {

    private final int originalTileSize = 16;
    private final int scale = 2;
    private final int tileSize = originalTileSize * scale;
    private final int maxScreenCol = 30;
    private final int maxScreenRow = 30;
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    private int FPS = 10;
    private Thread gameThread;
    private World world;
    TileManager tileM;
    private StatsPanel statsPanel;

    private boolean isRunning = false;
    private SimulationMode mode = SimulationMode.CONTINUOUS;
    private int targetYear = 0;
    private String targetSpecies = "";

    public GamePanel(StatsPanel statsPanel) throws UnsupportedLookAndFeelException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        initWorld();

        tileM = new TileManager(this);

        UIManager.setLookAndFeel(new NimbusLookAndFeel());
    }

    private void initWorld() {
        world = new World(maxScreenCol);
        world.worldGen();
    }

    public void restartGame() {
        isRunning = false;

        if (statsPanel != null) {
            statsPanel.setWorld(world);
        }
        repaint();
    }

    public void setFPS(int fps) {
        if (fps > 0) {
            this.FPS = fps;
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void startContinuous() {
        this.mode = SimulationMode.CONTINUOUS;
        this.isRunning = true;
    }

    public void startYears(int yearsToRun) {
        this.mode = SimulationMode.YEAR_LIMIT;
        this.targetYear = world.getStats().getCurrYear() + yearsToRun;
        this.isRunning = true;
    }

    public void startUntilExtinction(String speciesName) {
        this.mode = SimulationMode.EXTINCTION;
        this.targetSpecies = speciesName;
        this.isRunning = true;
    }

    public void pauseSimulation() {
        this.isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            double drawInterval = 1000000000.0 / FPS;

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                if (isRunning) {
                    update();
                    checkStopConditions();
                }
                repaint();
                if (statsPanel != null) statsPanel.repaint();
                delta--;
            }
        }
    }

    private void checkStopConditions() {
        if (mode == SimulationMode.YEAR_LIMIT) {
            if (world.getStats().getCurrYear() >= targetYear) {
                isRunning = false;
                System.out.println("Simulação parada: Limite de anos atingido.");
            }
        }
        else if (mode == SimulationMode.EXTINCTION) {
            SpecieStat s = world.getStats().getSpecieStat().get(targetSpecies);
            if (s == null || s.getCurrent_alive() <= 0) {
                isRunning = false;
                System.out.println("Simulação parada: " + targetSpecies + " extinta.");
            }
        }
    }

    public void update() {
        world.updateEntities();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        g2.dispose();
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

    public World getWorld() {
        return world;
    }

    public int getTileSize(){
        return tileSize;
    }
}