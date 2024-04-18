package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private Counter livesCounter;
    private Renderable heartRenderable;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    Vector2 widgetTopLeftCorner;

    private GameObject[] heartsArray;

    float heartSize;

    /**
     * This is the constructor for the graphic lives counter.
     *
     @param widgetTopLeftCorner - the top left corner of the left most heart
     @param widgetDimensions - the dimension of each heart
     @param livesCounter - the counter which holds current lives count
     @param heartRenderable - the image renderable of the hearts
     @param gameObjectsCollection - the collection of all game objects currently in the game

     * **/
    public GraphicLifeCounter
            (Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable heartRenderable,
                              GameObjectCollection gameObjectsCollection, float heartSize) {
        super(widgetTopLeftCorner, widgetDimensions,
                null);
        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.livesCounter = livesCounter;
        this.heartRenderable = heartRenderable;
        this.gameObjectCollection = gameObjectsCollection;
        this.heartSize = heartSize;
        this.numOfLives = livesCounter.value();


        // create an array of hearts to be shown on the screen
        this.heartsArray = new GameObject[4];

        // adds the hearts to the array
        for (int live = 0; live < livesCounter.value(); live++) {
            heartsArray[live] = new GameObject(new Vector2(widgetTopLeftCorner.x() + live * heartSize,
                    this.getTopLeftCorner().y()), new Vector2(heartSize, heartSize), heartRenderable);
            gameObjectsCollection.addGameObject(heartsArray[live], Layer.BACKGROUND);
        }
    }

    /**
     * This method is overwritten from GameObject It removes hearts from the screen
     * if there are more hearts than there are lives left
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // if number of lives is greater than the given value of liveCounter
        if (livesCounter.value() != numOfLives) {
            for (int live = 0; live < numOfLives; live++){
                gameObjectCollection.removeGameObject(heartsArray[live],Layer.BACKGROUND);
            }
            this.numOfLives = livesCounter.value();
            // removes a heart from the array
            //gameObjectCollection.removeGameObject(heartsArray[numOfLives], Layer.BACKGROUND);
            this.heartsArray = new GameObject[4];

            for (int live = 0; live < livesCounter.value(); live++) {
                heartsArray[live] = new GameObject(new Vector2(
                        this.widgetTopLeftCorner.x() + live * heartSize,
                        this.getTopLeftCorner().y()), new Vector2(heartSize, heartSize), heartRenderable);
                gameObjectCollection.addGameObject(heartsArray[live], Layer.BACKGROUND);
            }
        }
    }
}
