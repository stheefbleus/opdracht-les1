module be.kuleuven.candycrush {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    opens be.kuleuven.candycrush to javafx.fxml;
    exports be.kuleuven.candycrush;
    exports be.kuleuven.candycrush.model;
    opens be.kuleuven.candycrush.model to javafx.fxml;
    exports be.kuleuven.candycrush.controllers;
    opens be.kuleuven.candycrush.controllers to javafx.fxml;
    exports be.kuleuven.candycrush.view;
    opens be.kuleuven.candycrush.view to javafx.fxml;
    exports be.kuleuven.candycrush.recordsAndGenerics;
    opens be.kuleuven.candycrush.recordsAndGenerics to javafx.fxml;
    exports be.kuleuven.candycrush.application;
    opens be.kuleuven.candycrush.application to javafx.fxml;
}