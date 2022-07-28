package engine.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoader {
	
	public String processFile(String filePath) {
		String result = "";
		try {
			File file = new File(getClassPath() + filePath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				result += data;
			}
			scanner.close();
			return result;
		} catch (FileNotFoundException e) {
			System.err.println(String.format("[ERROR]: Could not find file at %s", filePath));
		}
		
		return "";
	}
	
	public static String getTitle() {
		return new FileLoader().processFile(".project").split("<")[3].replace("name>", "");
	}
	
	public String getClassPath() {
		String[] dirs = System.getProperty("java.class.path").split(";")[0].split("\\\\");
		ArrayList<String> dirArray = new ArrayList<String>();
		
		for (String dir : dirs) {
			dirArray.add(dir);
		}
		
		for (int i = 0; i < 2; i++) {
			dirArray.remove(dirArray.size() - 1);
		}
		
		String result = "";
		
		for (String dir : dirArray) {
			result += dir + "/";
		}
		
		return result;
	}
}