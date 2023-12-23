package volkov.ivan.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class WebImageLoader {

    final static private String api_path = "https://api.unsplash.com/search/photos?page=1&per_page=3&orientation=squarish&query=SEARCH";

    public static BufferedImage[] load(String search) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(api_path.replaceFirst("SEARCH", search));

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

        String imageURL1 = obj.getJSONArray("results").getJSONObject(0).getJSONObject("urls").getString("small");
        String imageURL2 = obj.getJSONArray("results").getJSONObject(1).getJSONObject("urls").getString("small");
        String imageURL3 = obj.getJSONArray("results").getJSONObject(2).getJSONObject("urls").getString("small");

        String[] urls = {imageURL1, imageURL2, imageURL3 };
        BufferedImage[] imgs = new BufferedImage[urls.length];

        for(int i = 0; i < urls.length; i++) {
            imgs[i] = ImageIO.read(new URL(urls[i]));
        }
        return imgs;
    }

}
