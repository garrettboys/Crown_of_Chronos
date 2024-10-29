package lol.millard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Monk extends Character {

    private static BufferedImage portrait;

    static {
        try {
            portrait = ImageIO.read(new File("src/main/resources/menu/portraits/Monk.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Monk() {
        super();
    }

    public static BufferedImage getPortrait() {
        return portrait;
    }



}