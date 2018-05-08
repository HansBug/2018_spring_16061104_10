package models.map;

public class BlockPathResult {
    /**
     * 单源最短路出发点
     */
    private final Node source;
    
    /**
     *
     * @param source
     */
    BlockPathResult(Node source) {
        this.source = source;
    }
    
    public Node getSource() {
        return this.source;
    }
    
}
