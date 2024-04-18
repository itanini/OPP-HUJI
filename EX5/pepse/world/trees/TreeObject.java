package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a tree object in the game world.
 */
public class TreeObject {
    private final int rootX; // The x coordinate of the root of the tree
    private final int rootY; // The y coordinate of the root of the tree
    private int trunkHeight; // The height of the tree trunk
    private final Block[] trunk; // The blocks that make up the tree trunk
    private Color leafColor; // The color of the leaves on the tree
    private Color trunkColor; // The color of the tree trunk

    private final List<GameObject> treeTop = new ArrayList<GameObject>(); // The leaves on the tree
    private int treeTopWidth; // The width of the tree top
    private int treeTopHeight; // The height of the tree top

    private int rightX; // The x coordinate of the rightmost point of the tree
    private int leftX; // The x coordinate of the leftmost point of the tree

    private final List<Block> branches = new ArrayList<Block>(); // The branches of the tree

    /**
     * Constructs a new tree object.
     * @param rootX The x coordinate of the root of the tree.
     * @param rootY The y coordinate of the root of the tree.
     * @param speciesType The species of tree to create.
     */
    public TreeObject(int rootX, int rootY, SpeciesType speciesType) {
        this.rootX = rootX;
        this.rootY = rootY;

        // Set the colors and size of the tree based on the species type
        Random rand = new Random(rootX);
        switch (speciesType){
             // Random number generator used to randomly generate tree properties
            case OAK:
                leafColor =new Color(50, 200, 30);
                treeTopWidth = 2*(rand.nextInt(3) + 2);
                treeTopHeight = treeTopWidth;
                trunkColor = new Color(100, 50, 20);
                break;

            case CUPRESSUS:
                leafColor =new Color(20, 150, 90);
                treeTopWidth = 1;
                treeTopHeight = rand.nextInt(10) + 10;
                trunkColor = new Color(100, 60, 40);
                break;
        }
        // Randomize the trunk height
        trunkHeight = rand.nextInt(3) + 5;
        trunk = new Block[trunkHeight];

        // Create the image for the tree trunk
        Renderable trunkImage = new RectangleRenderable(ColorSupplier.approximateColor(
                trunkColor
        ));
        // Create the blocks that make up the tree trunk
        for (int i = 0; i < trunk.length; i++) {
            trunk[i] = new Block(new Vector2(rootX,rootY - i * Block.SIZE), trunkImage);
        }

        // Calculate the left and right x coordinates of the tree
        this.leftX = -30 + rootX - treeTopWidth*Leaf.getSize();
        this.rightX = 30 + rootX + treeTopWidth*Leaf.getSize();

        // Create the leaves for the tree
        for (int i = - (int) treeTopWidth; i <= treeTopWidth+1; i++) {
            for (int j = 0; j < treeTopHeight; j++) {
                Renderable leafImage = new RectangleRenderable(ColorSupplier.approximateColor(
                        leafColor
                ));
                treeTop.add(new Leaf(new Vector2(rootX + i * Leaf.getSize(),
                        rootY - trunkHeight * Leaf.getSize() - Leaf.getSize() * j),
                        new Vector2(Leaf.getSize(), Leaf.getSize())
                        , leafImage));
            }
        }
    }
    /**
     * Gets the blocks that make up the tree trunk.
     * @return An array of blocks that make up the tree trunk.
     */
    public Block[] getTrunk() {
        return trunk;
    }

    /**
     * Gets the leaves on the tree.
     * @return A list of game objects representing the leaves on the tree.
     */
    public List<GameObject> getTreeTop() {
        return treeTop;
    }

    /**
     * Gets the x coordinate of the rightmost point of the tree.
     * @return The x coordinate of the rightmost point of the tree.
     */
    public int getRightX() {
        return rightX;
    }

    /**
     * Gets the x coordinate of the leftmost point of the tree.
     * @return The x coordinate of the leftmost point of the tree.
     */
    public int getLeftX() {
        return leftX;
    }

    /**
     * Gets the x coordinate of the root of the tree.
     * @return The x coordinate of the root of the tree.
     */
    public int getRootX() {
        return rootX;
    }

}
