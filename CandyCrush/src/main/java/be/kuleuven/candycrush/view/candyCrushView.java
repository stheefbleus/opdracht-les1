package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.model;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Iterator;

public class candyCrushView extends Region {
    private int widthCandy;
    private int heightCandy;
    private be.kuleuven.candycrush.model.model model;

    public Node makeCandyShape(model.position position, model.Candy candy) {
        Node node;
        int x = position.kolomNummer() * widthCandy;
        int y = position.rijNummer() * heightCandy;

        if (candy instanceof model.NormalCandy) {
            Circle circle = new Circle(x, y, widthCandy / 2);
            int color = ((model.NormalCandy) candy).color();
            switch (color) {
                case 0:
                    circle.setFill(Color.RED);
                    break;
                case 1:
                    circle.setFill(Color.GREEN);
                    break;
                case 2:
                    circle.setFill(Color.BLUE);
                    break;
                case 3:
                    circle.setFill(Color.YELLOW);
                    break;
            }
            node = circle;
        } else {
            Rectangle rectangle = new Rectangle(x, y, widthCandy, heightCandy);
            if (candy instanceof model.gummyBeertje) {
                rectangle.setFill(Color.PURPLE);
            } else if (candy instanceof model.jollyRanger) {
                rectangle.setFill(Color.ORANGE);
            } else if (candy instanceof model.dropVeter) {
                rectangle.setFill(Color.BLACK);
            } else if (candy instanceof model.Pèche) {
                rectangle.setFill(Color.PINK);
            }
            node = rectangle;
        }
        return node;
    }

    public candyCrushView(be.kuleuven.candycrush.model.model model) {
        this.model = model;
        widthCandy = 20;
        heightCandy = 20;
        updateView();
    }

    public void updateView() {
        getChildren().clear();

        int i = 0 ;
        int height = 1;
        Iterator<model.Candy> iter = model.getSpeelbord().iterator();
        while(iter.hasNext()){
            model.Candy candy = iter.next();
            Text text = new Text(i*widthCandy,height*heightCandy, String.valueOf(candy));
            getChildren().add(text);
            if(i== model.getBoard().width()-1){
                i = 0;
                height++;
            }
            else{
                i++;
            }
        }
    }
}
