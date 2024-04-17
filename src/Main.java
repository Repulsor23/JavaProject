import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private ImagePanel imagePanel;

    // Arrays to hold question data
    private String[] questions;
    private String[] countries;
    private int[] x1s;
    private int[] y1s;
    private int[] x2s;
    private int[] y2s;
    private boolean[] isEuropes;

    private List<Integer> askedIndices; // Track asked indices to prevent repetition

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

        // Initialize question data
        initializeQuestionData();

        askedIndices = new ArrayList<>(); // Initialize asked indices list

        controlPanel.getStartButton().addActionListener(e -> {
            // Randomly select a question index
            int questionIndex = getRandomQuestionIndex();

            // Display the question
            if (questionIndex != -1) {
                JOptionPane.showMessageDialog(null, questions[questionIndex], "Question", JOptionPane.INFORMATION_MESSAGE);

                // Mark the question as asked
                askedIndices.add(questionIndex);
            }

            // Switch to the image panel
            cardLayout.show(cards, "Image");
            imagePanel.requestFocusInWindow();
            setupImagePanel();
        });
    }

    private void initializeQuestionData() {
        // Sample question data (replace with your actual data)
        questions = new String[]{"IKEA was founded in which country?", "What is the world's largest country?", "What is the only country that voluntarily abandoned its nuclear weapons program?"};
        countries = new String[]{"Sweden", "Russia", "South Africa" };
        x1s = new int[]{831, 1017, 911};
        y1s = new int[]{223, 91, 849};
        x2s = new int[]{941, 1706, 999};
        y2s = new int[]{484, 331, 903};
    }

    private int getRandomQuestionIndex() {
        if (askedIndices.size() == questions.length) {
            JOptionPane.showMessageDialog(null, "All questions have been asked.", "No Questions Left", JOptionPane.INFORMATION_MESSAGE);
            return -1; // No questions left to ask
        }

        Random random = new Random();
        int index;
        do {
            index = random.nextInt(questions.length);
        } while (askedIndices.contains(index)); // Ensure the question hasn't been asked before

        return index;
    }

    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Check if the click satisfies the bounds for the selected country
                checkBounds(e.getX(), e.getY());
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
    }

    protected void checkBounds(int x, int y) {
        if (questions != null && questions.length > 0) {
            boolean found = false;
            for (int i = 0; i < questions.length; i++) {
                if (x >= x1s[i] && x <= x2s[i] && y >= y1s[i] && y <= y2s[i]) {
                    JOptionPane.showMessageDialog(null, "Clicked within bounds of " + countries[i], "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Clicked outside bounds", "Bounds Check", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}
