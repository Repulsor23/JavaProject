import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HelpScreen extends JPanel {

    private final Main main;

    public HelpScreen(Main main) throws IOException {
        this.main = main;


    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.pink);
        g.drawRect(0, 0, getWidth(), getHeight());
    }
}
