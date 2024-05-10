/*
 * Programmer: Ayan Balaji Kani, Charlie Kampp
 * Date: May 9, 2024
 * Purpose: This program will be house the main logic and questions.
 */

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends JFrame {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private ImagePanel imagePanel;
    private Endscreen endScreen;
    private HelpScreen helpScreen;
    private Clip backgroundMusic;
    private Clip wrong;
    private Clip fail;
    private Clip success;
    private String[] questions;
    private String[] countries;
    private int[] x1s;
    private int[] y1s;
    private int[] x2s;
    private int[] y2s;
    private int x = -1;
    private int y = -1;
    private int lastDisplayedQuestionIndex = -1;
    private int score = 0;

    private List<Integer> list; // Track questions to prevent repetition

    public Main() {
        setTitle("Nuke A Country");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new JPanel(new CardLayout());
        cardLayout = (CardLayout) cards.getLayout();
        add(cards);

        ControlPanel controlPanel = new ControlPanel(this); // Pass Main instance to ControlPanel
        cards.add(controlPanel, "controls");

        try {
            imagePanel = new ImagePanel("src/world-map-pro.jpg", "src/blast.png", this);
            setupImagePanel();
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

        // Initialize HelpScreen
        try {
            helpScreen = new HelpScreen(this);
            cards.add(helpScreen, "Help");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize question data
        initializeQuestionData();

        list = new ArrayList<>(); 

        helpScreen.getReadyButton().addActionListener(e -> {
            // Randomly select a question index
            int questionIndex = getRandomQuestionIndex();

            // Display the question
            if (questionIndex != -1) {
                showMessage(questions[questionIndex], "Question", questionIndex);
                list.add(questionIndex); // Add the index to prevent repetition
            }

            // Switch to the image panel
            cardLayout.show(cards, "Image");
            imagePanel.requestFocusInWindow();
        });

        // Load background music
        loadBackgroundMusic();
    }

    private void initializeQuestionData() {
        // Question and cords
        questions = new String[]{"IKEA was founded in which country?", "What is the world's largest country?", "What is the first country that voluntarily abandoned its nuclear weapons program?", "In which country is the Grand Canyon located in?", "What is the most populous country?", "What country does France share its longest land border with?", "Which country has the longest runtime of a nuclear fusion reactor?", "Which country has the longest coastline?", "Which country was originally called New South Whales?", "In which country is Rome located in?", "In which country does the Amazon River start in?", "In which country is is the Nile River Delta located in?", "What country did Genghis Khan Rule?", "Which country has the largest castle?", "What country is known as the Land of Fire and Ice?"};
        countries = new String[]{                            "Sweden",                               "Russia",                                                                     "South Africa",                                           "America",                              "India",                                                       "Brazil",                                                        "South Korea",                                   "Canada",                                             "Australia",                                "Italy",                                             "Peru",                                                   "Egypt",                            "Mongolia",                                "Poland",                                            "Iceland"};
        x1s = new int[]{                                          757,                                    828,                                                                                759,                                                 208,                                  998,                                                            427,                                                                 1210,                                        138,                                                    1151,                                    737,                                                365,                                                       813,                                  1058,                                     769,                                                  597};
        y1s = new int[]{                                          164,                                      2,                                                                                677,                                                 302,                                  389,                                                            557,                                                                  367,                                          0,                                                     608,                                    316,                                                568,                                                       400,                                   279,                                     263,                                                  157};
        x2s = new int[]{                                          799,                                   1439,                                                                                855,                                                 439,                                 1075,                                                            579,                                                                 1249,                                        512,                                                    1376,                                    791,                                                430,                                                       866,                                  1205,                                     810,                                                  667};
        y2s = new int[]{                                          251,                                    280,                                                                                741,                                                 440,                                  526,                                                            702,                                                                  412,                                        298,                                                     804,                                    372,                                                655,                                                       450,                                   348,                                     298,                                                  210};
    }

    private int getRandomQuestionIndex() {
        if (list.size() == questions.length) {
            if (score == list.size()) {
                cardLayout.show(cards, "End");
                backgroundMusic.stop();
                success();
                showMessage("Good Job! You Are A Geography Pro! Score: " + score + "/15", "No Questions Left", -1);
                cardLayout.show(cards, "controls");
            } else {
                cardLayout.show(cards, "End");
                backgroundMusic.stop();
                showMessage("Almost there! Just " + (list.size() - score) + " More! Score: " + score + "/15", "No Questions Left", -1);
                fail();
                cardLayout.show(cards, "controls");
            }
            return -1; // No questions left to ask
        }

        List<Integer> oddindexes = new ArrayList<>();
        List<Integer> evenindexes = new ArrayList<>();

        // Splitting indexes into odd and even lists
        for (int i = 0; i < questions.length; i++) {
            if (!list.contains(i)) {
                if (i % 2 != 0) {
                    oddindexes.add(i);
                } else {
                    evenindexes.add(i);
                }
            }
        }

        // Prioritize odd indexes before even ones
        if (!oddindexes.isEmpty()) {
            return oddindexes.get(new Random().nextInt(oddindexes.size()));
        } else if (!evenindexes.isEmpty()) {
            return evenindexes.get(new Random().nextInt(evenindexes.size()));
        } else {
            // In case no new questions are left
            return -1;
        }
    }


    private void showMessage(String message, String title, int questionIndex) {
        JPanel panel = new JPanel(new BorderLayout());

        // Creating JLabel with center-aligned text using HTML
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 1000px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Change font size here
        panel.add(messageLabel, BorderLayout.CENTER);

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
                    list.add(nextQuestionIndex);
                }
            }
        });

        // Create a panel to hold the button, allowing for custom size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Determine the preferred size based on the content
        panel.setPreferredSize(new Dimension(1300, panel.getPreferredSize().height + 100));

        JDialog dialog = new JDialog(this, title, true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void setupImagePanel() {
        // Add mouse listener
        imagePanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                checkBounds(x, y);
            }
        });
        
        // Add key listener
        imagePanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // Show the last displayed question again
                    if (lastDisplayedQuestionIndex != -1) {
                        showMessage(questions[lastDisplayedQuestionIndex], "Question", lastDisplayedQuestionIndex);
                    } else {
                        showMessage("No question was displayed", "Error", -1);
                    }
                }
            }
        });

        imagePanel.setFocusable(true);
    }

    protected void checkBounds(int x, int y) {
        if (lastDisplayedQuestionIndex != -1) {
            if (x >= x1s[lastDisplayedQuestionIndex] && x <= x2s[lastDisplayedQuestionIndex] &&
                    y >= y1s[lastDisplayedQuestionIndex] && y <= y2s[lastDisplayedQuestionIndex]) {
                score++;
                imagePanel.updateScore(score);
                showMessage("Correct!", "Bounds Check", -1);
            } else {
                wrong();
                showMessage("Oops! Maybe Next Time! - Answer: " + countries[lastDisplayedQuestionIndex], "Bounds Check", -1);
            }
        } else {
            showMessage("No question was displayed", "Error", -1);
        }
    }

    private void loadBackgroundMusic() {
        try {
            // Load audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/freebird.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            // Set volume to 20%
            FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-8.0f);
            // Loop the music
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void wrong() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/wrong.wav"));
            wrong = AudioSystem.getClip();
            wrong.open(audioInputStream);
            wrong.stop(); // Stop the clip if it's currently playing
            wrong.setFramePosition(0); // Rewind to the beginning
            wrong.start(); // Start playing the clip
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fail() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/failed.wav"));
            fail = AudioSystem.getClip();
            fail.open(audioInputStream);
            fail.stop(); // Stop the clip if it's currently playing
            fail.setFramePosition(0); // Rewind to the beginning
            fail.start(); // Start playing the clip
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void success() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/success.wav"));
            success = AudioSystem.getClip();
            success.open(audioInputStream);
            success.stop(); // Stop the clip if it's currently playing
            success.setFramePosition(0); // Rewind to the beginning
            success.start(); // Start playing the clip
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to show the HelpScreen
    public void showHelpScreen() {
        cardLayout.show(cards, "Help");
    }

    public void showControlPanel() {
        cardLayout.show(cards, "controls");
    }
    
    public void showImagePanel() {
    	cardLayout.show(cards, "Image");
        imagePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}
