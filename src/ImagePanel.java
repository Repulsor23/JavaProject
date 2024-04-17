//image
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private final Image image;
    private int lastClickedX1 = -1;
    private int lastClickedY1 = -1;
    private int lastClickedX2 = -1;
    private int lastClickedY2 = -1;
    private final Main main;

    public ImagePanel(String imagePath, Main main) throws IOException {
        image = ImageIO.read(new File(imagePath));
        setLayout(null);
        this.main = main;

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
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    main.checkBounds(lastClickedX1, lastClickedY1);

                    clearLastClicked();
                }
            }
        });
        setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            lastClickedX1 = e.getX();
            lastClickedY1 = e.getY();
            lastClickedX2 = e.getX();
            lastClickedY2 = e.getY();

        }
    }

    // Method to clear last clicked coordinates
    private void clearLastClicked() {
        lastClickedX1 = -1;
        lastClickedY1 = -1;
        lastClickedX2 = -1;
        lastClickedY2 = -1;
    }

    public int getLastClickedX1() {
        return lastClickedX1;
    }

    public int getLastClickedY1() {
        return lastClickedY1;
    }

    public int getLastClickedX2() {
        return lastClickedX2;
    }

    public int getLastClickedY2() {
        return lastClickedY2;
    }

}
