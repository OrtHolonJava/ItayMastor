package com.itay347.finaldays.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.itay347.finaldays.Actors.Enemy;
import com.itay347.finaldays.Actors.Player;

/**
 * The manager of AI path-finding
 */
public class AIManager {
    /**
     * A reference the AIManager's instance
     */
    private static AIManager Instance;

    /**
     * The graph of the map
     */
    private AIGraph graph;
    /**
     * The pathfinder (with the AStar algorithm)
     */
    private IndexedAStarPathFinder<AINode> pathFinder;
    /**
     * The heuristic used in the AStar algorithm
     */
    private Heuristic<AINode> heuristic;
    /**
     * The result path
     */
    private DefaultGraphPath<Connection<AINode>> resultPath;

    /**
     * A reference to the player for getting his position
     */
    private Player player;

    /**
     * Init the AI Manager
     *
     * @param player The player's actor (for accessing his node)
     */
    public AIManager(Player player) {
        this.graph = new AIGraph();
        this.pathFinder = new IndexedAStarPathFinder<AINode>(graph);
        this.resultPath = new DefaultGraphPath<Connection<AINode>>();
        this.heuristic = new Heuristic<AINode>() {
            @Override
            public float estimate(AINode node, AINode endNode) {
                // Manhattan distance
                return Math.abs(endNode.tileX - node.tileX) + Math.abs(endNode.tileY - node.tileY);
            }
        };

        this.player = player;
        AIManager.Instance = this;
    }

    /**
     * Runs the AStar Algorithm
     *
     * @param enemy The enemy actor
     * @return The next node for the enemy to go to
     */
    public AINode findNextNode(Enemy enemy) {
        // Get the start and end nodes
        AINode startNode = enemy.getNodeOfMyPosition();
        AINode endNode = player.getNodeOfMyPosition();
        // Clear the path for the next search
        resultPath.clear();
        // Run the pathfinder
        pathFinder.searchConnectionPath(startNode, endNode, heuristic, resultPath);
        // Return the next node
        return resultPath.getCount() == 0 ? null : resultPath.get(0).getToNode();
    }

    /**
     * @return the AIManager instance
     */
    public static AIManager getInstance() {
        return Instance;
    }

    /**
     * @return the graph of the map
     */
    public AIGraph getGraph() {
        return graph;
    }
}
