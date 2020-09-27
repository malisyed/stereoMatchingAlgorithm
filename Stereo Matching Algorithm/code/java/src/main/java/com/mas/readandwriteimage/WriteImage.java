package com.mas.readandwriteimage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

public class WriteImage {

    public static void writeImage(int width,int height,double[][] imageArr) throws Exception {
        String path = "disparity.png";
        File ImageFile = new File(path);
        BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = image.getRaster() ;
        double[][] imageMatrix = imageArr;
        for (int x = 0; x < height -1; x++) {
            for (int y = 0; y < width -1; y++) {
                wr.setSample(y,x,0,imageMatrix[x][y]);
            }
        }
        ImageIO.write(image, "png", ImageFile);
    }
}

