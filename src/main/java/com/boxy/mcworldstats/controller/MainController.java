package com.boxy.mcworldstats.controller;

import com.boxy.mcworldstats.model.WorldDataReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

    public TextArea outputBox;
    public Button directoryButton;
    public Button generateButton;
    public TextField directoryField;

    File selectedDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Home directory: "+System.getProperty("user.home"));
    }

    @FXML
    public void onDirectoryButtonClick(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Select a Save Directory");

        String homedir = System.getProperty("user.home");
        Path initialPath = FileSystems.getDefault().getPath(homedir,"AppData","Roaming",".minecraft");
        File initialDir = initialPath.toFile();

        if (initialDir.exists() && initialDir.isDirectory()) {
            dc.setInitialDirectory(initialDir);
        }

        selectedDirectory = dc.showDialog(window);

        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            directoryField.setText(selectedDirectory.getAbsolutePath());
        } else {
            directoryField.setText("No directory selected.");
        }

    }

    public void onGenerateButtonClick(ActionEvent actionEvent) {
        WorldDataReader.GetDirectoryStatistics(selectedDirectory);
    }

}
