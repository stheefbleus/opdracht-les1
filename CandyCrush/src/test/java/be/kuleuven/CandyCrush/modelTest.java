package be.kuleuven.CandyCrush;

import be.kuleuven.candycrush.model.model;
import org.junit.jupiter.api.Test;

class modelTest {
    @Test
    public void testScore10(){
        model model = new model("Stef");
        model.setScore(10);
        assert(model.getScore() == 10);
    }
    @Test
    public void testIfScore10plusScore5_equals15(){
        model model = new model("Stef");
        model.setScore(10);
        model.setScore(model.getScore()+5);
        assert(model.getScore() == 15);
    }
    @Test
    public void testIfSpeelbord_IsNotNULL(){
        model model = new model("Stef");
        assert(model.getSpeelbord() != null);
    }
    @Test
    public void testIfScore_Is0BijInistatie(){
        model model = new model("Stef");
        assert(model.getScore() == 0);
    }
    @Test
    public void testIfScore_Is0BijNaReset(){
        model model = new model("Stef");
        model.reset();
        assert(model.getScore() == 0);
    }
    @Test
    public void testIfSpeelbord_IsNotNULL_NaReset(){
        model model = new model("Stef");
        model.reset();
        assert(model.getSpeelbord() != null);
    }
    @Test
    public void testIfWidthIs10(){
        model model = new model("Stef");
        assert(model.getBoard().width() == 10);
    }
    @Test
    public void testIfHeightIs10(){
        model model = new model("Stef");
        assert(model.getBoard().height() == 10);
    }
    /*@Test
    public void testWhenAValueInSpeelbordHasChanged_ThatTheRightIndexHasChanged(){
        model model = new model("Stef");
        model.getSpeelbord().set(4,5);
        assert(model.getSpeelbordValueOfIndex(4) == (int)5);
    }*/
    @Test
    public void testWhenSpelerIsSet_ThatSpelerIsThatSpeler() {
        model model = new model("Stef");
        assert (model.getSpeler() == "Stef");
    }

}