package com.boxy.mcworldstats.controller;

import com.boxy.mcworldstats.model.WorldDataReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger =  LoggerFactory.getLogger(MainController.class);

    private Stage window;
    public void setWindow(Stage window) {
        this.window = window;
    }

    public Button directoryButton;
    public Button generateButton;
    public TextField directoryField;

    @FXML
    public CheckBox opt1;
    @FXML
    public CheckBox opt2;
    @FXML
    public CheckBox opt3;
    @FXML
    public CheckBox opt4;
    @FXML
    public CheckBox opt5;
    @FXML
    public CheckBox opt6;


    File selectedDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateButton.setDisable(true);
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
            generateButton.setDisable(false);
        } else {
            directoryField.setText("No directory selected.");
        }

    }

    public void onGenerateButtonClick(ActionEvent actionEvent) {
        WorldDataReader.GetDirectoryStatistics(selectedDirectory);
    }

    public void onOptChanged(ActionEvent actionEvent) {
        Object o = actionEvent.getSource();
        if (o instanceof CheckBox cb) {
            logger.debug("Checkbox {} changed state: {}", cb.getId(), cb.isSelected());
            switch (cb.getId()) {
                case "opt1":
            }
        } else {
            logger.warn("Object {} has created an event that only option checkboxes should create",o);
        }
    }
}
