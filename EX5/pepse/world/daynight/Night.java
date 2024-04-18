package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class is in charge of creating the night object in charge of creating the night and day brightness change
 * effect.
 */
public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * This method creates the night effect -  a rectangle game object that is black in color and changes opaqueness
     * from completely transparent to 0.5 opaque throughout a day cycle (30 sec).
     * @param gameObjects The GameObjectsCollection that holds all the objects of the game.
     * @param layer The layer to which to add the night.
     * @param windowDimensions A vector2 object of the game window dimensions.
     * @param cycleLength The length of a sunrise-sunset cylcle (A.K.A a day)
     * @return The night GameObject with the leeched transition.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        GameObject night = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.black));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag("night");

        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength/2, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null); // nothing further to execute upon reaching final value
        return night;


    }
}
