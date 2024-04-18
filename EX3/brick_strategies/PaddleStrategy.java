package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.gameobjects.SecondaryPaddle;

public class PaddleStrategy extends CollisionStrategy{
    /**
     * @param gameObjects contains all the objects on the board
     *
     * when an object holding this strategy got hir, a secondary paddle will appear on the window
     * (only if it is not already on the window)
     */


    // if the secondary paddle is on the window the boolean value will change to true
    static boolean secondaryPaddleOnBoard = false;


    // the secondary paddle that is emerging on the window when a brick holding this strategy is hit
    static SecondaryPaddle secondaryPaddle;

    public PaddleStrategy(GameObjectCollection gameObjects,SecondaryPaddle paddle) {
        super(gameObjects);
        PaddleStrategy.secondaryPaddle = paddle;
    }

    /**
     *
     * @param collidedObj the object that got collided (a brick mostly)
     * @param colliderObj mostly the ball
     * @param bricksCounter counts the number of bricks on the board
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        // if the secondary paddle is not on the board
        if (!secondaryPaddleOnBoard) {
            gameObjects.addGameObject(secondaryPaddle);
        // if the secondary paddle counter is 3 (or greater)
        if (secondaryPaddle.getHitCounter().value() >= 3){
            // change the counter value to 0
            secondaryPaddle.resetHitCounter(secondaryPaddle.getHitCounter());
            // remove the paddle from the window
            gameObjects.removeGameObject(secondaryPaddle);
        }
        }
    }
}
