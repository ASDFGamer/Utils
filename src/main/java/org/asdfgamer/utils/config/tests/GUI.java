package org.asdfgamer.utils.config.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.gui.SettingsGUI;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class GUI extends Application
{

    private final static Logger LOG = getLogger(GUI.class.getName());

    public static void main(String[] args)
    {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {

        SettingsGUI gui = new SettingsGUI(new Settings("test"));
        Scene scene = gui.getScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
