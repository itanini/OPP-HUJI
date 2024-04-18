package pepse.util;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This is the GameObjectFollower class designed to create a GameObject object that can follow another GameObject.
 */
public class GameObjectFollower extends GameObject {
    private GameObject objectToFollow;

    /**
     * This is the GameObjectFollower constructor that creates an instance.
     * @param topLeftCorner The top left corner of the corner.
     * @param dimensions The Game object dimensions.
     * @param renderable The renderable that the GameObjects renders.
     * @param objectToFollow The GameObject that this object follows.
     */
    public GameObjectFollower(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                              GameObject objectToFollow) {
        super(topLeftCorner, dimensions, renderable);
        this.objectToFollow = objectToFollow;
    }

    /**
     * This method overrides the original GameObject update method and adds the functionality to follow another
     * GameObject.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        this.setCenter(objectToFollow.getCenter());
        super.update(deltaTime);
    }
}
