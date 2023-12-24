package volkov.ivan.ui;

import volkov.ivan.draw.data.ColorSheet;
import volkov.ivan.draw.data.MousePosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class PalettePicker extends JFrame {

    private ColorSheet sheet;
    private Robot robot;

    public PalettePicker() throws AWTException {
        this.sheet = new ColorSheet(new HashMap<>());
        this.robot = new Robot();

        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        this.setUndecorated(true);
        this.setOpacity(0.1f);
        this.setBounds(0,0,screenWidth,screenHeight);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setLayout(null);

        this.setVisible(false);

        PalettePicker thisObj = this;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getButton() == MouseEvent.BUTTON1) {
                    Point mouse = MouseInfo.getPointerInfo().getLocation();
                    Color color = robot.getPixelColor(mouse.x, mouse.y);
                    sheet.addColor(color, new MousePosition((int) mouse.getX(), (int) mouse.getY()));
                    System.out.println(sheet);
                }
                else if(e.getButton() == MouseEvent.BUTTON3) {
                    thisObj.setVisible(false);
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }

    public ColorSheet getSheet() {
        return sheet;
    }

}
