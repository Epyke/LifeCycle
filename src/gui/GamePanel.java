package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import world.World;
import world.stat.SpecieStat;

public class GamePanel extends JPanel implements Runnable {

    // --- CONFIGURAÇÕES DE ECRÃ ---
    final int originalTileSize = 16;
    final int scale = 2; // Aumentei o scale para veres melhor os ícones de 32x32
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 30; // 30 colunas
    public final int maxScreenRow = 30; // 30 linhas
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // --- SISTEMA ---
    int FPS = 10; // Reduzi para 10 para dar tempo de ver as coisas a acontecer
    Thread gameThread;
    public World world;
    TileManager tileM;
    public StatsPanel statsPanel; // Referência para atualizar stats

    // --- VARIÁVEIS DE CONTROLO DE TEMPO (Novo!) ---
    private boolean isRunning = false; // Começa pausado
    private SimulationMode mode = SimulationMode.CONTINUOUS;
    private int targetYear = 0;
    private String targetSpecies = "";

    // Enum para os modos de simulação
    public enum SimulationMode {
        CONTINUOUS,
        YEAR_LIMIT,
        EXTINCTION
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        initWorld();

        tileM = new TileManager(this);
    }

    private void initWorld() {
        world = new World(maxScreenCol);
        world.worldGen();
    }

    public void restartGame() {
        isRunning = false; // Para a simulação
        initWorld();       // Cria um mundo novo

        // Atualiza o painel de stats com o novo mundo
        if (statsPanel != null) {
            statsPanel.setWorld(world);
        }
        repaint(); // Desenha o novo mundo (estado inicial)
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

    // --- MÉTODOS DE CONTROLO (Chamados pelo ControlPanel) ---

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
        // Mover o calculo do drawInterval para DENTRO do loop
        // para ele reagir às mudanças de velocidade
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            // Recalcula o intervalo baseado no FPS atual
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
            // Se a espécie não existe ou tem 0 vivos, paramos
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
}