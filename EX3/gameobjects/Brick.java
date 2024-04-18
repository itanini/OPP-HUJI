package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.CollisionStrategy;

public class Brick extends GameObject {

    static Counter counter;
    private CollisionStrategy strategy;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param strategy      conducts the brick behaviour when it gets hit by the ball
     * @param counter       counts the number of brickes on the board
     */


    /**
     *
     * @param topLeftCorner- location of the top left corner of the brickk
     * @param dimensions - dimenssion of the brick
     * @param renderable - image of the brick
     * @param strategy - the behaviour of the brick when got collied
     * @param counter - counts the number of bricks on the board

    This constructor extends the super's GameObject constructor, and also saves the strategy given.
    gives it also its strategy
    **/

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        Brick.counter = counter;
    }

    // functions called when the brick got hit by the ball ( or other objects)
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {

        // conducts the given strategy of the brick
        strategy.onCollision(this, other, counter);
    }

}
