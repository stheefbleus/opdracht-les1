package be.kuleuven.candycrush.model;
import be.kuleuven.candycrush.recordsAndGenerics.board;
import be.kuleuven.candycrush.recordsAndGenerics.Boardsize;
import be.kuleuven.candycrush.recordsAndGenerics.Position;
import javafx.util.Pair;

import java.util.*;
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
        Score = 0;
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
        Set<List<Position>> horizontalMatches = new HashSet<>();
        Set<List<Position>> verticalMatches = new HashSet<>();
        Set<List<Position>> allMatches = new HashSet<>();

        horizontalStartingPositions().forEach(pos -> {
            List<Position> horizontalMatch = longestMatchToRight(pos);
            if (horizontalMatch.size() >= 3) {
                horizontalMatches.add(horizontalMatch);
            }
        });

        verticalStartingPositions().forEach(pos -> {
            List<Position> verticalMatch = longestMatchDown(pos);
            if (verticalMatch.size() >= 3) {
                verticalMatches.add(verticalMatch);
            }
        });

        for (List<Position> hMatch : horizontalMatches) {
            for (List<Position> vMatch : verticalMatches) {
                List<Position> intersection = hMatch.stream()
                        .filter(vMatch::contains)
                        .collect(Collectors.toList());
                if (!intersection.isEmpty()) {
                    allMatches.add(intersection);
                }
            }
        }

        allMatches.addAll(horizontalMatches);
        allMatches.addAll(verticalMatches);

        return allMatches.stream().filter(match -> allMatches.stream()
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

    public boolean matchAfterSwitch(Position pos1, Position pos2) {
        swapPositions(pos1, pos2);
        boolean matchExists = !findAllMatches().isEmpty();
        swapPositions(pos1, pos2);
        return matchExists;
    }

    public Solution maximizeScore() {
        Solution initialSolution = new Solution(0, new ArrayList<>(), speelbord);
        Solution bestSoFar = findBestSolution(initialSolution, null);
        return bestSoFar;
    }

    public Solution findBestSolution(Solution initialSolution,Solution bestSoFar){
        if(getAllValidSwaps(initialSolution.getBoard()).isEmpty()) {
            if (bestSoFar == null || initialSolution.isBetterThan(bestSoFar)) {
                return initialSolution;
            } else {
                return bestSoFar;
            }
        }
        else {
            ArrayList<Pair<Position,Position>> swaps = getAllValidSwaps(initialSolution.getBoard());
            for(Pair<Position,Position> swap : swaps) {
                board<Candy> newBoard = new board<>(initialSolution.getBoard().getSize());
                initialSolution.getBoard().copyTo(newBoard);
                swapPositions(swap.getKey(), swap.getValue());
                updateBoard();
                int score = (int) newBoard.getBoardCells().values().stream()
                        .filter(Objects::isNull).count();

                ArrayList<Pair<Position,Position>> newMoves = new ArrayList<>(initialSolution.currentMoves());
                newMoves.add(swap);

                Solution newSolution = new Solution(score, newMoves, newBoard);
                if (bestSoFar == null || newSolution.isBetterThan(bestSoFar)) {
                    bestSoFar = newSolution;
                }
            }
            return bestSoFar;
        }
    }

    public ArrayList<Pair<Position,Position>> getAllValidSwaps(board<Candy> board){
        Set<Pair<Position,Position>> swaps = new HashSet<>();
        for (Position p : board.getSize().positions()) {
            for (Position q : p.neighborPositions()) {
                if (matchAfterSwitch(p, q)) {
                    swaps.add(new Pair<>(p, q));
                }
            }
        }
        return new ArrayList<>(swaps);
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
        setScore(0);
    }
}
