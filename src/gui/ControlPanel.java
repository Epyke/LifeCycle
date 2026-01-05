package gui;

import entities.AnimalType;
import entities.PlantType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ControlPanel extends JPanel {

    GamePanel gp;
    public final int width = 200; // Largura do painel de controlo

    // Componentes
    JButton btnStartStop;
    JTextField txtYears;
    JButton btnRunYears;
    JComboBox<String> comboSpecies;
    JButton btnRunExtinction;

    public ControlPanel(GamePanel gp, int height) {
        this.gp = gp;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(40, 40, 40));

        // Layout: Uma coluna com vários botões
        this.setLayout(new GridLayout(10, 1, 10, 10)); // 10 linhas, espaçamento de 10px
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
    }

    private void initUI() {
        // Estilo dos Títulos
        TitledBorder border;

        // --- 1. CONTINUOUS ---
        btnStartStop = new JButton("SIMULAÇÃO CONTINUA");
        btnStartStop.setBackground(Color.GREEN);
        btnStartStop.addActionListener(e -> {
            if (gp.isRunning()) {
                gp.pauseSimulation();
                btnStartStop.setText("RETOMAR");
                btnStartStop.setBackground(Color.GREEN);
            } else {
                gp.startContinuous();
                btnStartStop.setText("INTERROMPER");
                btnStartStop.setBackground(Color.RED);
            }
        });
        this.add(btnStartStop);

        // Separador
        this.add(new JLabel(" "));

        // --- 2. TIME LIMIT (Anos) ---
        JPanel pnlYears = new JPanel(new GridLayout(2, 1));
        pnlYears.setBackground(getBackground());
        pnlYears.setBorder(BorderFactory.createTitledBorder(null, "Correr N Anos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        txtYears = new JTextField("50");
        btnRunYears = new JButton("INICIAR");
        btnRunYears.addActionListener(e -> {
            try {
                int y = Integer.parseInt(txtYears.getText());
                gp.startYears(y);
                btnStartStop.setText("INTERROMPER (Anos)");
                btnStartStop.setBackground(Color.ORANGE);
            } catch (NumberFormatException ex) {
                txtYears.setText("Erro");
            }
        });

        pnlYears.add(txtYears);
        pnlYears.add(btnRunYears);
        this.add(pnlYears);

        // Separador
        this.add(new JLabel(" "));

        // --- 3. EXTINCTION ---
        JPanel pnlExtinction = new JPanel(new GridLayout(2, 1));
        pnlExtinction.setBackground(getBackground());
        pnlExtinction.setBorder(BorderFactory.createTitledBorder(null, "Até Extinção", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        comboSpecies = new JComboBox<>();
        // Adicionar Animais ao dropdown
        for (AnimalType t : AnimalType.values()) comboSpecies.addItem(t.toString());
        // Adicionar Plantas ao dropdown
        for (PlantType t : PlantType.values()) comboSpecies.addItem(t.toString());

        btnRunExtinction = new JButton("Correr até Extinção");
        btnRunExtinction.addActionListener(e -> {
            String species = (String) comboSpecies.getSelectedItem();
            gp.startUntilExtinction(species);
            btnStartStop.setText("INTERROMPER (Extinção)");
            btnStartStop.setBackground(Color.ORANGE);
        });

        pnlExtinction.add(comboSpecies);
        pnlExtinction.add(btnRunExtinction);
        this.add(pnlExtinction);
    }
}