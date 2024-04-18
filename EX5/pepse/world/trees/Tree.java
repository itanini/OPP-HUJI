package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * A class representing a tree in a game world.
 *
 * @author danogl
 */
public class Tree {

    /**
     * A random number generator used to generate a seed for the trees in this class.
     */
    private static final Random initRand = new Random();

    /**
     * The seed used to generate random values for this tree.
     */
    private static final int seed = initRand.nextInt(100);

    /**
     * The layer at which the leaves of this tree are rendered.
     */
    public static final int LEAF_LAYER = 10;

    /**
     * The layer at which the trunk of this tree is rendered.
     */
    public static final int TRUNK_LAYER = -110;

    /**
     * The game object collection that holds the visual representations of this tree.
     */
    private final GameObjectCollection gameObjects;

    /**
     * The terrain that this tree exists on.
     */
    private Terrain terrain;

    /**
     * A hash map that maps the x-coordinate of the root of a tree to the tree object itself.
     */
    private final HashMap<Integer, TreeObject> treeRootXLocationHashmap = new HashMap<>();

    /**
     * A hash map that maps the x-coordinate of the right side of a tree's trunk to the tree object itself.
     */
    private final HashMap<Integer, TreeObject> treeRightXLocationHashmap;

    /**
     * A hash map that maps the x-coordinate of the left side of a tree's trunk to the tree object itself.
     */
    private final HashMap<Integer, TreeObject> treeLeftXLocationHashmap;

    /**
     * Creates a new tree.
     *
     * @param gameObjects the game object collection that holds the visual representations of this tree
     * @param terrain the terrain that this tree exists on
     * @param treeRightXLocationHashmap a hash map that maps the x-coordinate of the right side of a tree's trunk
     *                                  to the tree object itself
     * @param treeLeftXLocationHashmap a hash map that maps the x-coordinate of the left side of a tree's trunk
     *                                 to the tree object itself
     */
    public Tree(GameObjectCollection gameObjects, Terrain terrain,
                HashMap<Integer, TreeObject> treeRightXLocationHashmap,
                HashMap<Integer, TreeObject> treeLeftXLocationHashmap) {

        this.gameObjects = gameObjects;
        this.terrain = terrain;
        this.treeRightXLocationHashmap = treeRightXLocationHashmap;
        this.treeLeftXLocationHashmap = treeLeftXLocationHashmap;
    }

/**
 * Generates and creates a tree within a specified range of x-coordinates.
 *
 * @param minX
**/

public void createInRange(int minX, int maxX) {
        for (int x = minX; x <= maxX ; x+=30) {
                if (!(treeRootXLocationHashmap.containsKey(x))){
                    Random rand = new Random(Objects.hash(seed, x));
                    int randomInt = rand.nextInt(100);
                    if (randomInt>92) {
                        SpeciesType[] speciesTypes = {SpeciesType.OAK, SpeciesType.CUPRESSUS};
                        int randomSpecies = rand.nextInt(speciesTypes.length);
                        TreeObject tree = new TreeObject(x, terrain.groundHeightAt(x),
                                speciesTypes[randomSpecies]);
                        treeRootXLocationHashmap.put(x, tree);
                        treeLeftXLocationHashmap.put(tree.getLeftX(), tree);
                        treeRightXLocationHashmap.put(tree.getRightX(), tree);
                        for (GameObject trunk : tree.getTrunk()) {
                            trunk.setTag("trunk");
                            gameObjects.addGameObject(trunk, TRUNK_LAYER);
                        }
                        for (GameObject treeTopObject : tree.getTreeTop()) {
                            gameObjects.addGameObject(treeTopObject, LEAF_LAYER);
                        }
                }
            }
        }
    }

    /**
     * The method getTreeRightXLocationHashmap returns the treeRightXLocationHashmap object,
     * which maps the right x-coordinates of the trees to the corresponding TreeObject instances.
     *
     */
    public HashMap<Integer, TreeObject> getTreeRightXLocationHashmap() {
        return treeRightXLocationHashmap;
    }

    /**
     * The method getTreeLeftXLocationHashmap returns the treeLeftXLocationHashmap object,
     * which maps the Left x-coordinates of the trees to the corresponding TreeObject instances.
     *
     */
    public HashMap<Integer, TreeObject> getTreeLeftXLocationHashmap() {
        return treeLeftXLocationHashmap;
    }

    /**
     * Removes a tree object from the game world.
     * @param treeToRemove The tree object to be removed.
     */
    public void remove(TreeObject treeToRemove) {
        // Loop through all leaf objects of the tree and remove them from the game world
        for (GameObject leaf : treeToRemove.getTreeTop()) {
            gameObjects.removeGameObject(leaf, LEAF_LAYER);
        }
        // Loop through all trunk objects of the tree and remove them from the game world
        for (GameObject trunkBlock : treeToRemove.getTrunk()) {
            gameObjects.removeGameObject(trunkBlock, TRUNK_LAYER);
        }
        // Remove the tree from the hashmap that tracks tree roots by their x location
        treeRootXLocationHashmap.remove(treeToRemove.getRootX());
    }
}
