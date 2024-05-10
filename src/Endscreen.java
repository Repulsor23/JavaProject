import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Endscreen extends JPanel {
    private final Image image;
    private final Image mission;

    public Endscreen(String imagePath, Main main) throws IOException {
        image = ImageIO.read(new File(imagePath));
        setLayout(null);

        mission = ImageIO.read(new File("src/success.png"));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(mission, 0, 0, getWidth(), getHeight(), this);
    }
}
