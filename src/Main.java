import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Main extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private ImagePanel imagePanel;
    private ImagePanel europePanel;

    public Main() {
        setTitle("Nuke A Country");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new JPanel(new CardLayout());
        cardLayout = (CardLayout) cards.getLayout();

        add(cards);

        ControlPanel controlPanel = new ControlPanel();
        cards.add(controlPanel, "controls");

        try {
            imagePanel = new ImagePanel("src/world-map-pro.jpg");
            setupImagePanel();
            cards.add(imagePanel, "Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            europePanel = new ImagePanel("src/europe-map.jpg");
            setupEuropePanel();
            cards.add(europePanel, "Europe");
        } catch (IOException e) {
            e.printStackTrace();
        }

        controlPanel.getStartButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
                imagePanel.requestFocusInWindow();
                setupImagePanel();
            }
        });
    }

    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Check if the mouse click is within the specified region
                int startX = 706; // Starting X coordinate
                int endX = 1065; // Ending X coordinate
                int startY = 180; // Starting Y coordinate
                int endY = 445; // Ending Y coordinate
                if (e.getX() >= startX && e.getX() <= endX &&
                        e.getY() >= startY && e.getY() <= endY) {
                    // Mouse click is within the specified region, perform the desired action
                    cardLayout.show(cards, "Europe");
                }
            }
        });

        // Remove the previous setup and key listeners
        KeyListener[] keyListeners = imagePanel.getKeyListeners();
        for (KeyListener listener : keyListeners) {
            imagePanel.removeKeyListener(listener);
        }

        // Add key listener to handle ESC key
        imagePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        imagePanel.getActionMap().put("escapePressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
            }
        });

        imagePanel.setFocusable(true);
    }

    private void setupEuropePanel() {
        europePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Handle clicks in EuropePanel here
                // Get the coordinates relative to the EuropePanel
                int x = e.getX();
                int y = e.getY();
                // Print out the coordinates
                System.out.println("Coordinates in EuropePanel: (" + x + ", " + y + ")");
            }
        });

        // Add key listener to handle ESC key
        europePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        europePanel.getActionMap().put("escapePressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
                setupImagePanel();
            }
        });

        europePanel.setFocusable(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}