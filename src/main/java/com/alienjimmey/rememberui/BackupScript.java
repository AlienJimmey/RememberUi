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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;

public class BackupScript extends Thread{
	private String src;
	private String dest;
	private Settings settings;
	private Label statusLabel;
	private CheckMenuItem addonsMenuItem;
	
	public BackupScript(String src, String dest, Label statusLabel) {
		this.src = src;
		this.dest = dest;
		this.statusLabel = statusLabel;
	}
	
	public BackupScript(Settings settings, Label statusLabel, CheckMenuItem addonsMenuItem) {
		this.settings = settings;
		this.addonsMenuItem = addonsMenuItem;
		this.statusLabel = statusLabel;
	}
	
	public void createBackup() {
		Path backupDirSrc = Paths.get(settings.getDestinationFileLocation());
		Path backupsDirDest = createBackupDir(backupDirSrc);
		
		Path wtfDirSrc = Paths.get(settings.getSourceFileLocation());
		Path wtfDirDest = createSubDir(backupsDirDest, "WTF");
		copyPaste(wtfDirSrc, wtfDirDest);
		
		if (addonsMenuItem.isSelected()) {
			Path addonsDirDestSrc = Paths.get(settings.getAddonsFileLocation());
			Path addonsDirDest = createSubDir(backupsDirDest, "addons");
			copyPaste(addonsDirDestSrc, addonsDirDest);
		}
		printMessage("Done copying all files.");
	}
	
	private Path createBackupDir(Path dest) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		 LocalDateTime now = LocalDateTime.now();  
		 System.out.println(dtf.format(now));  
		 String fileName = "backup-" + dtf.format(now);
		 
		
		if (Files.exists(dest) && Files.isDirectory(dest)) {
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
		
		return null;
	}
	
	public Path createSubDir(Path dest, String folderName) {
	
		if (Files.exists(dest) && Files.isDirectory(dest)) {
			String newFileLocation = dest.toString() + File.separatorChar + folderName;
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
		} else {
			System.out.println("unable to create the sub folder " + folderName +" for backups.");
		}
		
		return null;
	}
	
	
	public boolean copyPaste(Path srcDir, Path destDir) {
		if (srcDir != null || destDir != null) {
			if (Files.exists(srcDir) && Files.exists(destDir) && Files.isDirectory(srcDir) && Files.isDirectory(destDir)) {
				try {
					Files.walk(srcDir)
							.forEach(sourcePath -> {
								 Path targetPath = destDir.resolve(srcDir.relativize(sourcePath));
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
			printMessage("Done copying: " + srcDir.toString());	
			return true;
			} 
			else {
				System.out.println("files selected are not folder.");
				printMessage("Error backing up: please select valid folders");
			}
		} 
		
		System.out.println("error getting dir location.");
		printMessage("Error backing up: folder paths are null.");
		return false;
	}
	
	//prints given string to the status label
	private void printMessage(String msg) {
		//Platform.runLater allows for javafx objects to be handled out side of the javafx thread.
		Platform.runLater(() -> {
			statusLabel.setText(msg);
			
		});
	}
	
	public void run() {
		createBackup();
		


	}

}
