package volkov.ivan.ui;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    final static private double screenWidth;
    final static private double screenHeight;

    static {
        screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    public GUI() {
        this.setSize(300,800);
        this.setLocation((int)(screenWidth - this.getWidth()),100);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setLayout(null);

        this.setTitle("Bitmap Blaster");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
