import java.awt.*;

import java.io.IOException;

class EuropePanel extends ImagePanel {
    public EuropePanel(String imagePath) throws IOException {
        super(imagePath);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Paint a different image representing Europe
    }
}