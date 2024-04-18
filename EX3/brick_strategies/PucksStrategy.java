package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Brick;

import java.util.Random;


public class PucksStrategy extends CollisionStrategy{
    /**
     * @param gameObjects contains all the objects on the board
     *
     * when an object holding this strategy got hit, it will release 3 small balls (puck) into the window
     * space
     */

    // puckSpeed
    private final float puckSpeed;

    // holds the puck objects to be released when the object got collied
    private final Ball[] puckArray;

    public PucksStrategy(GameObjectCollection gameObjects, Ball[] puckArray,
                         float puckSpeed) {
        super(gameObjects);
        this.puckSpeed = puckSpeed;
        this.puckArray= puckArray;
    }

    /**
     *
     * @param collidedObj the object that got collided (a brick mostly)
     * @param colliderObj mostly the ball
     * @param brickCounter counts the number of bricks on the board
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj,
                            Counter brickCounter) {
        // set the velocity of the puck objects. the direction is random (for diagonal options) speed is
        // puckSpeed
        float ballVelX = this.puckSpeed;
        float ballVelY = this.puckSpeed;

        // release all pucks in the pucksArray
        for (int i = 0; i < puckArray.length; i++) {
            Random rand = new Random();
            if (rand.nextBoolean()){
                ballVelX *= -1;
            }
            if (rand.nextBoolean()){
                ballVelY *= -1;
            }
            puckArray[i].setVelocity(new Vector2(ballVelX, ballVelY));

            // adds the pucks to the window
            gameObjects.addGameObject(puckArray[i]);
        }
        super.onCollision(collidedObj,colliderObj,brickCounter);
    }
}
