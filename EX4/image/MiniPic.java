package image;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class MiniPic implements Image{

    // The number of pixels of each row and column of the MiniPic (all MiniPics are square).
    private int size;

    // an array of all pixels of the MiniPic
    private final Color[][] pixelArray;

    /**
     * constructor of the minipic (minipics are always square)
     * @param image the original image object
     * @param rowStart the first row in the original picture of that is in the minipic
     * @param colStart the first colunm in the original picture that is in the mini pic
     * @param size the number of pixels in a minipic row and col (minipics are always square)
     */
    MiniPic(Image image, int rowStart, int colStart, int size){
        this.size = size;
        this.pixelArray = new Color[size][size];

        //copies the originals image pixels into the MiniPic pixelArray.
        for (int row = rowStart ;row < rowStart + size; row++) {
            for (int col = colStart; col < colStart+size; col++) {
                pixelArray[row-rowStart][col-colStart] = image.getPixel(row, col);
            }
        }
    }


    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public Iterable<Color> pixels() {
        return Image.super.pixels();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.size, this.pixelArray);
    }

}
