package be.kuleuven.CandyCrush;

import be.kuleuven.candycrush.model.Model;
import org.junit.jupiter.api.Test;

class ModelTest {
    @Test
    public void testScore10(){
        Model model = new Model("Stef");
        model.setScore(10);
        assert(model.getScore() == 10);
    }
    @Test
    public void testIfSpeelbord_IsNotNULL(){
        Model model = new Model("Stef");
        assert(model.getSpeelbord() != null);
    }
    @Test
    public void testIfScore_Is0BijInistatie(){
        Model model = new Model("Stef");
        assert(model.getScore() == 0);
    }

}