package volkov.ivan.draw.data;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorSheet {

    private Map<Color, MousePosition> colors;

    public ColorSheet() {
        colors = new HashMap<>();
    }
    public ColorSheet(Map<Color, MousePosition> colors) {
        this.colors = colors;
    }

    public void addColor(Color c, MousePosition pos) {
        colors.put(c, pos);
    }

    public MousePosition getColorLocation(Color c) {
        return colors.getOrDefault(c, new MousePosition(0,0));
    }

    public Color[] getPalette() {
        return colors.keySet().toArray(Color[]::new);
    }

    public String toString() {
        String toReturn = "";
        for(Color c : colors.keySet()) {
            toReturn+= c+":"+colors.get(c)+"\n";
        }
        return toReturn;
    }

}
