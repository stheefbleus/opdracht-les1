package be.kuleuven.candycrush.model;

import java.util.ArrayList;

import be.kuleuven.CheckNeighboursInGrid;
public class Model {
    private String Speler;
    private ArrayList<Integer> speelbord;
    private int width;
    private int height;
    private int Score;

    public Model(String speler){
        this.Speler = speler;
        speelbord = new ArrayList<>();
        width = 10;
        height = 10;
        Score = 0;
        genSpeelbord();
    }


    public void genSpeelbord() {
        for (int i= 0;i < width*height;i++){
            int randomGetal = (int) (Math.random()*5);
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


    public static void main(String[] args) {
        Model m = new Model("stef");
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
