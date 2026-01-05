package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;

import entities.AnimalType;
import entities.PlantType;
import utils.Rand;
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

        int x = 10;
        int y = 20;
        int lineHeight = 18;

        // TÍTULO
        g.setFont(new Font("Consolas", Font.BOLD, 23));
        g.setColor(Color.ORANGE);
        g.drawString("ANO: " + world.getStats().getCurrYear(), x, y);
        y += 30;

        GlobalStat global = world.getStats().getGlobalStats();
        YearStat year = world.getStats().getCurrYearStat(); // Agora lê do Snapshot correto
        Map<String, SpecieStat> speciesMap = world.getStats().getSpecieStat();

        // NESTE ANO
        g.setFont(new Font("Consolas", Font.BOLD, 17));
        g.setColor(Color.CYAN);
        g.drawString("[ NESTE ANO ]", x, y);
        y += lineHeight;

        g.setFont(new Font("Consolas", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("Nascimentos : +" + year.getBorn_this_turn(), x, y); y += lineHeight;
        g.drawString("Mortes      : +" + year.getDied_this_turn(), x, y); y += lineHeight;
        g.drawString("Comidos     : +" + year.getEaten_this_turn(), x, y); y += lineHeight + 5;

        // GLOBAL
        g.setFont(new Font("Consolas", Font.BOLD, 17));
        g.setColor(Color.MAGENTA);
        g.drawString("[ GLOBAL TOTAL ]", x, y);
        y += lineHeight;

        g.setFont(new Font("Consolas", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        g.drawString("Vivos Agora : " + global.getCurrent_entities_alive(), x, y); y += lineHeight;
        g.drawString("Criados Tot : " + global.getTotal_entities_ever_created(), x, y); y += lineHeight;
        g.drawString("Mortos Tot  : " + global.getTotal_deaths(), x, y); y += lineHeight;
        g.drawString("Reprod. Tot : " + global.getTotal_born_reproduction(), x, y); y += lineHeight + 10;

        // POR ESPÉCIE
        g.setFont(new Font("Consolas", Font.BOLD, 17));
        g.setColor(Color.GREEN);
        g.drawString("[ POR ESPÉCIE ]", x, y);
        y += lineHeight + 5;

        for (Map.Entry<String, SpecieStat> entry : speciesMap.entrySet()) {
            String name = entry.getKey();
            SpecieStat s = entry.getValue();

            g.setFont(new Font("Consolas", Font.BOLD, 16));

            Color c = null; // Inicializa a null
            try {
                // Tenta achar em AnimalType
                c = AnimalType.valueOf(name).getStatTitleColor();
            } catch (IllegalArgumentException e1) {
                try {
                    // Tenta achar em PlantType
                    c = PlantType.valueOf(name).getStatTitleColor();
                } catch (IllegalArgumentException e2) {
                    // Se não existir o Enum, c continua null
                }
            }
            g.setColor(c);

            g.drawString("--- " + name + " ---", x, y);
            y += lineHeight;

            // Dados da Espécie
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            g.setColor(Color.LIGHT_GRAY);

            g.drawString("Vivos: " + s.getCurrent_alive() + " | Tot: " + s.getTotal_created(), x, y);
            y += lineHeight;

            if (s.getDeaths() > 0) {
                g.setColor(new Color(180, 180, 180));
                g.drawString("Mortes: " + s.getDeaths() + " (Comidos: " + s.getEaten() + ")", x, y);
                y += lineHeight;

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

            y += 5;
        }
    }
}