import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private ImagePanel imagePanel;
    private EuropePanel europePanel;
    private List<QuestionData> questionDataList;

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
            europePanel = new EuropePanel("src/europe-map.jpg");
            setupEuropePanel();
            cards.add(europePanel, "Europe");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load question data from CSV
        loadQuestionData();

        controlPanel.getStartButton().addActionListener(e -> {
            // Randomly select a question
            QuestionData questionData = selectRandomQuestion();
            if (questionData != null) {
                // Display the question
                JOptionPane.showMessageDialog(null, questionData.getQuestion(), "Question", JOptionPane.INFORMATION_MESSAGE);
            }
            // Switch to the image panel
            cardLayout.show(cards, "Image");
            imagePanel.requestFocusInWindow();
            setupImagePanel();
        });
    }

    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Check if the click satisfies the bounds for the selected country
                checkBounds(e.getX(), e.getY(), e.getX(), e.getY());
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
                // Check if the click satisfies the bounds for the selected country
                checkBounds(e.getX(), e.getY(), e.getX(), e.getY());
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

    private void loadQuestionData() {
        try (Stream<String> lines = Files.lines(Paths.get("src/questions.csv"))) {
            questionDataList = lines
                    .skip(1) // Skip header
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new QuestionData(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private QuestionData selectRandomQuestion() {
        if (questionDataList != null && !questionDataList.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(questionDataList.size());
            return questionDataList.get(index);
        }
        return null;
    }

    private void checkBounds(int x1, int y1, int x2, int y2) {
        if (questionDataList != null && !questionDataList.isEmpty()) {
            for (QuestionData questionData : questionDataList) {
                if (x1 >= questionData.getX1() && x2 <= questionData.getX2() &&
                        y1 >= questionData.getY1() && y2 <= questionData.getY2()) {
                    JOptionPane.showMessageDialog(null, "Clicked within bounds of " + questionData.getCountry(), "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Clicked outside bounds", "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}