package ascii_art.img_to_char;

import image.MiniPic;
import image.Image;
import java.awt.*;
import java.util.*;
import java.lang.Math;
import java.util.List;

public class BrightnessImgCharMatcher {
    private static int NUMBER_OF_PIXELS = 16;
    private static int MAX_RGB = 255;
    private Image image;

    private String fontName;

    // a hash map that match brightness vales to a char.  brightness values are fully normalized in this map
    private final HashMap<Float, Character> brightnessToCharHashMap = new HashMap<Float, Character>();

    // a has map that match a character to its brightness - the brightnesses in this hashmap are not fully
    //normalized.
    private final HashMap<Character, Float> characterToBrightnessHashmap = new HashMap<Character, Float>();

    // a hash map that fit a mini pic to its calculated brightness value
    private final HashMap<Image, Float> minipicToBrightnessHashMap = new HashMap<Image, Float>();

    public BrightnessImgCharMatcher(Image img, String font){

        this.image = img;

        this.fontName = font;
    }

    /**
     * this function makes the whole proccess to match a image to a 2dimentional array of characters
     * @param numCharsInRow - clear
     * @param charArray - all characters that can be matched to a minipic
     * @return the 2 dimentional array of characters to be rendered
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charArray){

        char[][] charImageArray= new char[image.getHeight()/(image.getWidth()/numCharsInRow)][numCharsInRow];
        Set <Character> charSet= new HashSet<>(Arrays.asList(charArray));

        // if there was a change in the characters that are allowed to be used
        if (!(charSet.equals(characterToBrightnessHashmap.keySet()))){
            for (Character character : charSet){
                hashMapCharMatcher(character, fontName);
            }
        }
        brightnessStretch();
        int rowCounter = 0;
        int colCounter = 0;
        for (Image miniPic: image.divideToMiniPics((image.getWidth())/numCharsInRow)){
            matchMiniPicToBrightness(miniPic);
            charImageArray[rowCounter][colCounter] = matchMiniPicToCharacter(miniPic);
            if (colCounter == numCharsInRow-1){
                colCounter = 0;
                rowCounter += 1;
            }
            else {
                colCounter += 1;
            }
        }
       return charImageArray;
    }

    /**
     * detects the character brightness and put it in a hashmap
     * @param character the character to be matched
     * @param fontName
     */
    private void hashMapCharMatcher(Character character, String fontName) {

        int brightnessCounter = 0;
        boolean[][] charBooleanArray = CharRenderer.getImg(character, NUMBER_OF_PIXELS, fontName);
        for (boolean[] row : charBooleanArray) {
            for (boolean pixel : row) {
                if (pixel) {
                    brightnessCounter += 1;
                }
            }
        }
        float charBrightness = ((float) brightnessCounter / NUMBER_OF_PIXELS);
        characterToBrightnessHashmap.put(character, charBrightness);
    }

    /**
     * normelize the brightness
     */
    private void brightnessStretch() {
        float minBrightness = Collections.min(characterToBrightnessHashmap.values());
        float maxBrightness = Collections.max(characterToBrightnessHashmap.values());

        for (Character character : characterToBrightnessHashmap.keySet()) {
            float normalizedBrightness = ((characterToBrightnessHashmap.get(character) - minBrightness)) /
                    (maxBrightness - minBrightness);
            brightnessToCharHashMap.put(normalizedBrightness, character);
        }
    }

    /**
     * calculates the minipics brightness
     * @param miniPic
     * @return
     */
    private float miniPicBrightnessCalc(Image miniPic) {
        float brightnessOfMiniPic = 0;
        for (Color pixel : miniPic.pixels()) {
            brightnessOfMiniPic += greyPixelCalc(pixel);
        }
        return (brightnessOfMiniPic / (miniPic.getHeight() * miniPic.getWidth() * MAX_RGB));
    }
    /**
     * calculates the grey color value of an RGB pixel
     * @param pixel a given pixel to calculate its grey value
     *
     */
    private float greyPixelCalc(Color pixel) {
        return (pixel.getRed() * 0.2126f + pixel.getGreen() * 0.7152f + pixel.getBlue() * 0.0722f);
    }

    /**
     * match a mini pic to its brightness and puts it in a hashmap
     * @param miniPic
     */
    private void matchMiniPicToBrightness(Image miniPic) {
        float miniPicBrightness = miniPicBrightnessCalc(miniPic);
        minipicToBrightnessHashMap.put(miniPic,miniPicBrightness);
    }

    /**
     * match a mini pic to a charcter using the brightnessTOcharacter hash map
     * @param miniPic
     * @return the matched character
     */
    private Character matchMiniPicToCharacter(Image miniPic){
        float closestBrightness = 2;
        float minimalDeferential = 1;
        for (float brightness : brightnessToCharHashMap.keySet()) {
            if (Math.abs(minipicToBrightnessHashMap.get(miniPic) - brightness) < minimalDeferential) {
                minimalDeferential = Math.abs(minipicToBrightnessHashMap.get(miniPic) - brightness);
                closestBrightness = brightness;
            }
        }
        return brightnessToCharHashMap.get(closestBrightness);
    }
}
