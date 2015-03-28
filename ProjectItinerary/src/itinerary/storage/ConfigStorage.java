package itinerary.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigStorage {
	private File configFile;
	
	private static final String CONFIG_FILE_NAME = ".ITconfig";
	
	public ConfigStorage () {
		configFile = new File(CONFIG_FILE_NAME);
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				// Should not result in any errors, catching for safety
				e.printStackTrace();
			}
		}
	}
	
	public String getStorageFileName () throws IOException {
		FileInputStream fileInputStream = new FileInputStream(configFile);
		Scanner fileScanner = new Scanner(fileInputStream);
		if (!fileScanner.hasNext()) {
			fileScanner.close();
			fileInputStream.close();
			return null;
		}
		String filename = fileScanner.nextLine();
		fileScanner.close();
		fileInputStream.close();
		return filename;
	}
	
	public void setStorageFileName (String storageFileName) throws IOException {
		FileWriter writer = new FileWriter(configFile, false);
		writer.write(storageFileName);
		writer.close();
	}
}
