package src.Factories;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.brick_strategies.*;
import src.gameobjects.*;

public class StrategyFactory {

    private CollisionStrategy collisionStrategy;

    // Puck Strategy arguments
    static private GameObjectCollection gameObjects;
    static private Renderable puckImage;
    static private Sound sound;
    static private float puckSpeed;


    // Paddle strategy arguments
    static private SecondaryPaddle paddle;

    // camera strategy arguments
    static private MainBall mainBall;
    static private BrickerGameManager gameManager;
    static  private WindowController windowController;

    // heart strategy fields
    static float heartSize;
    static Renderable heartImage;
    static NumericLifeCounter numericLifeCounter;
    static GraphicLifeCounter graphicLifeCounter;




    public StrategyFactory(GameObjectCollection gameObjectCollection, Renderable puckImage,
                           Sound sound, float puckSpeed,
                           Vector2 paddleDimension,
                           Renderable secondaryPaddleImage, UserInputListener inputListener,
                           int minDistFromEdge, MainBall mainBall, BrickerGameManager gameManager,
                           WindowController windowController, float heartSize, Renderable heartImage,
                           NumericLifeCounter numericLifeCounter, GraphicLifeCounter graphicLifeCounter)
    {
        /**
         * this factory is able to produce all kinds of collision strategies
         */
        gameObjects = gameObjectCollection;

        // needed for puck strategy
        StrategyFactory.puckImage = puckImage;
        StrategyFactory.sound = sound;
        StrategyFactory.puckSpeed = puckSpeed;

        //needed for paddle strategy
        paddle = new SecondaryPaddle(windowController.getWindowDimensions().mult(0.5f),
                paddleDimension, secondaryPaddleImage,
                inputListener, windowController.getWindowDimensions(), minDistFromEdge, gameObjects);
        paddle.setTag("secondary");

        // needed for camera strategy
        StrategyFactory.mainBall = mainBall;
        StrategyFactory.gameManager = gameManager;
        StrategyFactory.windowController = windowController;

        //needed for heart strategy
        StrategyFactory.heartSize = heartSize;
        StrategyFactory.heartImage = heartImage;
        StrategyFactory.numericLifeCounter = numericLifeCounter;
        StrategyFactory.graphicLifeCounter = graphicLifeCounter;



    }

    /**
     *
     * @param strategyType - an enum of the wanted strategy class to be created
     * @param brickLocation -the location of a specific brick is needed for puck strategy and heart strategy
     * @param brickDimension - needed if we want to set the size of the pucks or the heart in realtion
     *                       to the size of a specific brick
     * @return
     */
    public CollisionStrategy buildCollisionStrategy(StrategyEnums strategyType,
                                                    Vector2 brickLocation, Vector2 brickDimension) {
        switch (strategyType) {
            case NORMAL:
                this.collisionStrategy = new CollisionStrategy(gameObjects);
                break;
            case PUCK:
                // prepare an array of 3 pucks that will be released once the holding object will be collied
                Puck[] puckArray = getPuckArray(brickLocation, brickDimension);
                this.collisionStrategy = new PucksStrategy(gameObjects, puckArray, puckSpeed);
                break;
            case PADDLE:
                this.collisionStrategy = new PaddleStrategy(gameObjects, paddle);
                break;
            case CAMERA:
                this.collisionStrategy = new CameraChangeStrategy(gameObjects, mainBall,
                        gameManager, windowController);
                break;
            case HEART:
                // prepare the heart object that will be released to the space one the holding
                // object will be collied
                Heart heart = new Heart(brickLocation,new Vector2(heartSize, heartSize) , heartImage,
                        gameObjects, gameManager.getLivesCounter() , windowController);
                this.collisionStrategy = new HeartStrategy(gameObjects, heart);
                break;
            case DOUBLE:
                int MAX_POW_UPS = 3;
                int originalAdditionalDoubles = 1;
                // array of all types of strategies apart from normal
                        StrategyEnums[] strategyMenu = {
                    StrategyEnums.PUCK,
                    StrategyEnums.PADDLE,
                    StrategyEnums.CAMERA,
                    StrategyEnums.HEART,
                    StrategyEnums.DOUBLE};

            this.collisionStrategy =
                        new DoubleStrategy(gameObjects,strategyMenu , this, brickLocation,
                        brickDimension, MAX_POW_UPS, originalAdditionalDoubles);
                break;
            default:
                return this.collisionStrategy;
        }
        return this.collisionStrategy;
    }

    /**
     *
     * @param brickLocation - needed to set the location of the pucks
     * @param brickDimension - needed to se the location of the pucks
     * @return an array of puck objects
     */
    private Puck[] getPuckArray(Vector2 brickLocation, Vector2 brickDimension) {
        // the number of pucks to be released
        int NUMBER_OF_PUCKS = 3;

        // create the pucks
        Puck[] puckArray = new Puck[NUMBER_OF_PUCKS];
        for (int i = 0; i < NUMBER_OF_PUCKS; i++) {
            float puckXLocation = (float) (brickLocation.x()+((i-1)* (brickDimension.x()/NUMBER_OF_PUCKS)));
            puckArray[i] = new Puck(new Vector2 (puckXLocation, brickLocation.y()),
                   new Vector2(brickDimension.x()*0.333f,brickDimension.x()*0.333f) ,
                    puckImage, sound, gameObjects, windowController.getWindowDimensions());
            puckArray[i].setTag("puck");
        }
        return puckArray;

    }
}
