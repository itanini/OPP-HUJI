package src;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.Factories.StrategyEnums;
import src.Factories.StrategyFactory;
import src.gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    // size of the borders at the edges of the window constant
    static float BORDER_WIDTH = 10;
    //minimum distance of the paddle from the borders
    private static int MIN_DISTANCE_FROM_EDGE = 0;

    // number of lives constant
    private int NUM_OF_LIVES = 3;

    // conducts the speed of the ball constant
    private static final float BALL_SPEED = 400;
    //the main ball (not pucks)
    private MainBall mainBall;

    // controls game processes- exit game, reset game etc.
    private WindowController windowController;

    // size of the window
    private  Vector2 windowDimensions;
    //dimensions of the paddles (all kinds)
    private final Vector2 PADDLE_DIMENSION = new Vector2(200, 20);

    // counts number of bricks le ft on board

    static Counter brickCounter;

    // contains all objects on board

    private GameObjectCollection gameObjects;

    //counts the number of lives left
    private Counter livesCounter;
    // used to get the users input
    private UserInputListener inputListener;

    private final StrategyEnums[] strategyTypesArray = {
            StrategyEnums.NORMAL,
            StrategyEnums.PUCK,
            StrategyEnums.CAMERA,
             StrategyEnums.PADDLE,
            StrategyEnums.HEART,
    StrategyEnums.DOUBLE};

    /**
     *
     * This is the constructor of a brick game, which calls its super (GameManager)'s constructor
     *
     * @param windowTitle the title of the window
     * @param windowDimensions a 2d vector representing the height and width of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);

    }

    /**
     * This method initializes a new game.
     *
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        brickCounter = new Counter();
        this.windowController = windowController;
        windowDimensions = windowController.getWindowDimensions();
        this.gameObjects = gameObjects();
        this.livesCounter = new Counter(NUM_OF_LIVES);

        // set background
        gameObjects.addGameObject(new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg",
                        false)), Layer.BACKGROUND);

        // Create Borders
        bordersInitializer();

        // create main ball
        Random rand = new Random();

        Renderable mainBallImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");

        ballInitializer(mainBallImage, collisionSound, rand);


        // create paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        paddleInitializer(inputListener, paddleImage);


        // Create bricks

        Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Renderable secondaryPaddleImage =
                imageReader.readImage("assets/botGood.png", false);

        // create lives counter display
        NumericLifeCounter numericLifeCounter = NumericLifeCounterInitializer();

        // create lives counter graphic display

        // used also for strategyFactory
        Vector2 widgetDimensions = windowDimensions.mult(0.1f);
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        float heartSize = Math.min(widgetDimensions.x() / 3, widgetDimensions.y());

        GraphicLifeCounter graphicLifeCounter = graphicLifeCounterInitializer
                (numericLifeCounter, widgetDimensions, heartImage, heartSize);

        // creates Factory of strategies
        StrategyFactory strategyFactory = strategyFactoryInitializer
                (inputListener, windowController, collisionSound, puckImage, secondaryPaddleImage,
                        numericLifeCounter, heartImage, heartSize, graphicLifeCounter);

        // create bricks
        bricksInitializer(rand, brickImage, strategyFactory);


    }

    /**
     * initialize window borders
     */
    private void bordersInitializer() {
        GameObject leftBorder= new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()),
                null);
        GameObject rightBorder= new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        GameObject upperBorder = new GameObject(Vector2.ZERO,new Vector2(windowDimensions.x(),
                BORDER_WIDTH), null);
        gameObjects.addGameObject(leftBorder);
        gameObjects.addGameObject(rightBorder);
        gameObjects.addGameObject(upperBorder);
    }

    /**
     *
     * @param mainBallImage - image of the ball supplied by the game manager
     * @param collisionSound - sound when the ball hits an object- supplied in the initializer func
     * @param rand - randomize the velocity of the ball
     *
     *             initialize the main ball
     */
    private void ballInitializer(Renderable mainBallImage, Sound collisionSound, Random rand) {
        this.mainBall = new MainBall(new Vector2(0,0),
                new Vector2(50, 50), mainBallImage, collisionSound);
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        if (rand.nextBoolean()){
            ballVelY *= -1;
        }
        mainBall.setCenter(windowDimensions.mult(0.5F));
        mainBall.setVelocity(new Vector2(ballVelX, ballVelY));
        mainBall.setTag("main ball");
        gameObjects.addGameObject(mainBall);

    }

    /**
     *
     * @param inputListener - gets the users input
     * @param paddleImage - image of the paddle- supplied in the initializer func
     *
     *                    initialize the main paddle
     */
    private void paddleInitializer(UserInputListener inputListener, Renderable paddleImage) {
        GameObject paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSION, paddleImage, inputListener,
                windowDimensions, (int)BORDER_WIDTH);
        paddle.setCenter(new Vector2((windowDimensions.x()/2), windowDimensions.y()-30));
        paddle.setTag("paddle");
        gameObjects.addGameObject(paddle);
    }

    /**
     *
     * @param numericLifeCounter- given to accurate the position of the graphic counter on the screen
     * @param widgetDimensions - the dimension of the widget
     * @param heartImage - the image of the heart supplied in the initializer func
     * @param heartSize
     *
     * initialize the graphic counter
     *
     */
    private GraphicLifeCounter graphicLifeCounterInitializer(NumericLifeCounter numericLifeCounter,
                                                             Vector2 widgetDimensions, Renderable heartImage,
                                                             float heartSize) {
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(new
                Vector2((numericLifeCounter.getTopLeftCorner().x()
                + numericLifeCounter.getDimensions().x()*0.3f),
                numericLifeCounter.getCenter().y()
                        + numericLifeCounter.getDimensions().y()*0.15f), widgetDimensions,
                livesCounter, heartImage,gameObjects, heartSize);
        gameObjects.addGameObject(graphicLifeCounter, Layer.BACKGROUND);
        return graphicLifeCounter;
    }

    /**
     *
     * @param inputListener -  gets the users input
     * @param windowController - used to get the dimensions of the window. used by the camera given code.
     * @param secondaryPaddleImage - secondary paddle image

     * initailize the strategy factory
     */
    private StrategyFactory strategyFactoryInitializer(UserInputListener inputListener,
                                                       WindowController windowController,
                                                       Sound collisionSound, Renderable puckImage,
                                                       Renderable secondaryPaddleImage,
                                                       NumericLifeCounter numericLifeCounter,
                                                       Renderable heartImage, float heartSize,
                                                       GraphicLifeCounter graphicLifeCounter) {
        StrategyFactory strategyFactory =
                new StrategyFactory(gameObjects, puckImage, collisionSound, BALL_SPEED,
                PADDLE_DIMENSION, secondaryPaddleImage, inputListener, MIN_DISTANCE_FROM_EDGE
                , mainBall, this, windowController, heartSize, heartImage, numericLifeCounter,
                graphicLifeCounter);
        return strategyFactory;
    }

    private void bricksInitializer(Random rand, Renderable brickImage, StrategyFactory strategyFactory) {
        Vector2 brickDimensions = new Vector2((windowDimensions.x()-2*BORDER_WIDTH)/7-1, 25);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 7 ; col++) {
                Vector2 brickLocation =
                        new Vector2(((col * (windowDimensions.x() - 2 * BORDER_WIDTH) / 7) + BORDER_WIDTH),
                                row * 25 + BORDER_WIDTH);
                GameObject brick =
                        new Brick(brickLocation, brickDimensions
                                , brickImage,
                                strategyFactory.buildCollisionStrategy(
                                        strategyTypesArray[rand.nextInt(strategyTypesArray.length)],
                                        brickLocation, brickDimensions), brickCounter);
                gameObjects.addGameObject(brick);
                brickCounter.increment();
            }
        }
    }

    /**
     *
     * initialize the numeric life counter
     */
    private NumericLifeCounter NumericLifeCounterInitializer() {
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(livesCounter, new Vector2
                (10, windowDimensions.y()*0.88f), windowDimensions.mult(0.10f), gameObjects);
        gameObjects.addGameObject(numericLifeCounter, Layer.BACKGROUND);
        return numericLifeCounter;
    }

    @Override
    /**
     * This method overrides the GameManager update method.
     *
     * @PARAM deltaTime - - used in the super's update method
     */
    public void update(float deltaTime) {
        super.update(deltaTime);

        checkForGameEnd(this.inputListener);
    }


    /**
     * Checks game condition to determine if the game should be over and for what easor (win or lose)
     *
     * @param inputListener a listener capable of reading user keyboard inputs
     */
    private void checkForGameEnd(UserInputListener inputListener) {
        String prompt = "";

        // checks for a disqualification
        double ballHeight = mainBall.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            // we lost
            this.livesCounter.decrement();
            mainBall.setCenter(windowDimensions.mult(0.5f));
            // if users life counter is zero
            if (livesCounter.value() <= 0) {
                prompt = "You Lose!";
            }
            // if the brick counter shows zero or thw 'w' key was pressed by user
        }else if (brickCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You Win!";
            }

        if (!prompt.isEmpty()){
            prompt += " Play again?";

            // Asks user to play again
            if (windowController.openYesNoDialog(prompt)){
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    public Counter getLivesCounter() {
        return livesCounter;
    }

    /**
     *The main driver of the program
     *
     * @param args not used
     */

    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(900, 600)).run();

    }
}
