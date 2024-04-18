package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.GraphicLifeCounter;
import src.gameobjects.Heart;
import src.gameobjects.NumericLifeCounter;

public class HeartStrategy extends CollisionStrategy{
    /**
     * @param heart an heart object to be fallen by the brick
     *
     * when an object holding this strategy will get collied it will drop an heart that can
     *              be caught by the paddle
     */

    Heart heart;

    //this constant determine the speed of the heart when falling
    Vector2 HEART_VELOCITY = new Vector2(0, 100);
    public HeartStrategy(GameObjectCollection gameObjects, Heart heart) {
        super(gameObjects);
        this.heart = heart;
    }

    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        heart.setVelocity(HEART_VELOCITY);
        // puts the heart on the window when the brick got collied
        gameObjects.addGameObject(heart);
    }
}
