package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.trees.TreeObject;

import java.awt.*;
import java.util.HashMap;

/**
 * The main game manager class for the Pepse game.
 */
public class PepseGameManager extends GameManager {
    private WindowController windowController; // The window controller for the game window
    private Avatar avatar; // The avatar controlled by the player
    private int minXInScope; // The minimum x coordinate within the camera's scope
    private int maxXInScope; // The maximum x coordinate within the camera's scope

    private Tree treesOnBoard; // The trees on the game board

    private Terrain terrain; // The terrain for the game world

    // Hashmaps for tracking trees by their x coordinate
    private static final HashMap<Integer, TreeObject> treeRightXLocationHashmap = new HashMap<>();
    private static final HashMap<Integer, TreeObject> treeLeftXLocationHashmap = new HashMap<>();

    private static final int CYCLE_LENGTH = 30; // The length of a day/night cycle in the game

    /**
     * The main method that runs the game.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game by setting up the window, game objects, and input listeners.
     * @param imageReader The image reader used to load images for the game.
     * @param soundReader The sound reader used to load sounds for the game.
     * @param inputListener The user input listener for the game.
     * @param windowController The window controller for the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        // Initialize the game
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // Create the sky for the game
        Color haloColor = new Color(255, 255, 0, 40);
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);

        // Create the terrain for the game
        terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),
                20, windowController.getWindowDimensions().y()*(2/3f));

        // Save the window controller for later use
        this.windowController = windowController;

        // Calculate the starting position for the avatar
        int avatarStartingX = (int) windowController.getWindowDimensions().x()/2;
        Vector2 avatarStartingPoint = new Vector2(avatarStartingX, terrain.groundHeightAt(avatarStartingX));

        // Create the avatar for the game
        this.avatar = Avatar.create(gameObjects(), Layer.DEFAULT, avatarStartingPoint, inputListener, imageReader);

        // Set the camera for the game to follow the avatar
        this.setCamera(new Camera(avatar,Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

        // Calculate the minimum and maximum x coordinates within the camera's scope
          minXInScope = (int) ((avatar.getCenter().x()-(windowController.getWindowDimensions().x()/2))
          -(avatar.getCenter().x()-(windowController.getWindowDimensions().x()/2))%Block.SIZE);
        maxXInScope = (int) ((avatar.getCenter().x()+(windowController.getWindowDimensions().x()/2))
        + (30 - ((avatar.getCenter().x()+(windowController.getWindowDimensions().x()/2))%30)));

        treesOnBoard = new Tree(gameObjects(), terrain, treeRightXLocationHashmap, treeLeftXLocationHashmap);
        treesOnBoard.createInRange(minXInScope-60 ,maxXInScope+60);


        // null objects to prevent empty layer exceptions
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO,null),
                Tree.LEAF_LAYER);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, Vector2.ZERO,null),
                Tree.TRUNK_LAYER);

        gameObjects().layers().shouldLayersCollide(Tree.LEAF_LAYER,Terrain.groundLayer, true );
        gameObjects().layers().shouldLayersCollide(Tree.TRUNK_LAYER, Layer.DEFAULT, true);

        terrain.createInRange(minXInScope -60, maxXInScope + 60);
        Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(),Layer.BACKGROUND+1, windowController.getWindowDimensions(),
                CYCLE_LENGTH);
        SunHalo.create(gameObjects(), Layer.BACKGROUND+5, sun, haloColor);


    }

    /**
     * Updates the game state.
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int startOfXToRemove;
        int endOfXToRemove;

        int startOfXToCreate;
        int endOfXToCreate;

        // Calculate the new minimum and maximum x coordinates within the camera's scope
        int newMinInScope = (int)((avatar.getCenter().x()-(windowController.getWindowDimensions().x()/2))
                -(avatar.getCenter().x()-(windowController.getWindowDimensions().x()/2))%Block.SIZE);
        int newMaxInScope = (int) ((avatar.getCenter().x()+(windowController.getWindowDimensions().x()/2)) +
                30 + (30 - ((avatar.getCenter().x()+(windowController.getWindowDimensions().x()/2))%30)));

        // Check if the avatar has moved out of the current scope of trees
        if (minXInScope <  newMinInScope){
            // Remove trees to the left of the new scope
            startOfXToRemove = minXInScope;
            endOfXToRemove = newMinInScope;
            // Create new trees to the right of the new scope
            startOfXToCreate = maxXInScope;
            endOfXToCreate = newMaxInScope;

            // Remove trees with right x coordinate less than the left edge of the new scope
            for (int rightX: treesOnBoard.getTreeRightXLocationHashmap().keySet()) {
                if (rightX < avatar.getCenter().x()-(windowController.getWindowDimensions().x()/2)){
                    treesOnBoard.remove(treesOnBoard.getTreeRightXLocationHashmap().get(rightX));
                }
            }
        } else if (minXInScope > newMinInScope){
            // Remove trees to the right of the new scope
            startOfXToRemove = newMaxInScope;
            endOfXToRemove = maxXInScope;
            // Create new trees to the left of the new scope
            startOfXToCreate = newMinInScope;
            endOfXToCreate = minXInScope;

            // Remove trees with left x coordinate greater than the right edge of the new scope
            for (int leftX: treesOnBoard.getTreeLeftXLocationHashmap().keySet()) {
                if (leftX > avatar.getCenter().x()+(windowController.getWindowDimensions().x()/2)){
                    treesOnBoard.remove(treesOnBoard.getTreeLeftXLocationHashmap().get(leftX));
                }
            }
        } else {
            // The avatar has not moved out of the current scope, no updates needed
            return;
        }

        // Remove terrain outside of the new scope
        terrain.removeOutOfRange(startOfXToRemove, endOfXToRemove);
        // Create new terrain and trees within the new scope
        terrain.createInRange(startOfXToCreate-60, endOfXToCreate+60);
        treesOnBoard.createInRange(startOfXToCreate-300, endOfXToCreate+300);

        minXInScope = newMinInScope;
        maxXInScope = newMaxInScope;
    }
}
