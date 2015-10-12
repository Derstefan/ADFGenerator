package Generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * lädt die config.txt
 * und ließt den bearbeitungspfad ein
 * @author ich
 *
 */
public class Config {
	private String file;

	public static void main(String[] args) {
		Config c = new Config();
	}
/**
 * lädt die dateien
 */
	public Config() {
		FileReader fr;
		try {
			fr = new FileReader("config.txt");
			BufferedReader br = new BufferedReader(fr);

			try {

				file = br.readLine();
				//System.out.println(file);

				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
/**
 * getter
 * @return
 */
	public String getFile() {
		return file;
	}
/**
 * setter
 * @param file
 */
	public void setFile(String file) {
		this.file = file;
	}

}
