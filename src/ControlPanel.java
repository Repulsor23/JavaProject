import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel {
    private final JButton startButton;
    private final ImageIcon startButtonHoverIcon;
    private final Main main; // Main instance

    public ControlPanel(Main main) {
        this.main = main; // Initialize Main instance

        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel titleLabel = new JLabel("   NukeACountry.io");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 150));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBackground(new Color(60, 63, 65));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add question mark button next to the title
        JButton helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 60));
        helpButton.setForeground(Color.BLACK);
        helpButton.setBackground(Color.GRAY);
        helpButton.setBorderPainted(false);
        helpButton.setFocusPainted(false);
        helpButton.addActionListener(e -> main.showHelpScreen()); // Call showHelpScreen method on click

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(helpButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(new Color(60, 63, 65));

        startButtonHoverIcon = new ImageIcon("src/nuke.png");
        startButton = new JButton("START");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 100));
        startButton.setForeground(Color.WHITE);
        startButton.setBorderPainted(false);
        startButton.setBackground(new Color(32, 155, 211));
        startButton.setFocusPainted(false);

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setUI(new BasicButtonUI() {
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.drawImage(startButtonHoverIcon.getImage(), 0, 0, startButton.getWidth(), startButton.getHeight(), null);
                        g2.dispose();
                        super.paint(g, c);
                    }
                });
            }

            public void mouseExited(MouseEvent e) {
                startButton.setUI(new BasicButtonUI() {
                    public void paint(Graphics g, JComponent c) {
                        super.paint(g, c);
                    }
                });
            }
        });

        startButton.addActionListener(e -> {
            startButton.setText("CONTINUE");
        });
        buttonPanel.add(startButton);

        JButton quitButton = new JButton("QUIT");
        quitButton.setFont(new Font("Monospaced", Font.BOLD, 100));
        quitButton.setForeground(Color.BLACK);
        quitButton.setBackground(new Color(255, 0, 0));
        quitButton.setFocusPainted(false);

        quitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public JButton getStartButton() {
        return startButton;
    }
}
