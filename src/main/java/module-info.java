module com.alienjimmey.rememberui {
    requires javafx.controls;
    requires javafx.fxml;
	requires com.google.gson;
	requires javafx.graphics;
	requires javafx.base;

    opens com.alienjimmey.rememberui to javafx.fxml;
    exports com.alienjimmey.rememberui;
}