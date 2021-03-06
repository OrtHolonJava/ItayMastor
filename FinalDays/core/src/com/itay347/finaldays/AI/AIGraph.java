package com.itay347.finaldays.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.Screens.PlayScreen;

/**
 * Contains the graph of the map
 */
public class AIGraph implements IndexedGraph<AINode> {
    /**
     * 2D array of the nodes
     */
    private AINode[][] nodes;
    /**
     * The array's width
     */
    private int width;
    /**
     * The array's height
     */
    private int height;

    /**
     * Initialize the graph for the pathfinder
     */
    public AIGraph() {
        // get the walls info from the PlayScreen and save the width and height
        boolean[][] walls = PlayScreen.getWalls();
        width = walls.length;
        height = walls[0].length;
        this.nodes = new AINode[width][height];

        // Create the nodes
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.nodes[x][y] = new AINode(x, y);
            }
        }

        // Directions array/matrix for the connections
        int[][] directions = new int[][]{
                new int[]{0, 1},  // up
                new int[]{0, -1}, // down
                new int[]{-1, 0}, // left
                new int[]{1, 0}   // right
        };

        // For each node, connect to his neighbors
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                AINode node = getNodeAt(x, y);
                // If the node is a wall, continue to the next node
                if (node.isWall()) {
                    continue;
                }
                // For each direction, check for a valid neighbor node
                for (int dir = 0; dir < directions.length; dir++) {
                    AINode neighbor = getNodeAt(x + directions[dir][0], y + directions[dir][1]);
                    // If there is a non-wall neighbor in that direction, connect them
                    if (neighbor != null && !neighbor.isWall()) {
                        node.getConnections().add(new DefaultConnection<AINode>(node, neighbor));
                    }
                }
                node.getConnections().shuffle();
            }
        }
    }

    /**
     * @param tileX the x index of the tile
     * @param tileY the y index of the tile
     * @return the node representing the tile or null if out of bounds
     */
    public AINode getNodeAt(int tileX, int tileY) {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return null;
        }
        return this.nodes[tileX][tileY];
    }

    /**
     * Returns the unique index of the given node.
     *
     * @param node the node whose index will be returned
     * @return the unique index of the given node.
     */
    @Override
    public int getIndex(AINode node) {
        return node.getIndex();
    }

    /**
     * Returns the number of nodes in this graph.
     */
    @Override
    public int getNodeCount() {
        return width * height;
    }

    /**
     * Returns the connections outgoing from the given node.
     *
     * @param fromNode the node whose outgoing connections will be returned
     * @return the array of connections outgoing from the given node.
     */
    @Override
    public Array<Connection<AINode>> getConnections(AINode fromNode) {
        return fromNode.getConnections();
    }
}
