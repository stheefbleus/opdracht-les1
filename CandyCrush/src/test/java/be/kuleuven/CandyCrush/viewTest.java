package be.kuleuven.CandyCrush;


import be.kuleuven.candycrush.model.Model;
import be.kuleuven.candycrush.view.candyCrushView;
import org.junit.jupiter.api.Test;
public class viewTest {
    @Test
    public void testIfPixelWidthandHeight_Are20(){
        Model model = new Model("Stef");
        candyCrushView ccv = new candyCrushView(model);
        assert(ccv.getHeight() == 20 && ccv.getWidth() ==20 );
    }

}
