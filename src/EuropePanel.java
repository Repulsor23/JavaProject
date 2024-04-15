import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

class EuropePanel extends JPanel {
    private Image image;
    private Point clickPoint; // To store the location of the mouse click

    // Variables to store the last clicked coordinates
    private int lastClickedX = -1;
    private int lastClickedY = -1;

    public EuropePanel(String imagePath) throws IOException {
        image = ImageIO.read(new File(imagePath));
        clickPoint = null; // Initially, no click has happened

        setLayout(null);

        // Add mouse listener to track mouse clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Store the location of the mouse click
                clickPoint = e.getPoint();
                // Update the last clicked coordinates
                lastClickedX = clickPoint.x;
                lastClickedY = clickPoint.y;
                // Print the coordinates to console
                System.out.println("Last clicked coordinates in EuropePanel: (" + lastClickedX + ", " + lastClickedY + ")");
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        // Draw the red dot if a mouse click has happened
        if (clickPoint != null) {
            g.setColor(Color.RED);
            // Draw a small red dot centered around the click point
            int dotSize = 10;
            g.fillOval(clickPoint.x - dotSize / 2, clickPoint.y - dotSize / 2, dotSize, dotSize);
        }
    }

    // Getter methods for last clicked coordinates
    public int getLastClickedX() {
        return lastClickedX;
    }

    public int getLastClickedY() {
        return lastClickedY;
    }
}
