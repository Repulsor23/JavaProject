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

public class ImagePanel extends JPanel {
    private final Image backgroundImage;
    private BufferedImage pngImage;
    private int score;
    private JLabel scoreLabel; // JLabel to display the score



    public ImagePanel(String imagePath, String pngImagePath, Main main) throws IOException {
        backgroundImage = ImageIO.read(new File(imagePath));
        pngImage = ImageIO.read(new File(pngImagePath));

        setLayout(null);

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

        // Add JLabel to display score
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

    }


    // Method to update the score displayed on the JLabel
    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("Score: " + score);
    }
}
