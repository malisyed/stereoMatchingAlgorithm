package com.mas.readandwriteimage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ReadImage {

    public double[][] imageMatrix(BufferedImage image){
        int height = image.getHeight();
        int width = image.getWidth();
        double[][] matrix = new double[height][width];
        for (int i = 0; i <height; i++){
            for (int j = 0; j < width; j++){
                Color color = new Color(image.getRGB(j, i));
                double grey = rgbToGray(color);
                matrix[i][j] = grey;
            }
        }
        return matrix;
    }

    public BufferedImage readImage(String path){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public double rgbToGray(Color color) {
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();
        double grey = 0.2989 * red + 0.5870 * green + 0.1140 * blue;
        return grey;
    }
}
