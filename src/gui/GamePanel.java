package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import world.World;

public class GamePanel extends JPanel implements Runnable {

    // Configurações Gráficas
    final int originalTileSize = 32;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 64x64 pixels

    // Configurações do Mundo
    // O teu World(size) define o tamanho da grid. Vamos sincronizar isso.
    public final int worldSize = 15; // Exemplo: mapa 15x15
    public final int screenWidth = tileSize * worldSize;
    public final int screenHeight = tileSize * worldSize;
    public StatsPanel statsPanel;

    // Integração com o teu Projeto
    public World world;
    Thread gameThread;
    TileManager tileM;

    // Controlo de FPS e Simulação
    int FPS = 60;
    int simulationSpeed = 60; // A cada 60 frames (1 seg), o mundo atualiza (passa um ano)
    int counter = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // INICIALIZA O TEU MUNDO AQUI
        world = new World(worldSize);
        world.worldGen(); // Gera lagos, pedras e entidades iniciais

        tileM = new TileManager(this);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();

                // 2. ADICIONA ESTA LINHA PARA ATUALIZAR O PAINEL LATERAL
                if (statsPanel != null) {
                    statsPanel.repaint();
                }

                delta--;
            }
        }
    }

    public void update() {
        // Logica para abrandar a simulação para ser visível
        counter++;
        if(counter >= simulationSpeed) {
            System.out.println("Updating World Year..."); // Debug na consola
            world.updateEntities();
            counter = 0;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2); // Desenha o estado atual do World

        g2.dispose();
    }
}