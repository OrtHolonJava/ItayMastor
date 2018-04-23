package com.itay347.finaldays.AI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.itay347.finaldays.MyValues;
import com.itay347.finaldays.Screens.PlayScreen;

/**
 * A node used in the AIGraph
 */
public class AINode {
    /**
     * The node's tile x index
     */
    public final int tileX;
    /**
     * The node's tile y index
     */
    public final int tileY;
    /**
     * The index in the graph
     */
    private final int index;
    /**
     * Array of connection (neighbors)
     */
    private final Array<Connection<AINode>> connections;

    /**
     * Init a node
     *
     * @param tileX tile x index
     * @param tileY tile y index
     */
    public AINode(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.index = tileX + tileY * PlayScreen.getWalls().length;
        this.connections = new Array<Connection<AINode>>();
    }

    /**
     * @return whether or not the node is on a wall tile
     */
    public boolean isWall() {
        return PlayScreen.getWalls()[tileX][tileY];
    }

    /**
     * @return the index of the node
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return a vector2 of the location of the node (in pixels)
     */
    public Vector2 getCenterPoint() {
        return new Vector2(MyValues.tileToPos(tileX), MyValues.tileToPos(tileY));
    }

    /**
     * @return the connections array
     */
    public Array<Connection<AINode>> getConnections() {
        return connections;
    }
}
