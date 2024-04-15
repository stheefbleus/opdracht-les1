package be.kuleuven.candycrush;
import be.kuleuven.candycrush.model.model;

import java.util.ArrayList;
import java.util.function.Function;

public class board<T> {
    private ArrayList<ArrayList<T>> board;
    private model.boardSize size;
    public board(model.boardSize size) {
        this.size = new model.boardSize(10,10);
        this.board = new ArrayList<>();
        for (int i = 0; i < size.height(); i++) {
            ArrayList<T> row = new ArrayList<>();
            for (int j = 0; j < size.width(); j++) {
                row.add(null);
            }
            this.board.add(row);
        }
    }
    public T getCellAt(model.position position) {
        return this.board.get(position.rijNummer()).get(position.kolomNummer());
    }
    public void replaceCellAt(model.position position, T newCell) {
        this.board.get(position.rijNummer()).set(position.kolomNummer(), newCell);
    }

    public model.boardSize getSize() {
        return this.size;
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