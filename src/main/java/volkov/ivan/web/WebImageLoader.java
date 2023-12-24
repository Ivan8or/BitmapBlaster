package volkov.ivan.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class WebImageLoader {

    final static private String api_path = "https://api.unsplash.com/search/photos?page=1&per_page=10&orientation=squarish&query=SEARCH";

    public static BufferedImage[] load(String search) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(api_path.replaceFirst("SEARCH", search.replace(" ", "%20")));

        String secrets = "";
        for(String l : Files.readAllLines(Paths.get("secrets.txt")))
            secrets += l;

        String clientId = new JSONObject(secrets).getString("client-id");
        httpget.setHeader("Authorization", "Client-ID "+clientId);

        HttpResponse response = httpclient.execute(httpget);

        String text = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        JSONObject obj = new JSONObject(text);

        BufferedImage[] imgs = new BufferedImage[10];
        for(int i = 0; i < imgs.length; i++) {
            try {
                String imageURL1 = obj.getJSONArray("results").getJSONObject(i).getJSONObject("urls").getString("small");
                imgs[i] = ImageIO.read(new URL(imageURL1));
            }catch(Exception e) {
                BufferedImage blank = new BufferedImage(400,400,BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = blank.createGraphics();
                graphics.setPaint ( Color.BLACK );
                graphics.fillRect ( 0, 0, 400, 400 );
                graphics.setPaint ( Color.MAGENTA );
                graphics.fillRect ( 0, 0, 200, 200 );
                graphics.fillRect ( 200, 200, 400, 400 );
                graphics.dispose();
                imgs[i] = blank;
            }
        }
        return imgs;
    }

}
