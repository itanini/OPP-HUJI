package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Shell {
    // the given image
    private final Image img;
    // a flag- if the value is false the program will produce a html picture, if its true it will produce it
    // on the console screen
    private boolean consoleRenderer = false;
    //this object screen the image on the console
    private final ConsoleAsciiOutput consoleAsciiOutput;
    // this object makes the image in html
    private final HtmlAsciiOutput htmlAsciiOutput;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;

    private final String FONT = "Courier New";
    // this massage will appear when exceeding pixel boundries
    private static final String EXCEEDING_BOUNDARIES_MESSAGE = "Did not change due to exceeding boundaries";
    private final int minCharsInRow;
    private final int maxCharsInRow;
    // the actual chars in a row of the product
    private int charsInRow;
    // the initial characters list to be used on the image
    private final List<Character> INITIAL_CHARACTERS = Arrays.asList('0','1','2','3','4','5','6','7','8','9');
    // the set of all characters used to produce the image
    private final Set<Character> characterSet = new HashSet<Character>(INITIAL_CHARACTERS);
    // used to get the users input
    private final Scanner scanner = new Scanner(System.in);

    /**
     * shells constructor
     * @param img the given image
     */
    public Shell(Image img) {
        this.img = img;

        htmlAsciiOutput = new HtmlAsciiOutput(".out.html", FONT);

        consoleAsciiOutput = new ConsoleAsciiOutput();

        minCharsInRow = Math.max (1, img.getWidth()/ img.getHeight());

        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;

        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);

    }

    /**
     * this function runs the whole process
     */
    public void run() {
        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, FONT);
        while (true) {
            System.out.print(">>>");
            //these lines make the users input an array and saves the first and the second words in the command
            String[] input = scanner.nextLine().split(" ");
            if (input.length > 2) {
                printIncorrectCommandMessage();
            }
            String firstCommand = input[0];
            if (input.length == 2) {
                String secondCommand = input[1];
                switch (firstCommand) {
                    case "add":
                    case "remove":
                        addOrRemoveInput(firstCommand, secondCommand);
                        break;
                    case "res":
                        resCase(secondCommand);
                        break;
                    default:
                        printIncorrectCommandMessage();
                }
            } else if (input.length == 1) {
                switch (firstCommand) {
                    case "exit":
                        System.exit(0);
                        break;
                    case "console":
                        consoleRenderer = true;
                        break;
                    case "render":
                        renderCase(charMatcher);
                        break;
                    case "chars":
                        for (char character : characterSet) {
                            System.out.println(character + " ");
                        }
                        break;
                    default:
                        printIncorrectCommandMessage();
                }
            }
        }
    }

    /**
     * this function render the image- if the render flag is true than on the console. else it makes an html
     * file that will be saved as ".out"
     */
    private void renderCase(BrightnessImgCharMatcher charMatcher) {
        Character [] charArray = new Character[characterSet.size()];
        // these lines copy the character set into an array
        int counter = 0;
        for (Character character: characterSet
             ) {charArray[counter] = character;
            counter += 1;
        }
        //makes the match between the image and its ascii presentation
        char[][] imageToCharsTemplate = charMatcher.chooseChars(charsInRow, charArray);

        if (consoleRenderer){
            consoleAsciiOutput.output(imageToCharsTemplate);
        }
        else{
            htmlAsciiOutput.output(imageToCharsTemplate);
        }
    }

    /**
     * changes the resulution of the image presentation
     * @param secondCommand - should be up or down
     */
    private void resCase(String secondCommand) {
        if(secondCommand.equals("up")) {
            if (charsInRow * 2 <= maxCharsInRow) {
                charsInRow = charsInRow * 2;
                System.out.println("Width set to " + charsInRow);
            } else {
                System.out.println(EXCEEDING_BOUNDARIES_MESSAGE);
            }
        }
        else if (secondCommand.equals("down")){
            if (charsInRow/2 >= minCharsInRow){
                charsInRow = charsInRow/2;
                System.out.println("Width set to " + charsInRow);}
            else {
                System.out.println(EXCEEDING_BOUNDARIES_MESSAGE);
            }
        }
        else {
            printIncorrectCommandMessage();
        }
    }

    /**
     * this function makes any add or remove comand into a range asked to be added or removed.
     * after that it calls a function that adds a certain range of characters
     * @param firstCommand- should be add or remove
     * @param secondCommand should be {all (all characters),space, a character, or a range of characters}
     */
    private void addOrRemoveInput(String firstCommand, String secondCommand)
        {
            if (secondCommand.length() == 1) {
                addOrRemoveAsciiRange(firstCommand,secondCommand + "-" + secondCommand);
            } else if (secondCommand.equals("all")) {
                addOrRemoveAsciiRange(firstCommand, " -~");
            } else if (secondCommand.equals("space")) {
                addOrRemoveAsciiRange(firstCommand, " - ");
            } else if ((secondCommand.toCharArray().length == 3) && (secondCommand.toCharArray()[1] == '-')) {
                addOrRemoveAsciiRange(firstCommand,
                        secondCommand.toCharArray()[0] + "-" + secondCommand.toCharArray()[2]);
            } else {
                System.out.println("Did not " + firstCommand + " due to incorrect format");
            }
        }


    /**
     *
     * @param firstCommand should be add or remove
     * @param asciiRange a range of characters
     */
    private void addOrRemoveAsciiRange(String firstCommand, String asciiRange)
     {
        char[] asciiRangeArray = asciiRange.toCharArray();
        int[] asciiNumericRange = {(int) asciiRangeArray[0] , (int) (asciiRangeArray[2])};
        for (int index = Math.min(asciiNumericRange[0], asciiNumericRange[1]);
         index <= Math.max(asciiNumericRange[0], asciiNumericRange[1]); index++){
            addOrRemove(firstCommand,(char) index);
        }
    }

    /**
     * this function calls either the add function or the remove function
     * @param addOrRemove a string that should say add or remove
     * @param index the numeric present of the character

     */
    private Void addOrRemove(String addOrRemove,char index) {
        if (addOrRemove.equals("add")){
            characterSet.add(index);
        }
        else if (addOrRemove.equals("remove")) {
            characterSet.remove(index);
        }
        return null;
    }

    /**
     * print a massage when an incorrect command is given by the user
     */
    private void printIncorrectCommandMessage(){
        System.out.println("Did not executed due to incorrect command");
    }
}
