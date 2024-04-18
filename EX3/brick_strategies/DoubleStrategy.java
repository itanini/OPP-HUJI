package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.Factories.StrategyFactory;
import src.Factories.StrategyEnums;

import java.util.Arrays;
import java.util.Random;

public class DoubleStrategy extends CollisionStrategy{
    /**
     * /**
     * this strategy hld a few strategies in it, so the object who got collied will behave
     * according to more than only one strategy
     *
     * @param gameObjects contains all the objects on the board
     * @param MAX_POW_UP a constant determine the maxium number of strategies that can be holded by
     *                   an object that contain a double strategy
     * @param strategiesArray AN ARRAY OF ALL STRATEGIES HOLDED BY THE double strategy
     **/

    //the maximum number of powerups to be hold by double strategy
    private final int MAX_POW_UP;

    //the maximum number of additional doubles that can be randomized.
    private final int originalAdditionalDoubles;

    CollisionStrategy[] strategiesArray;

    /**
     *
     * @param gameObjects contains all the objects on the board
     * @param allStrategiesTypes an array of aall the types of strategy enums. 2 of these
     *                           will be choosen randomaly. normal strategy should not
     *                           be contained in this array
     * @param strategyFactory a factory to produce the strategy objects
     * @param brickLocation the location of a specific brick
     * @param brickDimension the dimension of a specific brick
     */
    public DoubleStrategy(GameObjectCollection gameObjects, StrategyEnums[] allStrategiesTypes,
                          StrategyFactory strategyFactory,
                          Vector2 brickLocation, Vector2 brickDimension, int MAX_POW_UP, int
                                  originalAdditionalDoubles) {
        super(gameObjects);
        this.MAX_POW_UP = MAX_POW_UP;
        this.originalAdditionalDoubles = originalAdditionalDoubles;
        strategiesArray = new CollisionStrategy[MAX_POW_UP];
        // picking random enums that will be the arguments for the factory
        StrategyEnums[] strategiesToBeProduced = getStrategyToBeProduced(allStrategiesTypes);
        // creating the chosen strategies
        for (int i = 0; i < MAX_POW_UP; i++){
            if (strategiesToBeProduced[i] != StrategyEnums.NORMAL){
                strategiesArray[i] =
                        strategyFactory.buildCollisionStrategy(strategiesToBeProduced[i], brickLocation,
                        brickDimension);
            }
        }
    }

    private StrategyEnums[] getStrategyToBeProduced(StrategyEnums[] allStrategiesTypes) {
        /**
         * picking the enums of the strategies randomly
         */
        // creating an array with all "Normal" strategies. the length
        // is the maximum number of strategies that can be hold by this strategy
        StrategyEnums[] strategiesToBeProduced = new StrategyEnums[MAX_POW_UP];
        Arrays.fill(strategiesToBeProduced, StrategyEnums.NORMAL);

        // the lottery. we run ony on the first two spots and only if one of them is double we pick another
        //spot in the array

        //number of possible additional doubles allowed
        int additionalDoubles = originalAdditionalDoubles; //must be smaller the MAX_POW_UP

        for (int j = 0; j < MAX_POW_UP - additionalDoubles; j++){
            Random rand = new Random();

            // picking a strategy from all the possible strategy
            strategiesToBeProduced[j] = allStrategiesTypes[rand.
                    nextInt(allStrategiesTypes.length)];

            // if a double strategy is randomly picked again
            if (strategiesToBeProduced[j] == StrategyEnums.DOUBLE &&
                    strategiesToBeProduced[MAX_POW_UP - additionalDoubles -1] == StrategyEnums.NORMAL){
                // if the strategy is double again or normal we randomly pick another strategy
                while (strategiesToBeProduced[MAX_POW_UP - additionalDoubles-1]
                        == StrategyEnums.NORMAL || strategiesToBeProduced
                        [MAX_POW_UP - additionalDoubles-1] ==
                        StrategyEnums.DOUBLE ) {
                    strategiesToBeProduced[MAX_POW_UP - additionalDoubles-1] =
                            allStrategiesTypes[rand.nextInt(allStrategiesTypes.length)];
                }
                if(additionalDoubles > 0){
                additionalDoubles -=1;}
                //checks if one of the first 2 spots are normal or double and change it
            while (strategiesToBeProduced[j] == StrategyEnums.DOUBLE || strategiesToBeProduced[j] ==
            StrategyEnums.NORMAL){
                strategiesToBeProduced[j] =
                        allStrategiesTypes[rand.nextInt(allStrategiesTypes.length)];
            }
            }
        }
        additionalDoubles = originalAdditionalDoubles;
        return strategiesToBeProduced;
    }

    /**
     *
     * @param collidedObj the object that got collided (a brick mostly)
     * @param colliderObj mostly the ball
     * @param bricksCounter counts the number of bricks on the board
     *
     *   activate all the strategies in the strategies array
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        for (CollisionStrategy strategy : strategiesArray){
            if (strategy != null){
                // every strategy in the array' if not null' will be called by this loop
                strategy.onCollision(collidedObj, colliderObj, bricksCounter);
            }
        }
    }
}

