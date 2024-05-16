package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.recordsAndGenerics.Position;
import be.kuleuven.candycrush.recordsAndGenerics.board;
import javafx.util.Pair;

import java.util.List;


public record Solution(int score, List<Pair<Position,Position>> currentMoves, board<model.Candy> board) {

    public boolean isBetterThan(Solution other) {
        if (this.score() > other.score()) {
            return true;
        } else if (this.score() == other.score()) {
            return this.currentMoves().size() < other.currentMoves().size();
        }
        return false;
    }

    public int getScore(){
        return score;
    }
    public board<model.Candy> getBoard(){
        return board;
    }
    public List<Pair<Position,Position>> getCurrentMoves(){
        return currentMoves;
    }
}