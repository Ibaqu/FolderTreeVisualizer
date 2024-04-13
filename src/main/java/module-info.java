module com.example.foldertreevisualizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.foldertreevisualizer to javafx.fxml;
    exports com.example.foldertreevisualizer;
}