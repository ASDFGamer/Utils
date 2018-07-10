module org.asdfgamer.utils {
    requires commons.net;
    requires java.logging;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.deploy;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires junit;
    exports org.asdfgamer.utils.config;
    exports org.asdfgamer.utils.file;
    exports org.asdfgamer.utils.other;
    exports org.asdfgamer.utils.config.tests to javafx.graphics;
    exports org.asdfgamer.utils.config.gui to javafx.graphics;
}
