package com.boxy.mcworldstats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    private Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

    public TextArea outputBox;
    public Button directoryButton;
    public Button generateButton;
    public TextField directoryField;


    @FXML
    public void onDirectoryButtonClick(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select a Save Directory");
        File initialDir = new File(System.getProperty("user.home"));
        if (initialDir.exists() && initialDir.isDirectory()) {
            dc.setInitialDirectory(initialDir);
        }

        File selectedDirectory = dc.showDialog(window);

        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            directoryField.setText(selectedDirectory.getAbsolutePath());
        } else {
            directoryField.setText("No directory selected.");
        }

    }

    public void onGenerateButtonClick(ActionEvent actionEvent) {
    }
}
