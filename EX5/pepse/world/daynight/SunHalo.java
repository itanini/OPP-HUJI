package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.GameObjectFollower;

import java.awt.*;

/**
 * This class is in charge of creating the suns halo.
 */

public class SunHalo {
    /** This method creates the suns halo.
     *
     * @param gameObjects The GameObjectsCollection that holds all the objects of the game.
     * @param layer The layer to which to add the sun's halo.
     * @param sun The suns GameObject.
     * @param color The color to which to set the suns halo.
     * @return The suns halo GameObject object (upcasted from GameObjectFollower)
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        Renderable haloRenderable = new OvalRenderable(color);
        GameObject halo = new GameObjectFollower(sun.getCenter(), new Vector2(200, 200), haloRenderable, sun);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(halo, layer);
        return halo;
    }

}
