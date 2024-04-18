package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * This class represents the avatar of the game. Its constructor is private but the create method instantiates the
 * class and returns the instance.
 */
public class Avatar extends GameObject {
        private static final int MOVEMENT_SPEED_FACTOR = 300; // The speed of walking sideways
        private static final int FLY_SPEED_FACTOR = 300; // The speed of flying
        private static final int JUMP_INITIAL_SPEED = 300; // The initial upwards speed of a jump
        private static Renderable restingRenderable; // The resting (no movement) rendered image
        private static Renderable jumpingRenderable; // The jumping rendered image.
        private static Renderable runningRenderable; // The running rendered image.
        private static Renderable flyingRenderable; // The flying rendered image.
        private final UserInputListener inputListener; // The input listener in charge of collecting user input.
        private float energy = 100; // The energy level of the avatar.

        private boolean lastFacingLeft = false; // Was the last direction the avatar faced left?

        /**
         * This method created the avatar object and sets the required static fields for further use.
         * @param gameObjects
         * @param layer
         * @param topLeftCorner The top left corner of the object.
         * @param inputListener The input listener object in charge of collecting inputs for the game.
         * @param imageReader The image reader object that reads images and creates static render-ables.
         * @return The avatar GameObject.
         */
        public static Avatar create(GameObjectCollection gameObjects,
                                    int layer, Vector2 topLeftCorner,
                                    UserInputListener inputListener,
                                    ImageReader imageReader) {

                String[] imagePathsRun = {"src/assets/Run/HeroKnight_Run_0.png",
                        "src/assets/Run/HeroKnight_Run_1.png", "src/assets/Run/HeroKnight_Run_2.png",
                        "src/assets/Run/HeroKnight_Run_3.png", "src/assets/Run/HeroKnight_Run_4.png",
                        "src/assets/Run/HeroKnight_Run_5.png", "src/assets/Run/HeroKnight_Run_6.png",
                        "src/assets/Run/HeroKnight_Run_7.png"};

                String[] imagePathsResting = {"src/assets/Idle/HeroKnight_Idle_0.png",
                        "src/assets/Idle/HeroKnight_Idle_1.png", "src/assets/Idle/HeroKnight_Idle_2.png",
                        "src/assets/Idle/HeroKnight_Idle_3.png", "src/assets/Idle/HeroKnight_Idle_4.png",
                        "src/assets/Idle/HeroKnight_Idle_5.png", "src/assets/Idle/HeroKnight_Idle_6.png",
                        "src/assets/Idle/HeroKnight_Idle_7.png"};


                String imagePathJump = "src/assets/Jump/HeroKnight_Jump_1.png";

                String imagePathFly = "src/assets/Fly/HeroKnight_Fall_2.png";

                restingRenderable = new AnimationRenderable(
                        imagePathsResting, imageReader, true, 0.1);

                runningRenderable = new AnimationRenderable(
                        imagePathsRun, imageReader, true, 0.1);


                jumpingRenderable = imageReader.readImage(imagePathJump, true);

                flyingRenderable = imageReader.readImage(imagePathFly, true);

                Vector2 avatarDimensions = new Vector2(100, 66);


                Avatar avatar = new Avatar(topLeftCorner.add(avatarDimensions.mult(-1f)), avatarDimensions,
                        restingRenderable, inputListener);

                avatar.transform().setAccelerationY(500); // set gravity acceleration

                avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                gameObjects.addGameObject(avatar, layer);

                return avatar;
        }

        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         *                      the GameObject will not be rendered.
         */
        private Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener) {
                super(topLeftCorner, dimensions, renderable);
                this.inputListener = inputListener;

        }

        /**
         * This method overrides the GameObjects onCollisionEnter method but adds the feature that allows the avatar to
         * collide with blocks and tree trunks.
         * @param other The GameObject with which a collision occurred.
         * @param collision Information regarding this collision.
         *                  A reasonable elastic behavior can be achieved with:
         *                  setVelocity(getVelocity().flipped(collision.getNormal()));
         */
        @Override
        public void onCollisionEnter(GameObject other, Collision collision) {
                super.onCollisionEnter(other, collision);
                if (other.getTag().equals("trunk") || other.getTag().equals("block")){
                        setVelocity(Vector2.ZERO);

                }
        }


        /**
         *  This method overrides the GameObjects update method but adds many more required features:
         *  1) The ability to move the avatar according to the users inputs.
         *  2) The changes in the avatars renderable according to recent inputs or its velocity.
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
                boolean flying = false;

                // ### Movement ###

                float yComponent = this.getVelocity().y();
                Vector2 movementDir = new Vector2(0, yComponent);

                //move left
                if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
                        movementDir = movementDir.add(Vector2.LEFT.mult(MOVEMENT_SPEED_FACTOR));
                        lastFacingLeft = true;
                }

                //move right
                if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                        movementDir = movementDir.add(Vector2.RIGHT.mult(MOVEMENT_SPEED_FACTOR));
                        lastFacingLeft = false;
                }

                // jump
                if ((inputListener.isKeyPressed(KeyEvent.VK_SPACE)) && (this.getVelocity().y() == 0)) {
                        movementDir = movementDir.add(Vector2.UP.mult(JUMP_INITIAL_SPEED));
                }

                // fly
                if ((inputListener.isKeyPressed(KeyEvent.VK_SPACE)) && (inputListener.isKeyPressed(KeyEvent.VK_SHIFT))
                        && this.energy > 0) {
                        movementDir = new Vector2(movementDir.x(), -FLY_SPEED_FACTOR);
                        flying = true;
                        energy -= 0.5;
                }

                this.setVelocity(movementDir);

                // ### Renderable ###

                renderer().setIsFlippedHorizontally(false);


                if (this.getVelocity().y() == 0) {
                        // run right
                        if (this.getVelocity().x() > 0) {
                                renderer().setRenderable(runningRenderable);
                        }

                        // run left
                        if (this.getVelocity().x() < 0) {
                                renderer().setRenderable(runningRenderable);
                                renderer().setIsFlippedHorizontally(true);
                        }

                        // rest
                        if (this.getVelocity().x() == 0) {
                                renderer().setRenderable(restingRenderable);
                                if (lastFacingLeft) {
                                        renderer().setIsFlippedHorizontally(true);
                                }
                        }
                }

                else {
                        // flying or jumping
                        Renderable inAirRenderable = jumpingRenderable;
                        if (flying) {inAirRenderable = flyingRenderable;}

                        // when x-axis speed is not - 0
                        if (this.getVelocity().x() != 0) {
                                renderer().setRenderable(inAirRenderable);
                                if (this.getVelocity().x() < 0) {
                                        renderer().setIsFlippedHorizontally(true);
                                }
                        }

                        // when x-axis speed is nil, face the last direction in which the avatar moved
                        else {
                                renderer().setRenderable(inAirRenderable);
                                if (lastFacingLeft) {
                                        renderer().setIsFlippedHorizontally(true);
                                }
                        }
                }

                // set max fall speed to fix bug in DanoGameLab that lets you move through objects at fast speeds

                if (this.getVelocity().y() > 600) {
                        this.setVelocity(new Vector2(this.getVelocity().x(), 600));
                }


                // recharge energy

                if ((this.getVelocity().y() == 0) && (this.energy < 100)) {
                        this.energy += 0.5; // add Energy
                }
        }
}

