package be.kuleuven.candycrush.recordsAndGenerics;

import java.util.*;
import java.util.function.Function;

public class board<T> {
    private Map<Position, T> boardMap;
    private Map<T, Set<Position>> reverseBoardMap;
    private Boardsize size;
    public board(Boardsize size) {
        this.size = new Boardsize(10,10);
        this.boardMap = new HashMap<>();
        this.reverseBoardMap = new HashMap<>();
    }
    public synchronized T getCellAt(Position position) {
        return this.boardMap.get(position);
    }
    public synchronized void replaceCellAt(Position position, T newCell) {
        T oldCell = this.boardMap.get(position);
        if (oldCell != null) {
            this.reverseBoardMap.get(oldCell).remove(position);
        }
        this.boardMap.put(position, newCell);
        this.reverseBoardMap.computeIfAbsent(newCell, k -> new HashSet<>()).add(position);
    }

    public Boardsize getSize() {
        return this.size;
    }

    public synchronized Set<Position> getPositionsOfElement(T cell) {
        return Collections.unmodifiableSet(this.reverseBoardMap.getOrDefault(cell, new HashSet<>()));
    }

    public synchronized void fill(Function<Position, T> cellCreator) {
        for (int i = 0; i < size.height(); i++) {
            for (int j = 0; j < size.width(); j++) {
                Position position = new Position(i,j,size);
                T cell = cellCreator.apply(position);
                replaceCellAt(position, cell);
            }
        }
    }
    public synchronized void copyTo(board<T> otherBoard) {
        if (otherBoard.getSize().height() != this.size.height() || otherBoard.getSize().width() != this.size.width()) {
            throw new IllegalArgumentException("The other board must have the same dimensions.");
        }

        for (int i = 0; i < size.height(); i++) {
            for (int j = 0; j < size.width(); j++) {
                Position position = new Position(i,j,size);
                T cell = getCellAt(position);
                otherBoard.replaceCellAt(position, cell);
            }
        }
    }
}