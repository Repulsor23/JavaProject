import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

class ImagePanel extends JPanel {
    private Image image;
    private Point clickPoint; // To store the location of the mouse click

    // Variables to store the last clicked coordinates
    private int lastClickedX = -1;
    private int lastClickedY = -1;

    public ImagePanel(String imagePath) throws IOException {
        image = ImageIO.read(new File(imagePath));
        clickPoint = null; // Initially, no click has happened

        setLayout(null);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(10, 10, 50, 50);
        closeButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(Color.RED);
        closeButton.setBorderPainted(false);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit to menu?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    CardLayout layout = (CardLayout) getParent().getLayout();
                    layout.show(getParent(), "controls");
                }
            }
        });

        add(closeButton);

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
                System.out.println("Last clicked coordinates in ImagePanel: (" + lastClickedX + ", " + lastClickedY + ")");
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

    }

    // Getter methods for last clicked coordinates
    public int getLastClickedX() {
        return lastClickedX;
    }

    public int getLastClickedY() {
        return lastClickedY;
    }
}
