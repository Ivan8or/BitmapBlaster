package volkov.ivan.bitmap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTransformer {


//    public static final Color[] SKRIBBL_PALETTE = {
//
//            new Color(255,255,255),
//            new Color(193,193,193),
//            new Color(239,19,11),
//            new Color(255,113,0),
//            new Color(255,228,0),
//            new Color(0,204,0),
//            new Color(0,255,145),
//            new Color(0,178,255),
//            new Color(35,31,211),
//            new Color(163,0,186),
//            new Color(223,105,167),
//            new Color(255,172,142),
//            new Color(160,82,45),
//            new Color(0,0,0),
//            new Color(80,80,80),
//            new Color(116,11,7),
//            new Color(194,56,0),
//            new Color(232,162,0),
//            new Color(0,70,25),
//            new Color(0,120,93),
//            new Color(0,86,158),
//            new Color(14,8,101),
//            new Color(85,0,105),
//            new Color(135,53,84),
//            new Color(204,119,77),
//            new Color(99,48,13)
//    };

    public static Image toBitmap(Image image, int resWidth, int resHeight, Color[] palette) {
        int originalWidth = image.getWidth(null);
        int originalHeight = image.getHeight(null);

        Image resizedImage = image.getScaledInstance(resWidth, resHeight, Image.SCALE_DEFAULT);
        BufferedImage bitmap = new BufferedImage(
                resWidth,
                resHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D bGr = bitmap.createGraphics();
        bGr.drawImage(resizedImage, 0, 0, null);
        bGr.dispose();

        for(int row = 0; row < bitmap.getHeight(); row++) {
            for(int col = 0; col < bitmap.getWidth(); col++) {

                int originalRGB = bitmap.getRGB(col, row);
                Color originalColor = new Color(originalRGB);
                Color closestColor = nearestColor(originalColor, palette);
                bitmap.setRGB(col, row, closestColor.getRGB());

                double redQuant = (originalColor.getRed() - closestColor.getRed()) / 16.0;
                double greenQuant = (originalColor.getGreen() - closestColor.getGreen()) / 16.0;
                double blueQuant = (originalColor.getBlue() - closestColor.getBlue()) / 16.0;

                int[] colOffsets = {1, 0, -1, 1};
                int[] rowOffsets = {0, 1,  1, 1};
                double[][] qAmounts = {
                        {redQuant*7, redQuant*5, redQuant*3, redQuant*1},
                        {greenQuant*7, greenQuant*5, greenQuant*3, greenQuant*1},
                        {blueQuant*7, blueQuant*5, blueQuant*3, blueQuant*1},
                };

                for(int offsetIndex = 0; offsetIndex < 4; offsetIndex++) {

                    int nextRow = row + rowOffsets[offsetIndex];
                    int nextCol = col + colOffsets[offsetIndex];

                    if (nextCol >= 0 && nextCol < bitmap.getWidth() && nextRow < bitmap.getHeight()) {

                        Color toAdjust = new Color(bitmap.getRGB(nextCol, nextRow));
                        int adjustedRed = boundColorGamut(toAdjust.getRed() + qAmounts[0][offsetIndex]);
                        int adjustedGreen = boundColorGamut(toAdjust.getGreen() + qAmounts[1][offsetIndex]);
                        int adjustedBlue = boundColorGamut(toAdjust.getBlue() + qAmounts[2][offsetIndex]);

                        bitmap.setRGB(nextCol, nextRow, new Color(adjustedRed, adjustedGreen, adjustedBlue).getRGB());
                    }
                }
            }
        }
        return bitmap.getScaledInstance(originalWidth, originalHeight, Image.SCALE_DEFAULT);
    }

    private static int boundColorGamut(double val) {
        return (int) Math.min(255, Math.max(0, val));
    }

    private static Color nearestColor(Color original, Color[] palette) {
        int origRed = original.getRed();
        int origGreen = original.getGreen();
        int origBlue = original.getBlue();

        double smallestDist = Integer.MAX_VALUE;
        Color closestColor = palette[0];

        for(Color c : palette) {
            int newRed = c.getRed();
            int newGreen = c.getGreen();
            int newBlue = c.getBlue();

            double dist = Math.pow(newRed - origRed, 2)
                    + Math.pow(newGreen - origGreen, 2)
                    + Math.pow(newBlue - origBlue, 2);

            if(dist < smallestDist) {
                smallestDist = dist;
                closestColor = c;
            }
        }
        return closestColor;
    }
}
