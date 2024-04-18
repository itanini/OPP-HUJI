package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * This class represents a leaf object in the game.
 */
public class Leaf extends GameObject {

    private final Vector2 originalTopLeftCorner; // This field stores the original top-left corner position
    // of the leaf object in window coordinates (pixels).;

    private static final int SIZE = 20; // This field is a constant representing
    // the size of the leaf object in window coordinates.

    private static final int FADEOUT_TIME = 8; //This field is a constant representing
    // the time it takes for the leaf object to fade out.
    private float INIT_ANGLE = 10; // This field stores the initial angle of the leaf object.
    private float FINAL_ANGLE = 15; //This field stores the final angle of the leaf object.
    private float TRANSITION_TIME = 0.2f; // This field stores the time it takes for the angle of the leaf
    // object to transition from the initial angle to the final angle.
    private int fallingTime; // This field stores the time it takes for the leaf object to fall.
    private int dyingTime; // This field stores the time it takes for the leaf object to die (fade out).
    private final Random rand = new Random(); // This field is a Random object used to generate random numbers.

    private final Vector2 dimensions; // This field stores the dimensions (width and height)
    // of the leaf object in window coordinates.


    /**
     * Construct a new GameObject instance.k
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.originalTopLeftCorner = topLeftCorner;

        this.dimensions = Vector2.ONES.mult(SIZE*1.2f);
        this.fallingTime = rand.nextInt(60);

        this.setTag("leaf");



        float waitTime = rand.nextFloat();
        // changes the width of the leaf
        new ScheduledTask(this, waitTime, false,
                this::widthChanger);

        // changes the angel of the leaf
        new ScheduledTask(this, waitTime, false,
                this::angelChanger);

        //make the leaf fall
        new ScheduledTask(this, fallingTime, false,
                this::leafFall);

    }

    /**
     * Changes the angle of the leaf over time.
     */
    private void angelChanger() {
        // changes the angel of the leaf
        new Transition<Float>(this,
                angle -> this.renderer().setRenderableAngle(angle),INIT_ANGLE, FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
    }

    /**
     * Changes the width of the leaf over time.
     */
    private void widthChanger() {
        // changes the width of the leaf
        new Transition<Float>(this,
                width -> this.setDimensions(new Vector2(width, SIZE)), dimensions.x(),
                dimensions.x()*0.6f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
    }

    /**
     * Schedules a task to revive the leaf after it has died.
     */
    private void afterDying() {
        // Schedule a task to revive the leaf
        new ScheduledTask(this, dyingTime, false,
                this::leafReviver);
    }

    /**
     * Makes the leaf fall and fades it out.
     */
    private void leafFall() {
        // Set the falling speed and fade the leaf out
        this.transform().setVelocityY(150);
        dyingTime = rand.nextInt(5);
        this.renderer().fadeOut(FADEOUT_TIME, this::afterDying);
    }


    /**
     * Revives the leaf by resetting its position, velocity, and renderer.
     * Schedules a new task to make the leaf fall again.
     */
    private void leafReviver() {
        // Reset the leaf's position, velocity, and renderer
        this.setTopLeftCorner(originalTopLeftCorner);
        this.transform().setVelocityY(0);
        this.renderer().fadeIn(1);
        // Choose a new falling time and schedule a task to make the leaf fall again
        this.fallingTime = rand.nextInt(60);
        new ScheduledTask(this, fallingTime, false,
                this::leafFall);
    }

    /**
     * Returns the size of the leaf.
     *
     * @return the size of the leaf
     */
    public static int getSize() {
        return SIZE;
    }

    /**
     * Handles collision events when the leaf collides with another object.
     * If the object is a block, the leaf's velocity is set to zero.
     *
     * @param other the other object that the leaf has collided with
     * @param collision the collision information
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("block")) {
            this.setVelocity(Vector2.ZERO);
        }
    }

}
