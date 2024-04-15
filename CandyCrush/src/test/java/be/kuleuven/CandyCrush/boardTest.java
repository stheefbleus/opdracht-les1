package be.kuleuven.CandyCrush;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import be.kuleuven.candycrush.board;
import be.kuleuven.candycrush.model.model;

public class boardTest {
    @Test
    public void testFill() {
        board<Integer> testBoard = new board<>(new model.boardSize(10,10));
        testBoard.fill(position -> 1);

        for (int i = 0; i < testBoard.getSize().height(); i++) {
            for (int j = 0; j < testBoard.getSize().width(); j++) {
                assertEquals(Integer.valueOf(1), testBoard.getCellAt(new model.position(i, j, testBoard.getSize())));
            }
        }
    }

    @Test
    public void testCopyTo() {
        board<Integer> originalBoard = new board<>(new model.boardSize(10,10));
        originalBoard.fill(position -> 1);

        board<Integer> copiedBoard = new board<>(new model.boardSize(10,10));
        originalBoard.copyTo(copiedBoard);

        for (int i = 0; i < copiedBoard.getSize().height(); i++) {
            for (int j = 0; j < copiedBoard.getSize().width(); j++) {
                assertEquals(Integer.valueOf(1), copiedBoard.getCellAt(new model.position(i, j, copiedBoard.getSize())));
            }
        }
    }
}