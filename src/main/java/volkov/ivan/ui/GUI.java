package volkov.ivan.ui;

import volkov.ivan.bitmap.ImageTransformer;
import volkov.ivan.draw.ImagePrinter;
import volkov.ivan.web.WebImageLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class GUI extends JFrame {

    final static private double screenWidth;
    final static private double screenHeight;

    final private BitPicPanel[] previews;
    private int previewIndex = 0;

    final private DrawAreaPicker dap;
    final private PalettePicker pp;

    final static Color[] DEFAULT_PALETTE = {Color.BLACK, Color.WHITE };

    static {
        screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    public GUI() throws AWTException {
        this.setSize(300,800);
        this.setLocation((int)(screenWidth - this.getWidth()),100);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setLayout(null);

        this.setTitle("Bitmap Blaster");

        previews = new BitPicPanel[10];
        for(int i = 0; i < previews.length; i++) {
            previews[i] = new BitPicPanel(null, 100, DEFAULT_PALETTE);
            previews[i].setBackground(Color.BLACK);
            previews[i].setBounds(0,0,300,300);
            previews[i].setVisible(false);
            this.add(previews[i]);
        }
        previews[0].setVisible(true);

        JButton leftScroll = new JButton("<");
        leftScroll.setBounds(10,305,130,20);

        JButton rightScroll = new JButton(">");
        rightScroll.setBounds(145,305,130,20);

        rightScroll.addActionListener((l -> {
            previews[previewIndex].setVisible(false);
            previewIndex = Math.min(previews.length-1, previewIndex+1);
            previews[previewIndex].setVisible(true);
            leftScroll.setEnabled(previewIndex != 0);
            rightScroll.setEnabled(previewIndex != previews.length-1);
        }));
        leftScroll.addActionListener((l -> {
            previews[previewIndex].setVisible(false);
            previewIndex = Math.max(0, previewIndex-1);
            previews[previewIndex].setVisible(true);
            leftScroll.setEnabled(previewIndex != 0);
            rightScroll.setEnabled(previewIndex != previews.length-1);
        }));
        leftScroll.setEnabled(false);

        this.add(leftScroll);
        this.add(rightScroll);

        JLabel slideLabel = new JLabel("Res:");
        slideLabel.setBounds(15,325,30,30);
        this.add(slideLabel);

        JSlider slide = new JSlider(2, 300, 100);
        slide.setBounds(45,327,233,30);
        slide.addChangeListener((l -> {
            for(int i = 0; i < previews.length; i++) {
                previews[i].setResolution(slide.getValue());
            }
        }));
        this.add(slide);


        this.dap = new DrawAreaPicker();
        JButton drawAreaButton = new JButton("C. Draw Area");
        drawAreaButton.setBounds(15, 400, 125, 24);
        drawAreaButton.addActionListener((l -> {
            this.dap.setVisible(true);
        }));
        this.add(drawAreaButton);

        this.pp = new PalettePicker();
        JButton paletteButton = new JButton("C. Palette");
        paletteButton.setBounds(145, 400, 125, 24);
        paletteButton.addActionListener((l -> {
            this.pp.setVisible(true);
        }));
        this.add(paletteButton);


        JTextField searchBar = new JTextField();
        searchBar.setBounds(15, 370, 171, 25);
        this.add(searchBar);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(190, 370, 80, 24);
        searchButton.addActionListener((l -> {
            try {
                Image[] pics = WebImageLoader.load(searchBar.getText());
                Image[] scaled = Arrays.stream(pics)
                        .map(i -> i.getScaledInstance(300, 300, Image.SCALE_DEFAULT))
                        .toArray(Image[]::new);
                this.setPics(scaled, pp.getSheet().getPalette());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        this.add(searchButton);



        JButton printButton = new JButton("Print!");
        printButton.setBounds(15, 430, 255, 24);
        printButton.addActionListener((l -> {
            try {
                ImagePrinter printer = new ImagePrinter(pp.getSheet(), dap.getArea());
                printer.print(previews[previewIndex].getBittedImage(), slide.getValue(), slide.getValue());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        this.add(printButton);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setPics(Image[] pics, Color[] palette) {
        if(palette == null || palette.length == 0)
            palette = DEFAULT_PALETTE;

        for(int i = 0; i < previews.length; i++) {
            previews[i].changeImageAndPalette(pics[i], palette);
        }
    }
}
