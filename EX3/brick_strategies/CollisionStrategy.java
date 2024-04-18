package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;



public class CollisionStrategy{


    static GameObjectCollection gameObjects;

    /**
     *
     * @param gameObjects contains all the objects on the board
     *
     * a parent class to many others, determines the behaviour of the object when got collied.
     *                    the function on collision is inherited to the other classed (that decorate it)
     */
    public CollisionStrategy(GameObjectCollection gameObjects){
        CollisionStrategy.gameObjects = gameObjects;
    }

    /**
     *
     * @param collidedObj the object that got collided (a brick mostly)
     * @param colliderObj mostly the ball
     * @param bricksCounter counts the number of bricks on the board
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {

        // removes the brick from the board
        if (gameObjects.removeGameObject(collidedObj)) {
            // decrement the number of bricks in the counter
            bricksCounter.decrement();
        }
    }
}
