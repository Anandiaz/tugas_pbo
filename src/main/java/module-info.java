module com.peliharaan {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires transitive javafx.graphics;

    opens com.peliharaan to javafx.fxml;
    exports com.peliharaan;
}
