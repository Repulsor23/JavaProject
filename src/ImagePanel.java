//image
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private final Image image;

    public ImagePanel(String imagePath, Main main) throws IOException {
        image = ImageIO.read(new File(imagePath));
        setLayout(null);


        JButton closeButton = new JButton("X");
        closeButton.setBounds(10, 10, 50, 50);
        closeButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
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

        // Add KeyListener to handle Enter key press

        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}
