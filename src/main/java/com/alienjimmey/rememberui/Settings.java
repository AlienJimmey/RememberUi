package com.alienjimmey.rememberui;

public class Settings {
	public  String  sourceFileLocation;
	public  String destinationFileLocation;
	public  String addonsFileLocation;

	public Settings(String tarURL, String destURL) {
		this.sourceFileLocation = tarURL;
		this.destinationFileLocation = destURL;
	}
	
	public Settings(String tarURL, String destURL, String addonsDirLocation) {
		this.sourceFileLocation = tarURL;
		this.destinationFileLocation = destURL;
		this.addonsFileLocation = addonsDirLocation;
	}

	public String getSourceFileLocation() {
		return sourceFileLocation;
	}

	public void setSourceFileLocation(String sourceFileLocation) {
		this.sourceFileLocation = sourceFileLocation;
	}

	public String getDestinationFileLocation() {
		return destinationFileLocation;
	}

	public void setDestinationFileLocation(String destinationFileLocation) {
		this.destinationFileLocation = destinationFileLocation;
	}

	public String getAddonsFileLocation() {
		return addonsFileLocation;
	}

	public void setAddonsFileLocation(String addonsFileLocation) {
		this.addonsFileLocation = addonsFileLocation;
	}
	
	

}
