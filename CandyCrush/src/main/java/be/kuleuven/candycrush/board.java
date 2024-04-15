package be.kuleuven.candycrush;
import be.kuleuven.candycrush.model.model;

import java.util.*;
import java.util.function.Function;

public class board<T> {
    private Map<model.position, T> boardMap;
    private Map<T, Set<model.position>> reverseBoardMap;
    private model.boardSize size;
    public board(model.boardSize size) {
        this.size = new model.boardSize(10,10);
        this.boardMap = new HashMap<>();
        this.reverseBoardMap = new HashMap<>();
    }
    public T getCellAt(model.position position) {
        return this.boardMap.get(position);
    }
    public void replaceCellAt(model.position position, T newCell) {
        T oldCell = this.boardMap.get(position);
        if (oldCell != null) {
            this.reverseBoardMap.get(oldCell).remove(position);
        }
        this.boardMap.put(position, newCell);
        this.reverseBoardMap.computeIfAbsent(newCell, k -> new HashSet<>()).add(position);
    }

    public model.boardSize getSize() {
        return this.size;
    }

    public Set<model.position> getPositionsOfElement(T cell) {
        return Collections.unmodifiableSet(this.reverseBoardMap.getOrDefault(cell, new HashSet<>()));
    }

    public void fill(Function<model.position, T> cellCreator) {
        for (int i = 0; i < size.height(); i++) {
            for (int j = 0; j < size.width(); j++) {
                model.position position = new model.position(i,j,size);
                T cell = cellCreator.apply(position);
                replaceCellAt(position, cell);
            }
        }
    }
    public void copyTo(board<T> otherBoard) {
        if (otherBoard.getSize().height() != this.size.height() || otherBoard.getSize().width() != this.size.width()) {
            throw new IllegalArgumentException("The other board must have the same dimensions.");
        }

        for (int i = 0; i < size.height(); i++) {
            for (int j = 0; j < size.width(); j++) {
                model.position position = new model.position(i,j,size);
                T cell = getCellAt(position);
                otherBoard.replaceCellAt(position, cell);
            }
        }
    }
}