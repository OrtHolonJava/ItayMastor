package com.itay347.finaldays.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.Screens.PlayScreen;

public class AIGraph implements IndexedGraph<AINode> {
    private AINode[][] nodes;
    private int width;
    private int height;

    /**
     * Initialize the graph for the pathfinder
     */
    public AIGraph() {
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
                // TODO: maybe this shuffle line is necessary
//                node.getConnections().shuffle();
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

    @Override
    public int getIndex(AINode node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return width * height;
    }

    @Override
    public Array<Connection<AINode>> getConnections(AINode fromNode) {
        return fromNode.getConnections();
    }
}
