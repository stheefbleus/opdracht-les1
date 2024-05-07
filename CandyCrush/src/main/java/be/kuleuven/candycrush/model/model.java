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
    private Boardsize boardsize;



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
        boardsize = new Boardsize(10,10);
        speelbord = new board<>(boardsize);
        Score = 0;
        genSpeelbord();
    }

    public void genSpeelbord() {
        speelbord = new board<>(boardsize);
        for (Position pos : boardsize.positions()) {
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
    public void klik(int x, int y){
        Position pos = new Position(x, y, boardsize);
        Stream<Position> stream = pos.walkRight();
        Position nextPos = stream.findFirst().orElse(null);
        if (nextPos != null) {
            swapPositions(pos, nextPos);
            updateBoard();
        }
    }

    public boolean firstTwoHaveSameCandy(Candy candy, Stream<Position> positions) {
        List<Position> positionList = positions.limit(2).collect(Collectors.toList());
        return positionList.size() == 2 && positionList.stream().allMatch(pos -> speelbord.getCellAt(pos).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions() {
        return boardsize.positions().stream().filter(pos -> !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkLeft()));
    }

    public Stream<Position> verticalStartingPositions() {
        return boardsize.positions().stream().filter(pos -> !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkUp()));
    }

    public List<Position> longestMatchToRight(Position pos) {
        Candy candy = speelbord.getCellAt(pos);
        return pos.walkRight().takeWhile(posi-> speelbord.getCellAt(posi) != null && speelbord.getCellAt(pos) != null && speelbord.getCellAt(posi).equals(candy)).toList();
    }

    public List<Position> longestMatchDown(Position pos) {
        Candy candy = speelbord.getCellAt(pos);
        return pos.walkDown().takeWhile(posi-> speelbord.getCellAt(posi) != null && speelbord.getCellAt(pos) != null && speelbord.getCellAt(posi).equals(candy)).toList();
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
        if (!findAllMatches().isEmpty()) {
            updateBoard();
        }

        return true;
    }

    private void swapPositions(Position pos1, Position pos2) {
        if (speelbord.getCellAt(pos1) == null || speelbord.getCellAt(pos2) == null) {
            return;
        }
        Candy temp = speelbord.getCellAt(pos1);
        speelbord.replaceCellAt(pos1, speelbord.getCellAt(pos2));
        speelbord.replaceCellAt(pos2, temp);
    }

    /*public List<Position[]> maximizeScore() {
        List<Position[]> bestSequence = new ArrayList<>();
        List<Position[]> currentSequence = new ArrayList<>();

        for (Position[] swap : getAllValidSwaps()) {
            swapPositions(swap[0], swap[1]);
            updateBoard();
            Set<List<Position>> matches = findAllMatches();
            if (!matches.isEmpty()) {
                int score = matches.stream().mapToInt(List::size).sum();
                currentSequence.add(swap);
                if (score > getScoreFromSequence(bestSequence)) {
                    bestSequence.clear();
                    bestSequence.addAll(currentSequence);
                }
                currentSequence.remove(currentSequence.size() - 1);
            }
            swapPositions(swap[0], swap[1]);
        }

        return bestSequence;
    }

    private Position[][] getAllValidSwaps() {
        List<Position[]> swaps = new ArrayList<>();
        for (Position pos : boardsize.positions()) {
            for (Position neighbor : pos.neighborPositions()) {
                if (matchAfterSwitch(pos, neighbor)) {
                    swaps.add(new Position[]{pos, neighbor});
                }
            }
        }
        return swaps.toArray(new Position[0][0]);
    }

    private boolean matchAfterSwitch(Position pos1, Position pos2) {
        swapPositions(pos1, pos2);
        boolean matchExists = !findAllMatches().isEmpty();
        swapPositions(pos1, pos2); // undo the swap
        return matchExists;
    }*/


/*
    private int getScoreFromSequence(List<Position[]> sequence) {
        int originalScore = getScore();
        for (Position[] swap : sequence) {
            swapPositions(swap[0], swap[1]);
            updateBoard();
        }
        int score = getScore();
        for (int i = sequence.size() - 1; i >= 0; i--) {
            swapPositions(sequence.get(i)[0], sequence.get(i)[1]);

        }
        setScore(originalScore);
        return score;
    }*/

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
    public Boardsize getBoardsize() {
        return boardsize;
    }
    public void reset() {
        speelbord = new board<>(boardsize);
        setScore(0);
        genSpeelbord();
    }
}
