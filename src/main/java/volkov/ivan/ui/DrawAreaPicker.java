package volkov.ivan.ui;

import volkov.ivan.draw.data.ColorSheet;
import volkov.ivan.draw.data.DrawArea;
import volkov.ivan.draw.data.MousePosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawAreaPicker extends JFrame {

    private DrawArea area;
    private Robot robot;

    private int xs[] = new int[2];
    private int ys[] = new int[2];
    private int coordIndex;

    public DrawAreaPicker() throws AWTException {
        this.robot = new Robot();

        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        this.setUndecorated(true);
        this.setOpacity(0.3f);
        this.setBounds(0,0,screenWidth,screenHeight);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
        this.setVisible(false);

        JLabel selectedArea = new JLabel();
        selectedArea.setBackground(new Color(255,0,0));
        selectedArea.setOpaque(true);
        selectedArea.setBounds(10,10,400,400);
        this.add(selectedArea);

        DrawAreaPicker thisObj = this;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.getButton() == MouseEvent.BUTTON1) {
                    Point mouse = MouseInfo.getPointerInfo().getLocation();
                    xs[coordIndex] = (int) mouse.getX();
                    ys[coordIndex] = (int) mouse.getY();
                    area = new DrawArea(xs[0], ys[0], xs[1], ys[1]);
                    selectedArea.setBounds(area.smallX(), area.smallY(), area.width(), area.height());
                    coordIndex = (coordIndex+1) % 2;
                    System.out.println(area);
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

    public DrawArea getArea() {
        return area;
    }

}
