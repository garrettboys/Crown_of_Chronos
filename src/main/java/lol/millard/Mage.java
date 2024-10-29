package lol.millard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mage extends Character {


    private static BufferedImage portrait;

    static {
        try {
            portrait = ImageIO.read(new File("src/main/resources/menu/portraits/Mage.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mage() {
        super();
        health = 10;
    }

    public static BufferedImage getPortrait() {
        return portrait;
    }


}
