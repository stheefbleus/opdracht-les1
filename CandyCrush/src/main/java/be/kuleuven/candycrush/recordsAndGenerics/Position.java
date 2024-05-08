package be.kuleuven.candycrush.recordsAndGenerics;

import java.util.ArrayList;
import java.util.stream.Stream;

public record Position(int rijNummer, int kolomNummer, Boardsize boardSize){
    public Position {
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
    public static Position fromIndex(int index, Boardsize size){
        if( index > size.height() * size.width()){
            throw new IllegalArgumentException("index bestaat niet");
        }
        else{
            int rij = index/ size.width();
            int kolom = index % size.width();
            return new Position(rij,kolom,size);
        }
    }
    public Iterable<Position> neighborPositions(){
        ArrayList<Position> neighbors = new ArrayList<>();
        int maxRij = boardSize.height() - 1;
        int maxKolom = boardSize.width() - 1;

        // Controleer noordelijke buur
        if (rijNummer > 0)
            neighbors.add(new Position(rijNummer - 1, kolomNummer, boardSize));

        // Controleer zuidelijke buur
        if (rijNummer < maxRij)
            neighbors.add(new Position(rijNummer + 1, kolomNummer, boardSize));

        // Controleer westelijke buur
        if (kolomNummer > 0)
            neighbors.add(new Position(rijNummer, kolomNummer - 1, boardSize));

        // Controleer oostelijke buur
        if (kolomNummer < maxKolom)
            neighbors.add(new Position(rijNummer, kolomNummer + 1, boardSize));

        // Controleer noordwestelijke buur
        if (rijNummer > 0 && kolomNummer > 0)
            neighbors.add(new Position(rijNummer - 1, kolomNummer - 1, boardSize));

        // Controleer noordoostelijke buur
        if (rijNummer > 0 && kolomNummer < maxKolom)
            neighbors.add(new Position(rijNummer - 1, kolomNummer + 1, boardSize));

        // Controleer zuidwestelijke buur
        if (rijNummer < maxRij && kolomNummer > 0)
            neighbors.add(new Position(rijNummer + 1, kolomNummer - 1, boardSize));

        // Controleer zuidoostelijke buur
        if (rijNummer < maxRij && kolomNummer < maxKolom)
            neighbors.add(new Position(rijNummer + 1, kolomNummer + 1, boardSize));

        return neighbors;
    }
    public boolean isLastColumn(){
        return kolomNummer == boardSize.width()-1;
    }

    public Stream<Position> walkLeft() {
        return Stream.iterate(this, pos -> pos.kolomNummer > 0, pos -> new Position(pos.rijNummer, pos.kolomNummer - 1, pos.boardSize));
    }

    public Stream<Position> walkRight() {
        return Stream.iterate(this, pos -> pos.kolomNummer < pos.boardSize.width() - 1, pos -> new Position(pos.rijNummer, pos.kolomNummer + 1, pos.boardSize));
    }

    public Stream<Position> walkUp() {
        return Stream.iterate(this, pos -> pos.rijNummer > 0, pos -> new Position(pos.rijNummer - 1, pos.kolomNummer , pos.boardSize));
    }

    public Stream<Position> walkDown() {
        return Stream.iterate(this, pos -> pos.rijNummer < pos.boardSize.height() - 1, pos -> new Position(pos.rijNummer + 1, pos.kolomNummer , pos.boardSize));
    }

    }
