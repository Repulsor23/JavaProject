import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Random;
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
            imagePanel = new ImagePanel("src/world-map-pro.jpg", this);
            setupImagePanel();
            cards.add(imagePanel, "Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            europePanel = new EuropePanel("src/europe-map.jpg", this);
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
            switchToImagePanel();
        });



    }



    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Check if the click satisfies the bounds for the selected country
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

        imagePanel.setFocusable(true);

        // Add KeyListener to handle Enter key press
        imagePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Get the last clicked coordinates from the ImagePanel
                    int x1 = imagePanel.getLastClickedX1();
                    int y1 = imagePanel.getLastClickedY1();
                    int x2 = imagePanel.getLastClickedX2();
                    int y2 = imagePanel.getLastClickedY2();
                    boolean europe = false;
                    // Perform bounds check using the last clicked coordinates
                    checkBounds(x1, y1, x2, y2, europe);
                }
            }
        });
    }

    private void setupEuropePanel() {
        europePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Get the last clicked coordinates from the ImagePanel
                    int x1 = europePanel.getLastClickedX1();
                    int y1 = europePanel.getLastClickedY1();
                    int x2 = europePanel.getLastClickedX2();
                    int y2 = europePanel.getLastClickedY2();
                    boolean europe = true;
                    // Perform bounds check using the last clicked coordinates
                    checkBounds(x1, y1, x2, y2, europe);
                }
            }
        });

        europePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapePressed");
        europePanel.getActionMap().put("escapePressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                switchToImagePanel();
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

    protected void checkBounds(int x1, int y1, int x2, int y2, boolean europe) {

        if (questionDataList != null && !questionDataList.isEmpty()) {
            for (QuestionData questionData : questionDataList) {
                if (europe == questionData.isEurope()) {
                    if (x1 >= questionData.getX1() && x2 <= questionData.getX2() && y1 >= questionData.getY1() && y2 <= questionData.getY2()) {
                        JOptionPane.showMessageDialog(null, "Clicked within bounds of " + questionData.getCountry(), "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Clicked outside bounds", "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void switchToImagePanel() {
        cardLayout.show(cards, "Image");
        imagePanel.requestFocusInWindow();
        setupImagePanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}