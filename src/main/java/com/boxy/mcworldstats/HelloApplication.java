package com.boxy.mcworldstats;

import com.boxy.mcworldstats.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        MainController controller = fxmlLoader.getController();
        controller.setWindow(stage);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
