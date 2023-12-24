package volkov.ivan.ui;

import volkov.ivan.bitmap.ImageTransformer;

import javax.swing.*;
import java.awt.*;

public class BitPicPanel extends JPanel {

    private Image originalImage;
    private Image bittedImage;

    private int currentResolution;
    private Color[] palette;

    public BitPicPanel(Image originalImage, int res, Color[] palette) {
        this.setLayout(null);
        this.currentResolution = res;
        this.originalImage = originalImage;
        this.palette = palette;
        rebitImage();
    }

    public void changeImage(Image image) {
        this.originalImage = image;
        rebitImage();
    }
    public void changeImageAndPalette(Image image, Color[] palette) {
        this.originalImage = image;
        this.palette = palette;
        rebitImage();
    }

    public void setResolution(int res) {
        this.currentResolution = res;
        rebitImage();
    }

    public void setPalette(Color[] palette) {
        this.palette = palette;
        rebitImage();
    }

    public void rebitImage() {
        if(originalImage == null)
            bittedImage = null;
        else
            bittedImage = ImageTransformer.toBitmap(originalImage, currentResolution, currentResolution, palette);
        this.repaint();
    }

    public Image getBittedImage() {
        return bittedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bittedImage, 0, 0, this); // see javadoc for more info on the parameters
    }


}
