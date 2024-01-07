module com.example.othello {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.othello to javafx.fxml;
    exports com.example.othello;
}