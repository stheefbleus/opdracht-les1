package be.kuleuven.candycrush.model;

import java.util.ArrayList;

import be.kuleuven.CheckNeighboursInGrid;
public class model {
    private String Speler;
    private ArrayList<Integer> speelbord;
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
            if (rijNummer <=0 || rijNummer >= boardSize.height()){
                throw new IllegalArgumentException("rij moet in de board-hoogte zijn");
            }
            if (kolomNummer <=0 || kolomNummer >= boardSize.width()){
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
            if (rijNummer > 0) neighbors.add(new position(rijNummer-1,kolomNummer,boardSize));
            if (rijNummer < boardSize.height()-1) neighbors.add(new position(rijNummer+1,kolomNummer,boardSize));
            if (kolomNummer > 0) neighbors.add(new position(rijNummer,kolomNummer-1,boardSize));
            if (kolomNummer < boardSize.width()-1) neighbors.add(new position(rijNummer,kolomNummer+1,boardSize));
            return neighbors;
        }
        boolean isLastColumn(){
            return kolomNummer == boardSize.width()-1;
        }

    }

    public model(String speler){
        this.Speler = speler;
        speelbord = new ArrayList<>();
        board = new boardSize(10,10);
        Score = 0;
        genSpeelbord();
    }



    public void genSpeelbord() {
        for (int i= 0;i < board.width()*board.height();i++){
            int randomGetal = (int) (1+Math.random()*5);
            speelbord.add(randomGetal);
        }
    }
    public ArrayList<Integer> checkBuren(int index){
        ArrayList<Integer> buren = (ArrayList<Integer>) CheckNeighboursInGrid.getSameNeighboursIds(speelbord,board.width(),board.height(),index);
        return buren;
    }

    public void veranderCandy(int index){
        speelbord.set(index,(int) (Math.random()*6));
    }

    public String getSpeler() {
        return Speler;
    }

    public ArrayList<Integer> getSpeelbord() {
        return speelbord;
    }

    public int getSpeelbordValueOfIndex(int index){
       return speelbord.get(index);
    }

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
        speelbord.clear();
        setScore(0);
        genSpeelbord();
    }
}
