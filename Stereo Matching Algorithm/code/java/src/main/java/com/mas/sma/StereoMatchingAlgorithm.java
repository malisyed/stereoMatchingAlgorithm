package com.mas.sma;

import com.mas.readandwriteimage.ReadImage;
import com.mas.readandwriteimage.WriteImage;
import java.awt.image.BufferedImage;
import static com.google.common.primitives.Doubles.min;

public class StereoMatchingAlgorithm {
    private double occlusion = 3.84;
    private static String left = "view11.png";
    private static String right = "view12.png";
    private double[][] leftImageMatrix;
    private double[][] rightImageMatrix;
    private int height;
    private int width;

    public double cost(int i , int j, int k){
        double cost = (Math.pow(((leftImageMatrix[k][i-1]) - (rightImageMatrix[k][j-1])), 2)) / 16;
        return cost;
    }

    public double[][] forwardPass(int k){
        int leftImageWidth = width;
        int rightImageWidth = width;
        double[][] cost_matrix = new double[leftImageWidth + 1][rightImageWidth + 1];

        for (int i = 1; i <= leftImageWidth; i++) {
            cost_matrix[i][0] = i*occlusion;
        }
        for (int i = 1; i <= rightImageWidth; i++) {
            cost_matrix[0][i] = i*occlusion;
        }
        for (int i = 1; i <= leftImageWidth; i++) {
            for (int j = 1; j <= rightImageWidth; j++) {
                double costOne = cost_matrix[i-1][j-1] + cost(i, j, k);
                double costTwo = cost_matrix[i][j-1] + occlusion;
                double costThree = cost_matrix[i-1][j] + occlusion;
                cost_matrix[i][j] = min(costOne, costTwo ,costThree);
            }
        }
        return cost_matrix;
    }

    public double[] backwardPass(int k){
        int leftImageWidth = width;
        int rightImageWidth = width;
        double[] d_matrix = new double[leftImageWidth];
        double[][] costMatrix = forwardPass(k);
        int i = leftImageWidth - 1;
        int j = rightImageWidth - 1 ;
        while (i > 0 && j > 0) {
            double value = costMatrix[i][j];
            double costOne = costMatrix[i - 1][j - 1] + cost(i, j, k);
            double costTwo = costMatrix[i][j - 1] + occlusion;
            double costThree = costMatrix[i - 1][j] + occlusion;
            if (value == costOne) {
                d_matrix[i-1] = Math.abs(i - j)*10;
                i--;
                j--;
            }
            if (value == costTwo){
                j--;

            }
            if (value == costThree){
                i--;
            }
        }
        return d_matrix;
    }

    public void stereoMatchingAlgorithm(ReadImage inputIm, WriteImage imageObj){
        BufferedImage leftImage = inputIm.readImage(left);
        BufferedImage rightImage = inputIm.readImage(right);;
        leftImageMatrix = inputIm.imageMatrix(leftImage);
        rightImageMatrix = inputIm.imageMatrix(rightImage);
        try{
            height = leftImage.getHeight();
            width = leftImage.getWidth();
            double[][] d_matrix = new double[height][width];
            for (int k = 0; k < height; k++) {
                System.out.println(k);
                double[] rowArr = backwardPass(k);
                for (int j = 0; j<width; j++){
                    d_matrix[k][j] = rowArr[j];
                }
            }
            imageObj.writeImage(width,height,d_matrix);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] arg){
        StereoMatchingAlgorithm stereoAlgo = new StereoMatchingAlgorithm();
        ReadImage inputIm = new ReadImage();
        WriteImage imageObj = new WriteImage();
        stereoAlgo.stereoMatchingAlgorithm(inputIm, imageObj);
    }
}
