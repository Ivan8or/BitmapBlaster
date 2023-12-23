package volkov.ivan.web;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LocalImageLoader {

    public BufferedImage load(String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        return img;
    }
}
