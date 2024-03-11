package be.kuleuven.candycrush.view;

import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import be.kuleuven.candycrush.model.Model;

import java.util.Iterator;

public class candyCrushView extends Region {
    private int widthCandy;
    private int heightCandy;
    private Model model;

    public candyCrushView(Model model) {
        this.model = model;
        widthCandy = 20;
        heightCandy = 20;
        updateView();
    }

    public void updateView() {
        getChildren().clear();

        int i = 0 ;
        int height = 1;
        Iterator<Integer> iter = model.getSpeelbord().iterator();
        while(iter.hasNext()){
            int candy = iter.next();
            Text text = new Text(i*widthCandy,height*heightCandy, String.valueOf(candy));
            getChildren().add(text);
            if(i== model.getWidth()-1){
                i = 0;
                height++;
            }
            else{
                i++;
            }
        }
    }
}
