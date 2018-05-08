import models.block.BlockEdge;
import models.block.BlockPoint;

import java.util.HashMap;

public abstract class Main {
    public static void main(String[] args) {
        BlockPoint p1 = new BlockPoint(1, 2);
        BlockPoint p2 = new BlockPoint(2, 1);
    
        HashMap<BlockEdge, Integer> map = new HashMap<>();
        map.put(new BlockEdge(p1, p2), 1);
        System.out.println(map.get(new BlockEdge(p2, p1)));
    }
}
