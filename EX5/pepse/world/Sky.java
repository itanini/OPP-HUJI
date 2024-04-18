package pepse.world;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class represents the sky background of the game.
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5"); // The standard sky color

    /***
     * The sky constructor.
     * @param gameObjects The GameObjectsCollection that contains all the GameObjects of the game.
     * @param windowDimensions The dimensions of the game window.
     * @param skyLayer The layer of which to put the sky object.
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag("sky");
        return sky;
    }
}

