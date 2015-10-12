package Generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class DiamondAction {

	// public static final String[] interpretations =
	// {"-cfi","-nai","-stg","-sem","-mod","-stm","-grd","-com","-adm","-prf"};
	// public static final boolean[] selection =
	// {true,true,true,true,true,true,true,true,true,true};

	public static final String DATATYPE_ADF = ".adf";
	private String directory;
	private String Request;

	// liste aller ordner
	private String folder;
	ArrayList<String> list1;
	ArrayList<String> list2 = new ArrayList<String>();

	/**
	 * constructor für 
	 * @param directory
	 * @param folder
	 */
	public DiamondAction(String directory, String folder) {
		this.directory = directory;
		this.folder = folder;
	}

	public static void main(String[] args) throws IOException {

		// dateiname !!!!!!!!!!!!!!!!!!
		String file = new Config().getFile();
		System.out.println("Name of the folder");
		String folder = eingabe();
		String dataName = "adf";

		DiamondAction diamond = new DiamondAction(file, folder);

		diamond.start(dataName);
		diamond.list1 = diamond.open(file + "/" + folder + "/csvData.txt");
		String csv = diamond.list1.get(0) + "\n";
		String globalCSV = "";
		for (int i = 0; i < diamond.list2.size(); ++i) {
			csv += diamond.list1.get(i + 1) + diamond.list2.get(i) + "\n";
			globalCSV += diamond.list1.get(i + 1) + diamond.list2.get(i) + "\n";
		}
		diamond.write(file, "globalCSV", globalCSV);
		diamond.write(file + "/" + folder, "csvData2", csv);
	}

	public void start(String dataName) throws IOException {
		int number = 1;
		boolean bool = true;
		while (bool) {
			bool = createADFData(DATATYPE_ADF, dataName, number);
			// String s = diamond.action(CFI, DATATYPE_ADF, dataName, number);
			// if (s != null) {
			// System.out.println(s);
			// diamond.parser(s);
			number++;
			System.out.println("\n\n");
		}
	}

	public String action(int interpretation, String type, String dataName,
			int number) throws IOException {

		String command = "python3.3 diamond.py";
		command += " " + folder + "/" + dataName + number + type;
		command += " " + ADFGenerator.interpretations[interpretation];

		File f = new File(directory + "/" + folder + "/" + dataName + number
				+ type);

		if (f.exists() != true) {
			return null;
		}
		System.out.print("-------->" + dataName + number + type + " "
				+ ADFGenerator.interpretations[interpretation]);
		long l = System.currentTimeMillis();
		// System.out.println(command + ":\n\n");

		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
		builder.redirectErrorStream(true);
		builder.directory(new File(directory));
		String response = "";
		try {
			Process p = builder.start();
			InputStream shellIn = p.getInputStream();

			response = convertStreamToStr(shellIn);
			shellIn.close();

		}

		catch (IOException e) {
			System.out.println("Error " + e.getMessage());
		}
		System.out.println("   " + (System.currentTimeMillis() - l) + " ms");
		return response;
	}

	// geklaut ....
	private String convertStreamToStr(InputStream is) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public String[] parser(String diamondOut) {
		 
		if(diamondOut=="" || diamondOut==null)
			return null;
		int m = diamondOut.indexOf("Models");
		if (m == -1) {
			String[] s = { "Error1: no Structure found" };
			System.out.println("Error1: no Structure found");
			return null;

		}
		String str = diamondOut.substring(m);
		str = str.replace(" ", "");

		// for(int i = 0;i<str.length();++i){
		// System.out.println("( " +i+" , " + str.charAt(i) + " )");
		// }
		boolean read = false;
		int counter = 0;
		String[] values = { "", "", "", "", "", "", "" };

		for (int i = 0; i < str.length(); ++i) {

			if (read
					&& (str.charAt(i) == '\n' || str.charAt(i) == 's' || str
							.charAt(i) == '+')) {
				read = false;
				counter++;
				// System.out.print(" ");
			}
			if (read) {
				values[counter] += str.charAt(i);
				// System.out.print(str.charAt(i));
			}
			if (str.charAt(i) == ':') {
				read = true;
			}

		}
		// System.out.println();
		return values;

	}

	public boolean createADFData(String type, String dataName, int number)
			throws IOException {
		String[][] data = new String[ADFGenerator.interpretations.length][7];
	
		for (int i = 0; i < ADFGenerator.interpretations.length; i++) {
			if (ADFGenerator.selection[i]) {
				if(i<10){
				data[i] = parser(action(i, type, dataName, number));
				
				if (data[i] == null) {
					System.out.println("-------------------------------Fertig");
					return false;
				}
				list2.add(data[i][0] + "," + data[i][2]);
				} else {
					//für andere i, benötigt man anderen parser
				}
				
				
			}

		}

		Data d = new Data(data);
		// System.out.println("(" + dataName + number + type + ":" +
		// d.toString()
		// + ")");
		write(directory + folder, "alldata", "(" + dataName + number + type
				+ ":" + d.toString() + ")");
		return true;
	}

	public void write(String file, String dataName, String text)
			throws IOException {
		File f = new File(file);
		FileWriter writer = new FileWriter(file + "/" + dataName + ".txt", true);
		writer.write(text);
		writer.close();
	}

	private static String eingabe() {
		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));
		String input = "";
		try {
			input = console.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return input;
	}

	public ArrayList<String> open(String data) throws IOException {
		FileReader fr = new FileReader(data);
		BufferedReader reader = new BufferedReader(fr);
		ArrayList<String> list = new ArrayList<String>();
		String zeile = reader.readLine();
		while (zeile != null) {
			list.add(zeile);
			zeile = reader.readLine();
			// System.out.println(zeile);
		}
		reader.close();
		// System.out.println(list.size());
		return list;
	}

}
