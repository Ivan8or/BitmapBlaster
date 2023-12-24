package volkov.ivan.draw;

import volkov.ivan.draw.data.ColorSheet;
import volkov.ivan.draw.data.DrawArea;
import volkov.ivan.draw.data.MousePosition;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

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
        MousePosition previousPos = null;
        boolean dragging = false;

        for(int col = 0; col < image.getWidth(); col++) {
            for(int row = 0; row < image.getHeight(); row++) {

                MousePosition curPos = new MousePosition((int)(area.smallX() + col * xSpacing), (int)(area.smallY() + row * ySpacing));
                Color curCol = new Color(image.getRGB(col, row));

                if(!curCol.equals(previousCol)) {

                    if(previousPos != null) {
                        leftClickRelease(previousPos);
                        dragging = false;
                    }

                    loadColor(curCol);
                }

                if(!dragging) {
                    boolean shouldDrag = goodToDrag(curCol, row, col, image);
                    if(shouldDrag) {
                        leftClickHold(curPos);
                        dragging = true;
                    }
                    else {
                        leftClickOn(curPos);
                    }
                }

                previousCol = curCol;
                previousPos = curPos;
            }
            leftClickRelease(previousPos);
            previousPos = null;
            previousCol = null;
            dragging = false;
        }
    }

    private boolean goodToDrag(Color want, int row, int col, BufferedImage image) {

        if(row >= image.getHeight()-4) {
            return false;
        }
        for(int ahead = 1; ahead < 5; ahead++) {
            Color futureColor = new Color(image.getRGB(col, row+ahead));
            if(!futureColor.equals(want)) {
                return false;
            }
        }
        return true;
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
        Thread.sleep(1);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(1);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void leftClickHold(MousePosition mp) throws InterruptedException {
        robot.mouseMove(mp.x(), mp.y());
        Thread.sleep(10);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }
    private void leftClickRelease(MousePosition mp) throws InterruptedException {
        robot.mouseMove(mp.x(), mp.y());
        Thread.sleep(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void rightClickOn(MousePosition mp) throws InterruptedException {
        robot.mouseMove(mp.x(), mp.y());
        Thread.sleep(1);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        Thread.sleep(1);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
}
