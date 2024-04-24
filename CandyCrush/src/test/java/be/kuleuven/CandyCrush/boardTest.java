package be.kuleuven.CandyCrush;

import be.kuleuven.candycrush.recordsAndGenerics.Boardsize;
import be.kuleuven.candycrush.recordsAndGenerics.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import be.kuleuven.candycrush.recordsAndGenerics.board;

public class boardTest {
    @Test
    public void testFill() {
        board<Integer> testBoard = new board<>(new Boardsize(10,10));
        testBoard.fill(position -> 1);

        for (int i = 0; i < testBoard.getSize().height(); i++) {
            for (int j = 0; j < testBoard.getSize().width(); j++) {
                assertEquals(Integer.valueOf(1), testBoard.getCellAt(new Position(i, j, testBoard.getSize())));
            }
        }
    }

    @Test
    public void testCopyTo() {
        board<Integer> originalBoard = new board<>(new Boardsize(10,10));
        originalBoard.fill(position -> 1);

        board<Integer> copiedBoard = new board<>(new Boardsize(10,10));
        originalBoard.copyTo(copiedBoard);

        for (int i = 0; i < copiedBoard.getSize().height(); i++) {
            for (int j = 0; j < copiedBoard.getSize().width(); j++) {
                assertEquals(Integer.valueOf(1), copiedBoard.getCellAt(new Position(i, j, copiedBoard.getSize())));
            }
        }
    }
}