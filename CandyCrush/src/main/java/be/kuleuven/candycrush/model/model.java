package be.kuleuven.candycrush.model;

import java.util.ArrayList;

import be.kuleuven.CheckNeighboursInGrid;
public class model {
    private String Speler;
    private ArrayList<Integer> speelbord;
    private int width;
    private int height;
    private int Score;

    public record boardSize(int width,int height){
        public boardSize {
            if (width <= 0) throw new IllegalArgumentException("width must not be 0");
            if (height <= 0) throw new IllegalArgumentException("height must not be 0");
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
    }

    public model(String speler){
        this.Speler = speler;
        speelbord = new ArrayList<>();
        width = 10;
        height = 10;
        Score = 0;
        genSpeelbord();
    }



    public void genSpeelbord() {
        for (int i= 0;i < width*height;i++){
            int randomGetal = (int) (1+Math.random()*5);
            speelbord.add(randomGetal);
        }
    }
    public ArrayList<Integer> checkBuren(int index){
        ArrayList<Integer> buren = (ArrayList<Integer>) CheckNeighboursInGrid.getSameNeighboursIds(speelbord,width,height,index);
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

    public int getWidth() {
        return width;
    }

    public int getSpeelbordValueOfIndex(int index){
       return speelbord.get(index);
    }

    public int getHeight() {
        return height;
    }
    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void reset() {
        speelbord.clear();
        setScore(0);
        genSpeelbord();
    }
}
