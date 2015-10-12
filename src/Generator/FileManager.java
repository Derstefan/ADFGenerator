package Generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	public FileManager() {

	}

	public boolean write(String text, String file, String dataName) {

		try {

			String s;
			if (file.charAt(file.length() - 1) == '/') {
				s = "";
			} else {
				s = "/";
			}
			System.out.println(file + s + dataName);

			File zielDatei = new File(file + s + dataName);

			FileWriter writer = new FileWriter(zielDatei);

			writer.write(text);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		return true;
	}

}
