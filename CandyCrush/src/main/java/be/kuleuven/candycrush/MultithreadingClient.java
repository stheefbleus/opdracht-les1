package be.kuleuven.candycrush;

import be.kuleuven.candycrush.model.model;
import be.kuleuven.candycrush.recordsAndGenerics.Boardsize;
import be.kuleuven.candycrush.recordsAndGenerics.Position;
import be.kuleuven.candycrush.recordsAndGenerics.board;

public class MultithreadingClient {
    private static volatile boolean running = true;

    public static void main(String[] args) {
        board<model.Candy> gameBoard = new board<>(new Boardsize(10,10));
        Runnable task = () -> {
            while (running) {
                Position randomPosition = new Position((int) (Math.random() * 10), (int) (Math.random() * 10), gameBoard.getSize());
                int randomCandy = (int) (Math.random() * 12) + 1;
                model.Candy candy;
                switch(randomCandy) {
                    case 1,2,3,4,5,6,7,8:
                        candy = new model.NormalCandy((int) (Math.random() * 4));
                        break;
                    case 9:
                        candy = new model.gummyBeertje();
                        break;
                    case 10:
                        candy = new model.jollyRanger();
                        break;
                    case 11:
                        candy = new model.dropVeter();
                        break;
                    case 12:
                        candy = new model.Pèche();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + randomCandy);
                }
                gameBoard.replaceCellAt(randomPosition, candy);
            }
        };
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        running = false;
    }
}