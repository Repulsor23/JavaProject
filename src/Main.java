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
    private Endscreen endScreen;

    // Arrays to hold question data
    private String[] questions;
    private String[] countries;
    private int[] x1s;
    private int[] y1s;
    private int[] x2s;
    private int[] y2s;
    private int lastDisplayedQuestionIndex = -1;
    private int score = 0;

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
            setupImagePanel(); // Add setupImagePanel here
            cards.add(imagePanel, "Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            endScreen = new Endscreen("src/nuke.jpg", this);
            cards.add(endScreen, "End");
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
                showMessage(questions[questionIndex], "Question", questionIndex);
                askedIndices.add(questionIndex); // Add the index to prevent repetition
            }

            // Switch to the image panel
            cardLayout.show(cards, "Image");
            imagePanel.requestFocusInWindow();
        });
    }

    private void initializeQuestionData() {
        // Question and cords
        questions = new String[]{"IKEA was founded in which country?", "What is the world's largest country?", "What is the only country that voluntarily abandoned its nuclear weapons program?"};
        countries = new String[]{"Sweden", "Russia", "South Africa" };
        x1s = new int[]{831, 1017, 911};
        y1s = new int[]{223, 91, 849};
        x2s = new int[]{941, 1706, 999};
        y2s = new int[]{484, 331, 903};
    }

    private int getRandomQuestionIndex() {
        if (askedIndices.size() == questions.length) {
            if (score == askedIndices.size()){
                cardLayout.show(cards, "End");
                showMessage("Good Job! You Are A Geography Pro!", "No Questions Left",-1);
                System.exit(0);
            }
            else{
                showMessage("Almost There! Just " + (askedIndices.size() - score) + " More!", "No Questions Left", -1);
                System.exit(0);
            }
            return -1; // No questions left to ask
        }

        Random random = new Random();
        int index;
        do {
            index = random.nextInt(questions.length);
        } while (askedIndices.contains(index)); // Ensure the question hasn't been asked before

        return index;
    }

    private void showMessage(String message, String title, int questionIndex) {
        JPanel panel = new JPanel(new BorderLayout());

        // Creating JLabel with center-aligned text using HTML
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 800px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Change font size here
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create a JButton with center-aligned text using HTML
        JButton okButton = new JButton("<html><div style='text-align: center; width: 100px;'>OK</div></html>");
        okButton.setFont(new Font("Arial", Font.PLAIN, 30)); // Change button font size here
        okButton.setPreferredSize(new Dimension(200, 80)); // Set button size
        okButton.addActionListener(e -> {
            ((JDialog) panel.getTopLevelAncestor()).dispose();
            if (title.equals("Question")) {
                lastDisplayedQuestionIndex = questionIndex;
            } else if (title.equals("Bounds Check")) {
                int nextQuestionIndex = getRandomQuestionIndex();
                if (nextQuestionIndex != -1) {
                    showMessage(questions[nextQuestionIndex], "Question", nextQuestionIndex);
                    askedIndices.add(nextQuestionIndex);
                }
            }
        });

        // Create a panel to hold the button, allowing for custom size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Determine the preferred size based on the content
        panel.setPreferredSize(new Dimension(1000, panel.getPreferredSize().height + 100));

        JDialog dialog = new JDialog(this, title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private void setupImagePanel() {
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Check if the click satisfies the bounds for the selected country
                checkBounds(e.getX(), e.getY());
            }
        });
        imagePanel.setFocusable(true);
    }

    protected void checkBounds(int x, int y) {
        if (lastDisplayedQuestionIndex != -1) {
            if (x >= x1s[lastDisplayedQuestionIndex] && x <= x2s[lastDisplayedQuestionIndex] &&
                    y >= y1s[lastDisplayedQuestionIndex] && y <= y2s[lastDisplayedQuestionIndex]) {
                score++;
                imagePanel.updateScore(score); // Call updateScore method to update the score in ImagePanel
                showMessage("Correct!", "Bounds Check", -1);
            } else {
                showMessage("Wrong! Try Again", "Bounds Check", -1);
            }
        } else {
            showMessage("No question was displayed", "Error", -1);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}