import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
class ImagePanel extends JPanel {
    private Image image;
    private int lastClickedX1 = -1;
    private int lastClickedY1 = -1;
    private int lastClickedX2 = -1;
    private int lastClickedY2 = -1;

    public ImagePanel(String imagePath) throws IOException {
        image = ImageIO.read(new File(imagePath));
        setLayout(null);
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
        }
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            lastClickedX2 = e.getX();
            lastClickedY2 = e.getY();
        }
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