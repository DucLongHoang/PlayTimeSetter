module application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires okio;
    requires selenium.firefox.driver;
    requires selenium.api;

    opens application to javafx.fxml;
    exports application;
}