package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class is in charge of creating the sun object.
 */

public class Sun {
    /**
     * This class creates the sun.
     * @param gameObjects The GameObjectsCollection that holds all the objects of the game.
     * @param layer The layer to which to add the sun.
     * @param windowDimensions A vector2 object of the game window dimensions.
     * @param cycleLength The length of a sunrise-sunset cylcle (A.K.A a day)
     * @return A game object sun that has a leeched Transition in charge of Movement.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
                GameObject sun = new GameObject(windowDimensions.mult(0.5f), new Vector2(100, 100),
                        new OvalRenderable(Color.YELLOW)); // create the sun object
                sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                gameObjects.addGameObject(sun, layer);
                sun.setTag("sun");

                // Create the suns movement across the sky
                float initAngle = (float) (0.6*Math.PI);
                float finishAngle = (float) (2.6 * Math.PI);

                new Transition<Float>(sun, angle -> sun.setCenter(calcSunPosition(windowDimensions, angle)),
                        initAngle, finishAngle, Transition.LINEAR_INTERPOLATOR_FLOAT,
                        cycleLength, Transition.TransitionType.TRANSITION_LOOP, null);
                return sun;
    }

    /**
     * This function calculates the suns position according to a supplied angle.
     * @param windowDimensions The game window dimensions.
     * @param angleInSky The angle (in Radians) of the sun in relation to the UP vector.
     * @return a Vector2 object containing the required location of the sun according to the angle.
     */

    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky) {
        // use the ellipse shape parametrization: (x,y) = (a*cos(theta), b*sin(theta))
        // generate a and b values in relation to the window dimensions
        double a = windowDimensions.x() * 0.5;
        double b= windowDimensions.x() * 0.3;

        // calculate the ellipse coordinates
        float xPartOfMovement = (float) (a * Math.sin(angleInSky));
        float yPartOfMovement = (float) (b * Math.cos(angleInSky));

        // add the coordinate to the center of the window so that the sun rotates around the center
        return windowDimensions.mult(0.5f).add(new Vector2(xPartOfMovement, yPartOfMovement));
    }
}
