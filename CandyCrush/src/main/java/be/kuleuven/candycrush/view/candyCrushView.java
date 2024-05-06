package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.model;
import be.kuleuven.candycrush.recordsAndGenerics.Position;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import static be.kuleuven.candycrush.recordsAndGenerics.Position.fromIndex;

public class candyCrushView extends Region {
    private int widthCandy;
    private int heightCandy;
    private model model;

    public Node makeCandyShape(Position position, model.Candy candy) {
        Node node;
        int x = position.kolomNummer() * widthCandy;
        int y = position.rijNummer() * heightCandy;

        if (candy instanceof model.NormalCandy) {
            Circle circle = new Circle(x+widthCandy/2, y+heightCandy/2, widthCandy/ 2);
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
        widthCandy = 40;
        heightCandy = 40;
        updateView();
    }

    public void updateView() {
        getChildren().clear();

        for (int i = 0; i < model.getBoardsize().height() * model.getBoardsize().width(); i++) {
            Position pos = fromIndex(i, model.getSpeelbord().getSize());
            model.Candy candyModel = model.getSpeelbord().getCellAt(pos);

            if(candyModel != null){
                Node candy = makeCandyShape(pos, candyModel);
                candy.setOnMouseClicked(e -> klik(e));
                getChildren().add(candy);
            }
        }
    }
    public void klik(MouseEvent e){
        Node candy = (Node) e.getSource();
        double x = candy.getBoundsInParent().getMinX();
        double y = candy.getBoundsInParent().getMinY();
        model.klik((int) x, (int) y);
        updateView();
    }
}
