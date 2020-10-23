package com.alienjimmey.rememberui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class BackupScript extends Thread{
	private String src;
	private String dest;
	private Label statusLabel;
	
	public BackupScript(String src, String dest, Label statusLabel) {
		this.src = src;
		this.dest = dest;
		this.statusLabel = statusLabel;
	}
	
	public void createBackup() {
		Path tempDestinationDir = Paths.get(dest);
		Path destinationDir = createSubFolder(tempDestinationDir);
		Path sourceDir = Paths.get(src);
		
			try {
				Files.walk(sourceDir)
						.forEach(sourcePath -> {
							 Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
							 try {
								System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
								Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
	
	}
	
	private Path createSubFolder(Path dest) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 LocalDateTime now = LocalDateTime.now();  
		 System.out.println(dtf.format(now));  
		 String fileName = "backup-" + dtf.format(now);
		 
		if(Files.exists(dest)) {
			if (Files.isDirectory(dest)) {
				String newFileLocation = dest.toString() + File.separatorChar + fileName;
				Path newDir = Paths.get(newFileLocation);
				if (Files.notExists(newDir)) {
					try {
						Files.createDirectory(newDir);
						return newDir;
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				return newDir;
			}
		}
		return null;
	}
	
	public void run() {
		createBackup();
		System.out.println("Done Copying");

		//Platform.runLater allows for javafx objects to be handled out side of the javafx thread.
		Platform.runLater(() -> {
			statusLabel.setText("Done Copying");
			
		});

	}

}
