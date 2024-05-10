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
        updateBoard();
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

    public boolean firstTwoHaveSameCandy(Candy candy, Stream<Position> positions) {
        List<Position> positionList = positions.limit(2).toList();
        if(positionList.size() < 2) {
            return false;
        }
        return positionList.stream()
                .allMatch(pos -> speelbord.getCellAt(pos) != null && speelbord.getCellAt(pos).equals(candy));
    }

    public Stream<Position> horizontalStartingPositions() {
        return boardsize.positions().stream().filter(pos -> !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkLeft()));
    }

    public Stream<Position> verticalStartingPositions() {
        return boardsize.positions().stream().filter(pos -> !firstTwoHaveSameCandy(speelbord.getCellAt(pos), pos.walkUp()));
    }

    public List<Position> longestMatchToRight(Position pos) {
        Candy candy = speelbord.getCellAt(pos);
        return pos.walkRight().takeWhile(posi-> speelbord.getCellAt(posi) != null && speelbord.getCellAt(posi).equals(candy)).toList();
    }

    public List<Position> longestMatchDown(Position pos) {
        Candy candy = speelbord.getCellAt(pos);
        return pos.walkDown().takeWhile(posi-> speelbord.getCellAt(posi) != null && speelbord.getCellAt(posi).equals(candy)).toList();
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
        return matches.stream().filter(match -> matches.stream()
                        .noneMatch(longerMatch -> longerMatch.size() > match.size() && longerMatch.containsAll(match)))
                .collect(Collectors.toSet());
    }
    public void clearMatch(List<Position> match){
        List<Position> matches = new ArrayList<>(match);
        if(matches.isEmpty()){
            return;
        }
        Position pos = matches.get(0);
        speelbord.replaceCellAt(pos, null);
        fallDownTo(pos);
        matches.remove(0);
        clearMatch(matches);
    }

    public void fallDownTo(Position pos){
        if(pos.rijNummer() == 0){
            return;
        }
        Position above = new Position(pos.rijNummer() - 1, pos.kolomNummer(), pos.boardSize());
        if(speelbord.getCellAt(above) == null){
            fallDownTo(above);
        }
        if(speelbord.getCellAt(above) != null){
            speelbord.replaceCellAt(pos, speelbord.getCellAt(above));
            speelbord.replaceCellAt(above, null);
            fallDownTo(above);
        }else{
            fallDownTo(above);
        }
    }

    public boolean updateBoard(){
        Set<List<Position>> matches = findAllMatches();
        if(matches.isEmpty()){
            return false;
        }
        List<Position> match = matches.iterator().next();
        clearMatch(match);
        updateBoard();
        setScore(getScore() + match.size());
        return true;
    }

    public void swapPositions(Position pos1, Position pos2) {
        if (speelbord.getCellAt(pos1) == null || speelbord.getCellAt(pos2) == null) {
            return;
        }
        Candy temp = speelbord.getCellAt(pos1);
        speelbord.replaceCellAt(pos1, speelbord.getCellAt(pos2));
        speelbord.replaceCellAt(pos2, temp);
    }

    public List<Position[]> maximizeScore() {
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
    }
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
    }

    public String getSpeler() {
        return Speler;
    }
    public board<Candy> getSpeelbord() {
        return speelbord;
    }
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
        updateBoard();
    }
}
