package engine.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileLoader {
	public String readFile(String fileName) {
		String line = "";
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				line += scanner.nextLine();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println(String.format("[ERROR]: Could not find file at %s"
					+ ", printing stacktrace...", fileName));
			e.printStackTrace();
		}
		
		return line;
	}
	
	public static String getTitle() {
		return new FileLoader().readFile(".project").split(">")[3].replace("</name", "");
	}
}
