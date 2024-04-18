package src.counters;

import danogl.GameManager;
import danogl.util.Counter;

public class BallHitCounter extends Counter {
    /**\
     * this counter counts the number of time the ball had hit other object and set of the camera follow
     * of the ball
     */
    private final GameManager gameManager;

    public BallHitCounter(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void stopCameraChange(){
        // called when the counter got to the number of hits that set to turn off the camera follow of
        // the ball
        gameManager.setCamera(null);
    }
}
