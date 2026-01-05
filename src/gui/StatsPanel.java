package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;
import world.World;
import world.stat.GlobalStat;
import world.stat.SpecieStat;
import world.stat.YearStat;

public class StatsPanel extends JPanel {

    World world;
    public final int width = 250; // Aumentei um pouco a largura para caberem os detalhes
    public final int height;

    public StatsPanel(World world, int height) {
        this.world = world;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(30, 30, 30)); // Cinzento quase preto
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (world == null || world.getStats() == null) return;

        // Configuração Base
        int x = 10;
        int y = 20;
        int lineHeight = 18; // Linhas mais compactas

        // --- TÍTULO E ANO ---
        g.setFont(new Font("Consolas", Font.BOLD, 20)); // Fonte monospaced fica melhor para números
        g.setColor(Color.ORANGE);
        g.drawString("ANO: " + world.getStats().getCurrYear(), x, y);
        y += 30;

        // Obter os objetos de estatística
        GlobalStat global = world.getStats().getGlobalStats();
        YearStat year = world.getStats().getCurrYearStat();
        Map<String, SpecieStat> speciesMap = world.getStats().getSpecieStat();

        // --- ESTATÍSTICAS DO ANO ATUAL ---
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.setColor(Color.CYAN);
        g.drawString("[ NESTE ANO ]", x, y);
        y += lineHeight;

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.drawString("Nascimentos : " + year.getBorn_this_turn(), x, y); y += lineHeight;
        g.drawString("Mortes      : " + year.getDied_this_turn(), x, y); y += lineHeight;
        g.drawString("Comidos     : " + year.getEaten_this_turn(), x, y); y += lineHeight + 5;

        // --- ESTATÍSTICAS GLOBAIS ---
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.setColor(Color.MAGENTA);
        g.drawString("[ GLOBAL TOTAL ]", x, y);
        y += lineHeight;

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.drawString("Vivos Agora : " + global.getCurrent_entities_alive(), x, y); y += lineHeight;
        g.drawString("Criados Tot : " + global.getTotal_entities_ever_created(), x, y); y += lineHeight;
        g.drawString("Mortos Tot  : " + global.getTotal_deaths(), x, y); y += lineHeight;
        g.drawString("Reprod. Tot : " + global.getTotal_born_reproduction(), x, y); y += lineHeight + 10;

        // --- DETALHES POR ESPÉCIE ---
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.setColor(Color.GREEN);
        g.drawString("[ POR ESPÉCIE ]", x, y);
        y += lineHeight + 5;

        for (Map.Entry<String, SpecieStat> entry : speciesMap.entrySet()) {
            String name = entry.getKey();
            SpecieStat s = entry.getValue();

            // Título da Espécie
            g.setFont(new Font("Consolas", Font.BOLD, 13));
            if (name.contains("WOLF")) g.setColor(new Color(255, 100, 100));
            else if (name.contains("SHEEP")) g.setColor(Color.LIGHT_GRAY);
            else if (name.contains("PLANT")) g.setColor(new Color(100, 255, 100));
            else g.setColor(Color.YELLOW);

            g.drawString("--- " + name + " ---", x, y);
            y += lineHeight;

            // Dados da Espécie
            g.setFont(new Font("Consolas", Font.PLAIN, 11));
            g.setColor(Color.LIGHT_GRAY);

            // Coluna 1: Vivos e Total
            g.drawString("Vivos: " + s.getCurrent_alive() + " | Tot: " + s.getTotal_created(), x, y);
            y += lineHeight;

            // Coluna 2: Causas de Morte
            if (s.getDeaths() > 0) {
                g.setColor(new Color(180, 180, 180));
                g.drawString("Mortes: " + s.getDeaths() + " (Comidos: " + s.getEaten() + ")", x, y);
                y += lineHeight;

                // Detalhes das mortes (recuado)
                g.setColor(new Color(150, 150, 150));
                g.drawString(" Causas:", x, y);
                y += lineHeight;
                g.drawString("  Fome:" + s.getStarved() + " Sede:" + s.getThirst(), x, y);
                y += lineHeight;
                g.drawString("  Ene:" + s.getEnergy() + " Nat:" + s.getNatural(), x, y);
                y += lineHeight;
            } else {
                g.drawString("Sem mortes registadas.", x, y);
                y += lineHeight;
            }

            y += 5; // Espaço entre espécies
        }
    }
}