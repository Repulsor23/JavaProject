import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HelpScreen extends JPanel {

    private final Main main;

    public HelpScreen(Main main) throws IOException {
        this.main = main;
        setBackground(Color.BLACK);
        JButton readyButton = new JButton("Im Ready!");
        readyButton.setFont(new Font("Monospaced", Font.BOLD, 100));
        readyButton.setForeground(Color.WHITE);
        readyButton.setBorderPainted(false);
        readyButton.setBackground(Color.BLACK);
        readyButton.addActionListener(e -> main.showControlPanel());
        add(readyButton);


    }
}
