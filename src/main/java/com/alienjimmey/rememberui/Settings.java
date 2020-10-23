package com.alienjimmey.rememberui;

public class Settings {
	public String  sourceFileLocation;
	public String destinationFileLocation;

	public Settings(String tarURL, String destURL) {
		this.sourceFileLocation = tarURL;
		this.destinationFileLocation = destURL;
	}

}
