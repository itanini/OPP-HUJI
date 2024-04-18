package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.counters.BallHitCounter;

public class MainBall extends Ball{
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param Sound
     * @param gameObjects
     */
    private BallHitCounter hitCounter;
    public MainBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    Sound Sound) {
        super(topLeftCorner, dimensions, renderable, Sound);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        int NUMBER_OF_HITS_TO_STOP_CAMERA = 4;
        super.onCollisionEnter(other, collision);
        // hit counter will be created when the camera starts to follow an object
        if (!(hitCounter == null)){
            hitCounter.increment();

            //IF THE COUNTER GOT TO THE GIVEN NUMBER OF HITS: STOP THE OBJECT FOLLOW
            if (hitCounter.value() >= NUMBER_OF_HITS_TO_STOP_CAMERA){
                hitCounter.stopCameraChange();
                hitCounter = null;
            }
        }
    }

    /**
     *
     * @param hitCounter counts the number of the main ball hits
     *
     *                   sets a new zeroed counter when an object with a camera strategy is collied by the
     *                   main ball, and there is no other counter.
     *
     */
    public void setHitCounter(BallHitCounter hitCounter) {
        this.hitCounter = hitCounter;
    }
}
