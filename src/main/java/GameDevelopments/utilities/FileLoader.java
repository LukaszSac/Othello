package GameDevelopments.utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileLoader
{
    public static ImageIcon loadImage(String src)
    {
        try {
            BufferedImage image =  ImageIO.read(new File(src));
            return new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Wrong src");
            return null;
        }
    }
}
