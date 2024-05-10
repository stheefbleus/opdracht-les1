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
    public boolean isLastColumn(){
        return kolomNummer == boardSize.width()-1;
    }

    public Stream<Position> walkLeft() {
        return Stream.iterate(this, pos -> pos.kolomNummer > 0, pos -> new Position(pos.rijNummer, pos.kolomNummer - 1, pos.boardSize));
    }

    public Stream<Position> walkRight() {
        return Stream.iterate(this, pos -> pos.kolomNummer < pos.boardSize.width() -1, pos -> new Position(pos.rijNummer, pos.kolomNummer + 1, pos.boardSize));
    }

    public Stream<Position> walkUp() {
        return Stream.iterate(this, pos -> pos.rijNummer > 0, pos -> new Position(pos.rijNummer - 1, pos.kolomNummer , pos.boardSize));
    }

    public Stream<Position> walkDown() {
        return Stream.iterate(this, pos -> pos.rijNummer < pos.boardSize.height() - 1, pos -> new Position(pos.rijNummer + 1, pos.kolomNummer , pos.boardSize));
    }

    }
