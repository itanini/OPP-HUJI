package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {

    // sound played when the ball hits the paddle
    private final Sound collisionSound;
    private static GameObjectCollection gameObjects;



    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */

    // This is the ball object constructor.
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound Sound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = Sound;

    }

    @Override
    // gives the ball a new moving vector when it hits an object
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }
}
