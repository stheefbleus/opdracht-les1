package be.kuleuven.candycrush.recordsAndGenerics;

import java.util.ArrayList;
import java.util.Collection;

public record Boardsize(int width, int height){
    public Boardsize {
        if (width <= 0) throw new IllegalArgumentException("width must not be 0");
        if (height <= 0) throw new IllegalArgumentException("height must not be 0");
    }
    public Collection<Position> positions(){
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < width*height; i++){
            positions.add(Position.fromIndex(i,this));
        }
        return positions;
    }
}
