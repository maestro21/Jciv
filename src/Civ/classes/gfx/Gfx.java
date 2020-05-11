package Civ.classes.gfx;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gfx {


    protected Map<String, Image> images = new HashMap<>();


    public Image load(String key, String src) {
        Image img = Toolkit.getDefaultToolkit().getImage(src);
        if(img != null) {
            images.put(key, img);
            return img;
        }
        System.out.println(src + " don't exist");
        return null;
    }

    public Image get(String key) {
        return images.get(key);
    }
}
