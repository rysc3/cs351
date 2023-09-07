module prog01 {
    requires javafx.controls;
    requires javafx.fxml;

    opens prog01 to javafx.fxml;
    exports prog01;
}
