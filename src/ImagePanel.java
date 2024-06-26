/*
 * Programmer: Ayan Balaji Kani, Charlie Kampp
 * Date: May 9, 2024
 * Purpose: This program will display the map.
 */


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class ImagePanel extends JPanel {
    private final Image backgroundImage;
    private BufferedImage pngImage;
    private int score;
    private JLabel scoreLabel;
    private List<Point> clickPoints = new ArrayList<>();
    private Clip boom;
    private Clip click;
    private Clip oops;

    public ImagePanel(String imagePath, String pngImagePath, Main main) throws IOException {
        backgroundImage = ImageIO.read(new File(imagePath));
        pngImage = ImageIO.read(new File(pngImagePath));

        setLayout(null);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/nuke.wav"));
            boom = AudioSystem.getClip();
            boom.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            throw new IOException("Error loading sound file.");
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/click.wav"));
            click = AudioSystem.getClip();
            click.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            throw new IOException("Error loading sound file.");
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/oops.wav"));
            oops = AudioSystem.getClip();
            oops.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            throw new IOException("Error loading sound file.");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickPoints.add(e.getPoint());
                System.out.println(e.getX());
                System.out.println(e.getY());
                repaint();
                boom();
                click();
                if (1325 > e.getX() && e.getX() > 1249 && 412 > e.getY() && e.getY() > 279) {
                    oops();
                }
            }
        });

        JButton closeButton = new JButton("X");
        closeButton.setBounds(10, 5, 100, 100);
        closeButton.setFont(new Font("Monospaced", Font.PLAIN, 50));
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(Color.RED);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit to menu?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                CardLayout layout = (CardLayout) getParent().getLayout();
                layout.show(getParent(), "controls");
            }
        });
        add(closeButton);

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(1500, 40, 300, 30);
        add(scoreLabel);

        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        for (Point point : clickPoints) {
            int x = (int) point.getX() - 25;
            int y = (int) point.getY() - 25;
            g.drawImage(pngImage, x, y, 50, 50, this);
        }
    }

    // Method to update the score displayed on the JLabel
    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("Score: " + score);
    }

    // Method to play the sound effect
    private void boom() {
        if (boom != null) {
            boom.stop(); // Stop the clip if it's currently playing
            boom.setFramePosition(0); // Rewind to the beginning
            boom.start(); // Start playing the clip
        }
    }

    private void click() {
        if (click != null) {
            click.stop(); // Stop the clip if it's currently playing
            click.setFramePosition(0); // Rewind to the beginning
            click.start(); // Start playing the clip
        }
    }

    private void oops() {
        if (oops != null) {
            oops.stop(); // Stop the clip if it's currently playing
            oops.setFramePosition(0); // Rewind to the beginning
            oops.start(); // Start playing the clip
        }
    }
}
