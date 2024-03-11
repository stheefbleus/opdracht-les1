module be.kuleuven.candycrush {
    requires javafx.controls;
    requires javafx.fxml;
    requires be.kuleuven.CheckNeighboursInGrid;
    requires org.controlsfx.controls;
    opens be.kuleuven.candycrush to javafx.fxml;
    exports be.kuleuven.candycrush;
    exports be.kuleuven.candycrush.model;
    opens be.kuleuven.candycrush.model to javafx.fxml;
    exports be.kuleuven.candycrush.controllers;
    opens be.kuleuven.candycrush.controllers to javafx.fxml;
    exports be.kuleuven.candycrush.view;
    opens be.kuleuven.candycrush.view to javafx.fxml;
}