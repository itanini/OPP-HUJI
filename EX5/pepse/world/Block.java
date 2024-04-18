package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents a block object in the game.
 */
public class Block extends GameObject {
    public static final int SIZE = 30; // The standard size of a ground block object.

    /**
     * Constructs a ground Block object.
     * @param topLeftCorner The top left corner of the object.
     * @param renderable The Renderable of the object.
     */

    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
