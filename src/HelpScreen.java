/*
 * Programmer: Ayan Balaji Kani, Charlie Kampp
 * Date: May 9, 2024
 * Purpose: This program will show th instructions.
 */


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HelpScreen extends JPanel {

    private final Main main;
    private JButton readyButton;

    public HelpScreen(Main main) throws IOException {
        this.main = main;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        
        String text = "<html>You have been hired to bring peace to the world.<br/>Click on the country that is the answer to the question.<br/>Press the Spacebar to show question again.<br/>The future depends on you</html>";

        JLabel titleLabel = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 200));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("<html><div style='text-align: center;'>" + text + "</div></html>", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Monospaced", Font.PLAIN, 40));
        subtitleLabel.setForeground(Color.WHITE);
        add(subtitleLabel, BorderLayout.CENTER);

        readyButton = new JButton("I'm Ready To Go!");
        readyButton.setFont(new Font("Monospaced", Font.BOLD, 120));
        readyButton.setForeground(Color.GREEN);
        readyButton.setBorderPainted(false);
        readyButton.setBackground(Color.BLACK);
        add(readyButton, BorderLayout.SOUTH);

        
    }

    public JButton getReadyButton() {
        return readyButton;
    }
}
