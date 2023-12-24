package volkov.ivan.draw;

import volkov.ivan.draw.data.ColorSheet;
import volkov.ivan.draw.data.DrawArea;
import volkov.ivan.draw.data.MousePosition;
import volkov.ivan.ui.BitPicPanel;
import volkov.ivan.web.LocalImageLoader;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagePrinter {

    final private Robot robot;
    private ColorSheet cs;
    private DrawArea area;

    public ImagePrinter(ColorSheet cs, DrawArea da) throws AWTException {
        robot = new Robot();
        this.cs = cs;
        this.area = da;
    }

    public void setColorSheet(ColorSheet cs) {
        this.cs = cs;
    }

    public void setDrawArea(DrawArea area) {
        this.area = area;
    }

    public void print(Image i, int resX, int resY) throws InterruptedException {

        BufferedImage image = getBufferedImage(i, resX, resY);

        double xSpacing = area.width() / (double) image.getWidth();
        double ySpacing = area.height() / (double) image.getHeight();

        Color previousCol = null;
        for(int col = 0; col < image.getWidth(); col++) {
            for(int row = 0; row < image.getHeight(); row++) {
                Color curCol = new Color(image.getRGB(col, row));
                if(!curCol.equals(previousCol)) {
                    loadColor(curCol);
                    previousCol = curCol;
                }
                leftClickOn(new MousePosition((int)(area.smallX() + col * xSpacing), (int)(area.smallY() + row * ySpacing)));
            }
        }
    }

    private static BufferedImage getBufferedImage(Image i, int resX, int resY) {
        BufferedImage bufferedImage = new BufferedImage(
                resX,
                resY,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(i.getScaledInstance(resX, resY, Image.SCALE_DEFAULT), 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    private void loadColor(Color c) throws InterruptedException {
        MousePosition mp = cs.getColorLocation(c);
        leftClickOn(mp);
    }

    private void leftClickOn(MousePosition mp) throws InterruptedException {
        robot.mouseMove(mp.x(), mp.y());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2);
    }

    private void rightClickOn(MousePosition mp) throws InterruptedException {
        robot.mouseMove(mp.x(), mp.y());
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        Thread.sleep(1);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        Thread.sleep(1);
    }
}
