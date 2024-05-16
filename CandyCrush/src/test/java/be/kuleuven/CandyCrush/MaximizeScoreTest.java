package be.kuleuven.CandyCrush;

import be.kuleuven.candycrush.model.Solution;
import be.kuleuven.candycrush.model.model;
import be.kuleuven.candycrush.recordsAndGenerics.Boardsize;
import be.kuleuven.candycrush.recordsAndGenerics.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MaximizeScoreTest {

    public static model createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        Boardsize size = new Boardsize(lines.size(), lines.getFirst().length());
        var model = new model("Default Player");
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.getSpeelbord().replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }

    private static model.Candy characterToCandy(char c) {
        return switch(c) {
            case '.' -> null;
            case 'o' -> new model.NormalCandy(0);
            case '*' -> new model.NormalCandy(1);
            case '#' -> new model.NormalCandy(2);
            case '@' -> new model.NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }
    @Test
    public void testMaximizeScore1() {
        model model1 = createBoardFromString("""
                                                       @@o#
                                                       o*#o
                                                       @@**
                                                       *#@@""");
        Solution s = model1.maximizeScore();
        assertEquals(16, s.getScore());
        assertEquals(4, s.currentMoves().size());
    }
    @Test
    public void testMaximizeScore2() {
        model model2 = createBoardFromString("""
                                                       #oo##
                                                       #@o@@
                                                       *##o@
                                                       @@*@o
                                                       **#*o""");
        Solution s = model2.maximizeScore();
        assertEquals(23, s.getScore());
        assertEquals(7, s.currentMoves().size());
    }

    @Test
    public void testMaximizeScore3() {
        model model3 = createBoardFromString("""
                                                        #@#oo@
                                                        @**@**
                                                        o##@#o
                                                        @#oo#@
                                                        @*@**@
                                                        *#@##*""");
        Solution s = model3.maximizeScore();
        assertEquals(33, s.getScore());
        assertEquals(9, s.currentMoves().size());
    }
}
