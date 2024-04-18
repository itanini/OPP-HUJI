package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represent the terrain in the game. It is in charge of building and creating the blocks in the range of
 * the players window view.
 */

public class Terrain {
    private static final int TERRAIN_DEPTH = 20; // The depth of each terrain block
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74); // The basic color of the ground
    private static Vector2 windowDimensions;
    private GameObjectCollection gameObjects; // The collection of the all the game objects in the game.
    public static int groundLayer; // The layer in which to put the ground objects.
    private float groundHeightatX0 = 0; // The value representing the height of the ground at x = 0.
    private NoiseGenerator noiseGenerator; // An instance of the Noise Generator (perlin noise function) for uses in
    // generating infinite pseudo random terrain.

    private final static HashMap<Integer, ArrayList<Block>> blocksInAGivenXHashMap = new HashMap<>(); // A map object
    // that contains all the blocks in a specific x coordinate. This map is used to retain each blocks x coordinate for
    // deleting in the avatars field of view.


    /**
     * A constructor for the Terrain object. This constructor has no groundHeightatX0 paraeter and defaults it to 0.
     * @param gameObjects The GameObjectsCollection that contains all the GameObjects of the game.
     * @param groundLayer  The layer of which to put the ground object.
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed of the random generator.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        noiseGenerator = new NoiseGenerator(seed);
    }

    /**
     *
     * @param gameObjects The GameObjectsCollection that contains all the GameObjects of the game.
     * @param groundLayer  The layer of which to put the ground object.
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed of the random generator.
     * @param groundHeightAtX0 The initial height of the ground at x=0.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed, float groundHeightAtX0) {
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.groundLayer = groundLayer;
        this.groundHeightatX0 = groundHeightAtX0;
        this.noiseGenerator = new NoiseGenerator(seed);
    }

    /**
     * This method is in charge of creating repoducable pseudo random heights for the ground level in our "Randomly
     * Generated Terrain".
     *
     * @param x The x coordinate for which to generater the random terrain height.
     * @return a int of the height of the ground at the specified x coordinate.
     */
    public int groundHeightAt(float x) {
        double noise = this.noiseGenerator.noise(x / 10f);
        double modNoise = (noise + 1.0) * 0.5f; // Change the perlin noise from values in the (-1, 1) range to values
        // in the (0,1) range.

        modNoise = (modNoise + 1) / 3; // Make fluctuations less extreme by changing the change in amplitude
        // of the function

        return (int) Math.floor(modNoise * groundHeightatX0 / Block.SIZE) * Block.SIZE;
    }


    /**
     * This method creates all the ground blocks in a specific range of x coordinates.
     * It uses the noiseGenerator object in order to assign each x coordinate a height and then creates all the blocks
     * needed to fill that height and hashes them to the blocksInAGivenXHashMap for use later in deleting them should
     * they move out of the users view.
     * @param minX The bottom value of the range in which to create the blocks.
     * @param maxX The top value of the range in which to create the blocks.
     */
    public void createInRange(int minX, int maxX) {

        for (int i = minX; i <= maxX; i += 30) {
            if (!(blocksInAGivenXHashMap.containsKey(i))) {
                int heightOfBlocks = (int) groundHeightAt(i);
                ArrayList<Block> blocksAtX = new ArrayList<>();
                for (int j = 1; j <= TERRAIN_DEPTH; j++) {
                    int blockHeight = heightOfBlocks + j * 30;

                    // Block Renderable
                    Renderable blockImage = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

                    // Block Location
                    Vector2 blockLocation = new Vector2(i, blockHeight);

                    Block block = new Block(blockLocation, blockImage);
                    blocksAtX.add(block);
                    if (j == 1) {
                        gameObjects.addGameObject(block, groundLayer);
                    } else {
                        gameObjects.addGameObject(block, groundLayer + 1);
                    }
                    block.setTag("block");
                }
                blocksInAGivenXHashMap.put(i, blocksAtX);
            }
        }
    }

    /**
     * This method removes all objects that are outside a specific range. It is used for deleting objects which are no
     * longer in the users view.
     * @param startOfXToRemove The bottom value of the range in which to create the blocks.
     * @param endOfXToRemove The top value of the range in which to create the blocks.
     */
    public void removeOutOfRange(int startOfXToRemove, int endOfXToRemove) {
        for (int x = startOfXToRemove; x < endOfXToRemove - 30; x += Block.SIZE) {
            if (blocksInAGivenXHashMap.containsKey(x)) {
                ArrayList<Block> blocksToRemove = blocksInAGivenXHashMap.get(x);
                for (Block block :
                        blocksToRemove) {
                    gameObjects.removeGameObject(block, Terrain.groundLayer);
                }
                blocksInAGivenXHashMap.remove(x);
            }
        }
    }

}