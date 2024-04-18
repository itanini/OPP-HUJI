package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    // creates an empty string to be filled in the number of lives left
    private static final TextRenderable textRenderer = new TextRenderable("");

    // number of lives left
    private final Counter livesCounter;


    private final GameObjectCollection gameObjectCollection;

    /**
     *The constructor of the textual representation object of how many strikes are left in the game.
     *
     * @param livesCounter The counter of how many lives are left right now.
     * @param topLeftCorner the top left corner of the position of the text object
     * @param dimensions the size of the text object
     * @param gameObjectCollection the collection of all game objects currently in the game
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner,
                              Vector2 dimensions, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner,dimensions, textRenderer);
        this.livesCounter= livesCounter;
        this.gameObjectCollection = gameObjectCollection;

    }

    /**
     * This method is overwritten from GameObject. It sets the string value
     * of the text object to the number of current lives left.
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

        // set the shown string as the number of lives
        textRenderer.setString(String.valueOf(livesCounter.value()));

        // conduct the colour of the digit
        switch (livesCounter.value()){
            case 4:
            case 3:
                textRenderer.setColor(Color.green);
                break;
            case 2:
                textRenderer.setColor(Color.yellow);
                break;
            case 1:
                textRenderer.setColor(Color.red);
                break;
        }
    }
}

