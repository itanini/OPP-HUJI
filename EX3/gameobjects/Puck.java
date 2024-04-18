package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball{
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param Sound
     */

    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimension;

    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound Sound,
                GameObjectCollection gameObjects, Vector2 windowDimension) {
        super(topLeftCorner, dimensions, renderable, Sound);
        this.gameObjects = gameObjects;
        this.windowDimension = windowDimension;
    }

    /**
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     *
     * removes the puck from the window when it got out of the display
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y()>windowDimension.y()){
            gameObjects.removeGameObject(this);
        }
    }
}
