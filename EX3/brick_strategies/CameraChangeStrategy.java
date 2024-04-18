package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.counters.BallHitCounter;
import src.gameobjects.MainBall;

public class CameraChangeStrategy extends CollisionStrategy{
    /**
     *
     * when a brick holding this strategy collied, the camera follows the ball until the ball
     * collides 4 times
     * @param mainBall - the main ball in the game
     * @param gameManager - used to call the camera change
     * @param windowController - used to get the windows dimensions
     */

    static private MainBall mainBall;
    static private GameManager gameManager;
    static private WindowController windowController;

    /**
     *
     * @param gameObjects -holdes all the objects on the board
     */
    public CameraChangeStrategy(GameObjectCollection gameObjects, MainBall mainBall, GameManager gameManager,
    WindowController windowController)
    {
        super(gameObjects);
        CameraChangeStrategy.mainBall = mainBall;
        CameraChangeStrategy.gameManager =gameManager;
        CameraChangeStrategy.windowController = windowController;
    }

    /**
     *
     * @param collidedObj the object that got collided (a brick mostly)
     * @param colliderObj mostly the ball
     * @param bricksCounter counts the number of bricks on the board
     *
*      tnis function is conducts the objects behaviour when it got collied
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        // checks if the colliding object is the main ball
        if (colliderObj.getTag().equals("main ball")) {
            // if the camera is not following the main ball at the moment
            if (gameManager.getCamera() == null) {
                gameManager.setCamera(
                        new Camera(
                                mainBall, //object to follow
                                Vector2.ZERO, //follow the center of the object
                                windowController.getWindowDimensions().mult(1.2f),
                                //widen the frame a bit
                                windowController.getWindowDimensions() //share the window dimensions
                        )
                );
                //create a new counter to count the number of the ball hits.
                mainBall.setHitCounter(new BallHitCounter(gameManager));
            }
        }
    }
}
