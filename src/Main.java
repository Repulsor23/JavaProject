import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class Main extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private ImagePanel imagePanel;

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
            imagePanel = new ImagePanel("src/world-map-pro.jpeg");
            setupImagePanel(); // Setup mouse and key listeners for ImagePanel
            cards.add(imagePanel, "Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add EuropePanel
        try {
            ImagePanel europePanel = new ImagePanel("src/europe-map.jpg");
            setupEuropePanel(europePanel); // Setup mouse listener for EuropePanel
            cards.add(europePanel, "Europe");
        } catch (IOException e) {
            e.printStackTrace();
        }

        controlPanel.getStartButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
                imagePanel.requestFocusInWindow(); // Request focus when switching back to ImagePanel
                setupImagePanel(); // Reattach KeyListener and MouseListener
            }
        });
    }

    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // Changed mouseClicked to mousePressed
                cardLayout.show(cards, "Europe");
            }
        });

        // Remove existing KeyListener (if any)
        KeyListener[] keyListeners = imagePanel.getKeyListeners();
        for (KeyListener listener : keyListeners) {
            imagePanel.removeKeyListener(listener);
        }

        // Set up key bindings
        imagePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        imagePanel.getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
            }
        });

        imagePanel.setFocusable(true); // Ensure panel can receive key events
    }

    private void setupEuropePanel(ImagePanel europePanel) {
        // Set up key bindings for EuropePanel
        europePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        europePanel.getActionMap().put("escapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
                setupImagePanel(); // Reattach key bindings for ImagePanel
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}