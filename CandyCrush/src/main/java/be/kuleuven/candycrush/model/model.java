package be.kuleuven.candycrush.model;
import be.kuleuven.candycrush.board;

import java.util.ArrayList;
import java.util.List;

public class model {
    private String Speler;
    private board<Candy> speelbord;
    private int Score;
    private boardSize board;

    public record boardSize(int width,int height){
        public boardSize {
            if (width <= 0) throw new IllegalArgumentException("width must not be 0");
            if (height <= 0) throw new IllegalArgumentException("height must not be 0");
        }
        Iterable<position> positions(){
            ArrayList<position> positions = new ArrayList<>();
            for (int i = 0; i < width*height; i++){
                positions.add(position.fromIndex(i,this));
            }
            return positions;
        }
    }
    public record position(int rijNummer, int kolomNummer, boardSize boardSize){
        public position {
            if (rijNummer <0 || rijNummer >= boardSize.height()){
                throw new IllegalArgumentException("rij moet in de board-hoogte zijn");
            }
            if (kolomNummer <0 || kolomNummer >= boardSize.width()){
                throw new IllegalArgumentException("rij moet in de board-breedte zijn");
            }
        }
        public int toIndex(){
            return rijNummer * boardSize.width() + kolomNummer;
        }
        static position fromIndex(int index, boardSize size){
            if( index > size.height() * size.width()){
                throw new IllegalArgumentException("index bestaat niet");
            }
            else{
                int rij = index/ size.width();
                int kolom = index % size.width();
                return new position(rij,kolom,size);
            }
        }
        Iterable<position> neighborPositions(){
            ArrayList<position> neighbors = new ArrayList<>();
            int maxRij = boardSize.height() - 1;
            int maxKolom = boardSize.width() - 1;

            // Controleer noordelijke buur
            if (rijNummer > 0)
                neighbors.add(new position(rijNummer - 1, kolomNummer, boardSize));

            // Controleer zuidelijke buur
            if (rijNummer < maxRij)
                neighbors.add(new position(rijNummer + 1, kolomNummer, boardSize));

            // Controleer westelijke buur
            if (kolomNummer > 0)
                neighbors.add(new position(rijNummer, kolomNummer - 1, boardSize));

            // Controleer oostelijke buur
            if (kolomNummer < maxKolom)
                neighbors.add(new position(rijNummer, kolomNummer + 1, boardSize));

            // Controleer noordwestelijke buur
            if (rijNummer > 0 && kolomNummer > 0)
                neighbors.add(new position(rijNummer - 1, kolomNummer - 1, boardSize));

            // Controleer noordoostelijke buur
            if (rijNummer > 0 && kolomNummer < maxKolom)
                neighbors.add(new position(rijNummer - 1, kolomNummer + 1, boardSize));

            // Controleer zuidwestelijke buur
            if (rijNummer < maxRij && kolomNummer > 0)
                neighbors.add(new position(rijNummer + 1, kolomNummer - 1, boardSize));

            // Controleer zuidoostelijke buur
            if (rijNummer < maxRij && kolomNummer < maxKolom)
                neighbors.add(new position(rijNummer + 1, kolomNummer + 1, boardSize));

            return neighbors;
        }
        boolean isLastColumn(){
            return kolomNummer == boardSize.width()-1;
        }

    }
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
        board = new boardSize(10,10);
        speelbord = new board<>(board);
        Score = 0;
        genSpeelbord();
    }

    public void genSpeelbord() {
        speelbord = new board<>(board);
        for (position pos : board.positions()) {
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
            speelbord.setCell(pos, candy);
        }
    }
    public void veranderCandy(int index){
        for (position pos : board.positions()) {
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
            speelbord.setCell(position.fromIndex(index, board), candy);
        }
    }
    public Iterable<position> getSameNeighborsPositions(int index) {
        // Check if the index is within the board size
        if (index < 0 || index >= board.width() * board.height()) {
            throw new IllegalArgumentException("Index must be within the board size");
        }

        Candy targetCandy = speelbord.getCellAt(position.fromIndex(index, board));
        List<position> sameNeighbors = new ArrayList<>();

        // Get the current position
        position currentPosition = position.fromIndex(index, board);

        // Check neighbor positions
        for (position neighbor : currentPosition.neighborPositions()) {
            int neighborIndex = neighbor.toIndex();
            if (speelbord.getCellAt(position.fromIndex(neighborIndex, board)).equals(targetCandy)) {
                sameNeighbors.add(neighbor);
            }
        }
        // Add the current position to the list
        sameNeighbors.add(currentPosition);

        return sameNeighbors;
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
    public boardSize getBoard() {
        return board;
    }
    public void reset() {
        speelbord = new board<>(board);
        setScore(0);
        genSpeelbord();
    }
}
