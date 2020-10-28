package com.alienjimmey.rememberui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PrimaryController {
	
	@FXML private TextField tarTextField;
	@FXML private TextField destTextField;
	@FXML private TextField addonsTextField;
	
	@FXML private VBox addonsVBox;
	
	@FXML private Button backupButton;
	@FXML private Button saveButton;
	@FXML private Button selectFileButton1;
	@FXML private Button selectFileButton2;
	@FXML private Button SelectFileButton3;
	@FXML private CheckMenuItem addonsMenuItem;
	@FXML private Label statusLabel;
	@FXML private Label progressLabel;
	
	private final static String PROPERTIESFILE = "C:\\Users\\fredjino\\dev\\eclipse-workspace\\rememberui\\src\\main\\resources\\com\\alienjimmey\\properties.json";
	private String SOURCEFILELOCATION = "";
	private String DESTFILELOCATION = "";
	private String ADDONSFILELOCATION = "";
	private Settings globalSettings;
	
	@FXML protected void initialize() {
		
		addonsVBox.managedProperty().bind(addonsVBox.visibleProperty());
		backupButton.setDisable(true);
		loadSettings();
		
		tarTextField.setText(SOURCEFILELOCATION);
		destTextField.setText(DESTFILELOCATION);
		addonsTextField.setText(ADDONSFILELOCATION);
		
		setupEvents();
		
		
	}
	
	@FXML
	private void saveLocatons() {		
		String tarURL = tarTextField.getText();
		String destURL = destTextField.getText();
		String addonsDirURL = addonsTextField.getText();
		
		//Checks if tar address exist; if not sent a warning to user
		if (tarURL.isBlank() || destURL.isBlank()) {
			statusLabel.setText("Invalid file location added.");
			return;
		}
		
		//check if the selected folders exist
		if (isFolder(tarURL) == false || isFolder(destURL) == false ) {
			statusLabel.setText("Invalid folder location selected.");
			return;
		}
		
		if (addonsMenuItem.isSelected()) {
			if ( addonsDirURL.isBlank() || !isFolder(addonsDirURL)) {
				statusLabel.setText("Invalid folder location selected.");
				return;
			}
		} else {
			//if addons option is toggled of use last remembered location.
			addonsDirURL = ADDONSFILELOCATION;
		}

		try {
			Settings settings = new Settings(tarURL, destURL, addonsDirURL);
			Writer writer = new FileWriter(PROPERTIESFILE);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(settings, writer);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadSettings();
		saveenablesBackupButton(SOURCEFILELOCATION, DESTFILELOCATION);
		statusLabel.setText("Saved.");
	}
	
	@FXML 
	private void copyFile() {
		backupButton.setDisable(true);
		statusLabel.setText("Backing up in progress.");
		
		//BackupScript script = new BackupScript(SOURCEFILELOCATION, DESTFILELOCATION, statusLabel);
		BackupScript script = new BackupScript(globalSettings, statusLabel, addonsMenuItem);
		script.start();	
	}
	
	@FXML
	private void selectFile(ActionEvent event) {
		Button button = (Button) event.getSource();
		Stage stage = (Stage) button.getScene().getWindow();
		
		File selectedFile = fileChooser(stage);
		String fileLocation = selectedFile.toString();
		
		printToTextField(button, fileLocation);
	}
	
	@FXML
	private void toggleAddons() {
		backupButton.setDisable(true);
		
		if (addonsMenuItem.isSelected()) {
			addonsVBox.setVisible(true);
			addonsVBox.setDisable(false);
		} else {
			addonsVBox.setVisible(false);
			addonsVBox.setDisable(true);
		}
	}
	
	//TODO: choose a butter name for this function.
	private void printToTextField(Button button, String fileLocation) {
		String buttonID = button.getId();
		
		switch (buttonID) {
		case "selectFileButton1":
			tarTextField.setText(fileLocation);
			break;
		
		case "selectFileButton2":
			destTextField.setText(fileLocation);
			break;
		case "selectFileButton3":
			addonsTextField.setText(fileLocation);
			break;
		default:
			break;
		}
	}
	
	//returns a directory that the user selected as a File object.
	public File fileChooser(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Folder");
		
		//returns the file that the user selected
		File selectedDirectory = directoryChooser.showDialog(stage);
		return selectedDirectory;
	}
	
	private void loadSettings() {
		try {
			Gson gson = new GsonBuilder().create();
			FileReader reader = new FileReader(PROPERTIESFILE);
			Settings settings = gson.fromJson(reader, Settings.class);
			
			this.SOURCEFILELOCATION = settings.sourceFileLocation;
			this.DESTFILELOCATION = settings.destinationFileLocation;
			this.ADDONSFILELOCATION = settings.addonsFileLocation;
			
			this.globalSettings = settings;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void saveenablesBackupButton(String src, String dest) {
		if (src.isBlank() || dest.isBlank()) {
			backupButton.setDisable(true);
			progressLabel.setText("Click save to unlock the backup button");
			
		} else {
			backupButton.setDisable(false);
			progressLabel.setText("");
		}
	}
	
	//checks if the passed string is a folder.
	private boolean isFolder(String str) {
		Path path = Paths.get(str);
		if (Files.exists(path) && Files.isDirectory(path)) {
			return true;
		}
		return false;
	}
	
	private void setupEvents() {
		tarTextField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				backupButton.setDisable(true);
			}
        });
		
		destTextField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				backupButton.setDisable(true);
			}
        });
		
		addonsTextField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				backupButton.setDisable(true);
			}
        });
	}
	
	@FXML
	private void launchAbout() {
		 FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("secondary.fxml"));
		 Parent root;
		try {
			root = fxmlLoader.load();
			Stage stage = new Stage();
	         stage.setTitle("About");
	         stage.setScene(new Scene(root, 450, 450));
	         stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
	
}
