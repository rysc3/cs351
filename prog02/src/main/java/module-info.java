module com.example.prog02 {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;


  opens com.example.prog02 to javafx.fxml;
  exports com.example.prog02;
}