package volkov.ivan.ui;

import javax.swing.*;
import java.awt.*;

public class PicPanel extends JPanel {

    private Image image;

    public PicPanel(Image image) {
        this.image = image;
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }


}
