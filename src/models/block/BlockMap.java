package models.block;

import exceptions.block.block_map.NoSuchEdgeException;
import interfaces.block.BlockMapInterface;
import models.application.ApplicationModel;

import java.util.HashMap;
import java.util.HashSet;

public abstract class BlockMap extends ApplicationModel implements BlockMapInterface {
    protected final HashMap<BlockPoint, HashSet<BlockPoint>> edge_map;
    
    public BlockMap() {
        this.edge_map = new HashMap<>();
    }
    
    public HashSet<BlockPoint> getTargets(BlockPoint point) {
        return this.edge_map.getOrDefault(point, new HashSet<>());
    }
    
    public boolean containsEdge(BlockEdge e) {
        return this.getTargets(e.getFirst()).contains(e.getSecond());
    }
    
    public int getEdgeWeight(BlockEdge e) throws NoSuchEdgeException {
        if (!this.containsEdge(e)) throw new NoSuchEdgeException(e);
        return 1;
    }
    
    protected void addSingleEdge(BlockPoint source, BlockPoint target) {
        HashSet<BlockPoint> set = this.edge_map.getOrDefault(source, new HashSet<>());
        set.add(target);
        this.edge_map.put(source, set);
    }
    
    protected void removeSingleEdge(BlockPoint source, BlockPoint target) {
    
    }
    
    public void addEdge(BlockEdge e) {
        this.addSingleEdge(e.getFirst(), e.getSecond());
        this.addSingleEdge(e.getSecond(), e.getFirst());
    }
    
    
    public void clear() {
        this.edge_map.clear();
    }
}
