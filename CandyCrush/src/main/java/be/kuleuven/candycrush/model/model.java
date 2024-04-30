package be.kuleuven.candycrush.model;
import be.kuleuven.candycrush.recordsAndGenerics.board;
import be.kuleuven.candycrush.recordsAndGenerics.Boardsize;
import be.kuleuven.candycrush.recordsAndGenerics.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class model {
    private String Speler;
    private board<Candy> speelbord;
    private int Score;
    private Boardsize board;



    public sealed interface Candy permits NormalCandy,gummyBeertje,jollyRanger,dropVeter,Pèche{}
    public record NormalCandy(int color) implements Candy{
        public NormalCandy{
            if (color < 0 || color > 3) throw new IllegalArgumentException("color must be 0,1,2 or 3");
        }
    }
    public record gummyBeertje() implements Candy{}
    public record jollyRanger() implements Candy{}
    public record dropVeter() implements Candy{}
    public record Pèche() implements Candy{}

    public model(String speler){
        this.Speler = speler;
        board = new Boardsize(10,10);
        speelbord = new board<>(board);
        Score = 0;
        genSpeelbord();
    }

    public void genSpeelbord() {
        speelbord = new board<>(board);
        for (Position pos : board.positions()) {
            int randomGetal = (int) (1+Math.random()*12);
            Candy candy;
            switch(randomGetal) {
                case 1,2,3,4,5,6,7,8:
                    candy = new NormalCandy((int) (Math.random()*4));
                    break;
                case 9:
                    candy = new gummyBeertje();
                    break;
                case 10:
                    candy = new jollyRanger();
                    break;
                case 11:
                    candy = new dropVeter();
                    break;
                case 12:
                    candy = new Pèche();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + randomGetal);
            }
            speelbord.replaceCellAt(pos, candy);
        }
    }
    public void veranderCandy(int index){
        for (Position pos : board.positions()) {
            int randomGetal = (int) (1 + Math.random() * 12);
            Candy candy;
            switch (randomGetal) {
                case 1, 2, 3, 4, 5, 6, 7, 8:
                    candy = new NormalCandy((int) (Math.random() * 4));
                    break;
                case 9:
                    candy = new gummyBeertje();
                    break;
                case 10:
                    candy = new jollyRanger();
                    break;
                case 11:
                    candy = new dropVeter();
                    break;
                case 12:
                    candy = new Pèche();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + randomGetal);
            }
            speelbord.replaceCellAt(Position.fromIndex(index, board), candy);
        }
    }
    public Iterable<Position> getSameNeighborsPositions(int index) {
        // Check if the index is within the board size
        if (index < 0 || index >= board.width() * board.height()) {
            throw new IllegalArgumentException("Index must be within the board size");
        }

        Candy targetCandy = speelbord.getCellAt(Position.fromIndex(index, board));
        List<Position> sameNeighbors = new ArrayList<>();

        // Get the current position
        Position currentPosition = Position.fromIndex(index, board);

        // Check neighbor positions
        for (Position neighbor : currentPosition.neighborPositions()) {
            int neighborIndex = neighbor.toIndex();
            if (speelbord.getCellAt(Position.fromIndex(neighborIndex, board)).equals(targetCandy)) {
                sameNeighbors.add(neighbor);
            }
        }
        // Add the current position to the list
        sameNeighbors.add(currentPosition);

        return sameNeighbors;
    }
    public boolean firstTwoHaveSameCandy(Candy candy, Stream<Position> positions) {
        List<Position> positionList = positions.limit(2).collect(Collectors.toList());
        return positionList.size() == 2 && positionList.stream().allMatch(pos -> speelbord.getCellAt(pos).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions() {
        return board.positions().stream().filter(pos -> pos.kolomNummer() > 0 && !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkLeft()));
    }

    public Stream<Position> verticalStartingPositions() {
        return board.positions().stream().filter(pos -> pos.rijNummer() > 0 && !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkUp()));
    }

    public List<Position> longestMatchToRight(Position pos) {
        List<Position> match = new ArrayList<>();
        Candy candy = speelbord.getCellAt(pos);
        pos.walkRight().takeWhile(currentPos -> speelbord.getCellAt(currentPos).equals(candy)).forEach(match::add);
        return match;
    }

    public List<Position> longestMatchDown(Position pos) {
        List<Position> match = new ArrayList<>();
        Candy candy = speelbord.getCellAt(pos);
        pos.walkDown().takeWhile(currentPos -> speelbord.getCellAt(currentPos).equals(candy)).forEach(match::add);
        return match;
    }

    public Set<List<Position>> findAllMatches() {
        Set<List<Position>> matches = new HashSet<>();
        Stream.concat(horizontalStartingPositions(), verticalStartingPositions()).forEach(pos -> {
            List<Position> horizontalMatch = longestMatchToRight(pos);
            if (horizontalMatch.size() >= 3) {
                matches.add(horizontalMatch);
            }
            List<Position> verticalMatch = longestMatchDown(pos);
            if (verticalMatch.size() >= 3) {
                matches.add(verticalMatch);
            }
        });
        return matches;
    }
    public void clearMatch(List<Position> match) {
        if (match.isEmpty()) {
            return;
        }

        Position pos = match.get(0);
        speelbord.replaceCellAt(pos, null);
        match.remove(0);

        clearMatch(match);
    }
    public void fallDownTo(Position pos) {
        if (speelbord.getCellAt(pos) != null) {
            return;
        }

        Position abovePos = new Position(pos.rijNummer() - 1, pos.kolomNummer(), pos.boardSize());

        if (abovePos.rijNummer() >= 0 && speelbord.getCellAt(abovePos) != null) {
            speelbord.replaceCellAt(pos, speelbord.getCellAt(abovePos));
            speelbord.replaceCellAt(abovePos, null);
        }

        if (abovePos.rijNummer() > 0) {
            fallDownTo(abovePos);
        }
    }
    public boolean updateBoard() {
        Set<List<Position>> matches = findAllMatches();

        if (matches.isEmpty()) {
            return false;
        }

        for (List<Position> match : matches) {
            clearMatch(new ArrayList<>(match));
            for (Position pos : match) {
                fallDownTo(pos);
            }
        }

        updateBoard();

        return true;
    }

    public String getSpeler() {
        return Speler;
    }
    public board<Candy> getSpeelbord() { return speelbord; }
    public int getScore() {
        return Score;
    }
    public void setScore(int score) {
        Score = score;
    }
    public Boardsize getBoard() {
        return board;
    }
    public void reset() {
        speelbord = new board<>(board);
        setScore(0);
        genSpeelbord();
    }
}
