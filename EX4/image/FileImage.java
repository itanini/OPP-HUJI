package image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
class FileImage implements Image {

    private static final Color DEFAULT_COLOR = Color.WHITE;
    private int imageWidth;

    private int imageHeight;
    private final Color[][] pixelArray;

    /**
     * open a file image and makes it an image object. and also make it fluffy (מרופד)
     * @param filename
     * @throws IOException
     */
    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        //im.getRGB(x, y)); getter for access to a specific RGB rates
        // מגדיל את התמונה כך שהרוחב והגובה שלה יהיו חזקות שלמות של 2
        int newWidth = 1;
        while (newWidth < origWidth){
            newWidth = newWidth*2;
        }
        this.imageWidth = newWidth;

        int newHeight = 1;
        while (newHeight < origHeight) {
            newHeight = newHeight * 2;
        }
        this.imageHeight = newHeight;

        // נעתיק את הקורדינטות התמונה למערך בגודל החדש ונוסיף בקצוות התמונה פיקסלים לבנים
        pixelArray = new Color[newHeight][newWidth];
        int vertical_gap = (newHeight - origHeight)/2;
        int horizonal_gap = (newWidth -origWidth)/2;
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if ((row < vertical_gap)||(col< horizonal_gap)||(row >= newHeight - vertical_gap) ||
                        (col>= newWidth - horizonal_gap)){
                    pixelArray[row][col] = Color.WHITE;
                }
                else{
                    pixelArray[row][col] =
                            new Color(im.getRGB(col- horizonal_gap, row- vertical_gap));
                }
            }

        }
    }

    @Override
    public int getWidth() {
        return imageWidth;
    }

    @Override
    public int getHeight() {
        return imageHeight;
    }

    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

}
