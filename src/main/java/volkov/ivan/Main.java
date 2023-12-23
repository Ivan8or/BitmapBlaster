package volkov.ivan;

import volkov.ivan.bitmap.ImageTransformer;
import volkov.ivan.ui.GUI;
import volkov.ivan.ui.PicPanel;
import volkov.ivan.web.WebImageLoader;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        Image loaded = WebImageLoader.load("man")[0]
                .getScaledInstance(300, 300, Image.SCALE_DEFAULT);

        Image bitmap = new ImageTransformer().toBitmap(loaded, 200, 200);

        GUI gui = new GUI();

        PicPanel p1 = new PicPanel(loaded);
        p1.setBackground(Color.BLACK);
        p1.setBounds(0,0,300,300);
        PicPanel p2 = new PicPanel(bitmap);
        p2.setBounds(0,300,300,300);
        gui.add(p1);
        gui.add(p2);
    }
}