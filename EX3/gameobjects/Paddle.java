package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {

    // Paddle Speed constant
    static float MOVE_SPEED = 1000;
    // window size vector
    private final Vector2 windowDimensions;

    // minimal distance from the paddle edge to the border
    private final int minDistFromEdge;

    // paddle size vector
    private final Vector2 dimensions;

    //object that receives keyboard presses
    private final UserInputListener inputListener;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener object that receives keyboard presses
     * @param dimensions    paddle size vector
     * @param minDistFromEdge minimal distance from the paddle edge to the border
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions, int minDistFromEdge) {
        // create paddle
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
        this.dimensions= dimensions;
    }

// this function called every frame and updates the paddle object

    public void update(float deltaTime){
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        //Detects user's presses on the keys and keeps paddle on the screen (considering min dist from edge).
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && (getTopLeftCorner().x()-minDistFromEdge > 0)){
            movementDir = movementDir.add((Vector2.LEFT));
        }
        if ((inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            && (this.getTopLeftCorner().x()+dimensions.x()+minDistFromEdge<windowDimensions.x())){
            movementDir = movementDir.add(Vector2.RIGHT);
        }

        setVelocity(movementDir.mult(MOVE_SPEED));
    }
}
