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
            setupImagePanel();
            cards.add(imagePanel, "Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ImagePanel europePanel = new ImagePanel("src/europe-map.jpg");
            setupEuropePanel(europePanel);
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
                cardLayout.show(cards, "Europe");
            }
        });

        KeyListener[] keyListeners = imagePanel.getKeyListeners();
        for (KeyListener listener : keyListeners) {
            imagePanel.removeKeyListener(listener);
        }

        imagePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        imagePanel.getActionMap().put("escapePressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
            }
        });

        imagePanel.setFocusable(true);
    }

    private void setupEuropePanel(ImagePanel europePanel) {
        europePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        europePanel.getActionMap().put("escapePressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Image");
                setupImagePanel();
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
