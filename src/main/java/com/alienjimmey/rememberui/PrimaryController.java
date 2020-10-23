package com.alienjimmey.rememberui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController {
	
	@FXML private TextField tarTextField;
	@FXML private TextField destTextField;
	
	@FXML private Button backupButton;
	@FXML private Button saveButton;
	@FXML private Label statusLabel;
	@FXML private Label progressLabel;
	
	private final static String PROPERTIESFILE = "C:\\Users\\fredjino\\dev\\eclipse-workspace\\rememberui\\src\\main\\resources\\com\\alienjimmey\\properties.json";
	private String SOURCEFILELOCATION = "";
	private String DESTFILELOCATION = "";
	
	@FXML protected void initialize() {
		backupButton.setDisable(true);
		loadSettings();
		
		tarTextField.setText(SOURCEFILELOCATION);
		destTextField.setText(DESTFILELOCATION);
		
	}
	
	@FXML
	private void saveLocatons() {		
		String tarURL = tarTextField.getText();
		String destURL = destTextField.getText();
		
		//Checks if tar address exist; if not sent a warning to user
		if (tarURL.isBlank() || destURL.isBlank()) {
			statusLabel.setText("Invalid file location added.");
			return;
		}

		try {
			Settings settings = new Settings(tarURL, destURL);
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
		System.out.println("save button pressed.");
		statusLabel.setText("Saved.");
	}
	
	@FXML 
	private void copyFile() {
		
		backupButton.setDisable(true);
		statusLabel.setText("Backing up in progress.");
		
		BackupScript script = new BackupScript(SOURCEFILELOCATION, DESTFILELOCATION, statusLabel);
		script.start();
			//unlock button
		
		
	}
	
	private void loadSettings() {
		try {
			Gson gson = new GsonBuilder().create();
			
			FileReader reader = new FileReader(PROPERTIESFILE);
			Settings settings = gson.fromJson(reader, Settings.class);
			
			this.SOURCEFILELOCATION = settings.sourceFileLocation;
			this.DESTFILELOCATION = settings.destinationFileLocation;
			
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
	
    
}
