package lol.millard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Ranger extends Player {
    private static BufferedImage portrait;

    static {
        try {
            portrait = ImageIO.read(new File("src/main/resources/menu/portraits/Ranger.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Ranger(int x, int y) {
        super(x, y);
    }

    public static BufferedImage getPortrait() {
        return portrait;
    }

}
