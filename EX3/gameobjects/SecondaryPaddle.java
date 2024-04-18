package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class SecondaryPaddle extends Paddle{
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    object that receives keyboard presses
     * @param windowDimensions
     * @param minDistFromEdge  minimal distance from the paddle edge to the border
     */

    Counter hitCounter;
    GameObjectCollection gameObjects;

    public SecondaryPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge
    , GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        // this counter counts how many times the secondary paddle got hit.
        this.hitCounter = new Counter(0);
        this.gameObjects = gameObjects;
    }

    public Counter getHitCounter() {
        return hitCounter;
    }

    public void resetHitCounter(Counter hitCounter) {
        hitCounter.reset();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        int NUMBER_OF_SECONDARY_PADDLE_HITS = 3 ;
        super.onCollisionEnter(other, collision);
        // if the paddle got hit by a puck or the main ball (to prevent counts of borders, hearts, etc hits.)
        if (other.getTag().equals("main ball") || other.getTag().equals("puck")){
            hitCounter.increment();
        }
        // if the counter got to 4- remove the paddle and reset the counter
        if (hitCounter.value()>= NUMBER_OF_SECONDARY_PADDLE_HITS ){
            hitCounter.reset();
            gameObjects.removeGameObject(this);

        }
    }
}
