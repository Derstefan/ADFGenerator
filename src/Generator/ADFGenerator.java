package Generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Klasse zur erstellung von ADFs
 * @author 
 *
 */
public class ADFGenerator {
	/**
	 * konstanten für die Generatorzuweisung
	 */
	public static final int LAPLACE = 0;
	public static final int KDNF = 1;
	public static final int STFconst = 2;
	public static final int STFroot = 3;
	

	/**
	 * Semantiken, welche untersucht werden
	 */
	public static final String[] interpretations = {"-cfi","-nai","-stg","-sem","-mod","-stm","-grd","-com","-adm","-prf"};
	public static final boolean[] selection = {false,
		false,false,false,false,false,false,false,false,true};
	
	/**
	 * Programminterne Variablen für die generierung
	 */
	private int min;
	private int max;
	private int maxLinks;
	private String folder = "newTest3";
	private int count;
	private int conditionFunction;
	
	private String csvData = "number,nodes,maxLinks,formulaTyp,interpretation,models,time\n";	
	private int nodeCount;
	private long z = 0;

	
/*
	public static void main2(String[] args) throws IOException { 

		boolean repeat = true;
		int min = 5, max = 10, maxLinks = 6, count = 10, conditionFunction = 0;
		String folder = "test";
		System.out.println("hello ....");
		while (repeat) {
			try {
				System.out.println("\nmin: ");
				min = Integer.valueOf(eingabe());
				System.out.println("\nmax: ");
				max = Integer.valueOf(eingabe());
				System.out.println("\nmaxLinks: ");
				maxLinks = Integer.valueOf(eingabe());
				System.out.println("\ncount: ");
				count = Integer.valueOf(eingabe());
				System.out.println("\nfolder: ");
				folder = eingabe();
				System.out
						.println("\n0 - Laplace\n1 - Kdnf\n2 - STFconst\n3 - STFroot\n\nconditionFunction: ");
				conditionFunction = Integer.valueOf(eingabe());
				// if(conditionFunction>=0 && conditionFunction<4)
				repeat = false;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("fehler bitte nochmal...");
				repeat = true;
			}
		}
		ADFGenerator g = new ADFGenerator(min, max, maxLinks, folder, count,
				conditionFunction);
		long l;

		String file = new Config().getFile();
		g.writeGenerateData();
		for (int i = 1; i <= g.count; i++) {
			
			System.out.println("adf" + i + ".adf");
			l = System.currentTimeMillis();
			g.z = 0;
			String adf = g.generate();
			for(int j = 0;j<10;j++){
			g.csvData += i + "," + g.nodeCount + "," + maxLinks + "," + conditionFunction + ","+j+",\n";
			}
			File f = new File(file + g.folder);
			f.mkdir();
			FileWriter writer = new FileWriter(file + g.folder + "/adf" + i
					+ ".adf");
			writer.write(adf);
			writer.close();
		}
		g.writeCsv(g.csvData);
		// mit löschen
						// 01110
						// 02010
						// 10002
						// 10210
						// 11020

						// 02021
						// 22100

						// ohne löschen
						// 11100-

						// 02100-
						// 10002-
						// 01120-
						// 10210-
						// 10120-
						// 11020-

						// 02021-
						// 02012-
	}
*/

	/**
	 * mainfunction mit Abfrage der Parameter 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		boolean repeat = true;
		int min = 5, max = 10, maxLinks = 6, count = 10, conditionFunction = 0;
		String folder = "test";
		System.out.println("hello ....");
		while (repeat) {
			try {
				System.out.println("\nmin: ");
				min = Integer.valueOf(eingabe());
				System.out.println("\nmax: ");
				max = Integer.valueOf(eingabe());
				System.out.println("\nmaxLinks: ");
				maxLinks = Integer.valueOf(eingabe());
				System.out.println("\ncount: ");
				count = Integer.valueOf(eingabe());
				System.out.println("\nfolder: ");
				folder = eingabe();
				System.out
						.println("\n0 - Laplace\n1 - Kdnf\n2 - STFconst\n3 - STFroot\n\nconditionFunction: ");
				conditionFunction = Integer.valueOf(eingabe());
				// if(conditionFunction>=0 && conditionFunction<4)
				repeat = false;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("fehler bitte nochmal...");
				repeat = true;
			}
		}
		String oldFolder = folder;
		//for(int t=1;t<=4;t++){
		//folder = oldFolder + t;
		//conditionFunction = t-1;
		ADFGenerator g = new ADFGenerator(min, max, maxLinks, folder, count,
				conditionFunction);
		long l;

		String file = new Config().getFile();
		g.writeGenerateData();
		for (int i = 1; i <= g.count; i++) {
			
			System.out.println("adf" + i + ".adf");
			l = System.currentTimeMillis();
			g.z = 0;
			String adf = g.generate();
			for(int j = 0;j<interpretations.length;j++){
			if(selection[j])
			g.csvData += i + "," + g.nodeCount + "," + maxLinks + "," + conditionFunction + ","+interpretations[j]+",\n";
			}
			File f = new File(file + g.folder);
			f.mkdir();
			FileWriter writer = new FileWriter(file + g.folder + "/adf" + i
					+ ".adf");
			writer.write(adf);
			writer.close();
		}
		g.writeCsv(g.csvData);
		
		System.out.println("\n\n\n\nstarting diamondaction....");
		String dataName = "adf";
	

		DiamondAction diamond = new DiamondAction(file, folder);
	
		diamond.start(dataName);
		diamond.list1 = diamond.open(file + "/" + folder + "/csvData.txt");
		String csv = diamond.list1.get(0) + "\n";
		String globalCSV = "";
		for(int i = 0;i<diamond.list2.size();++i){
			csv +=diamond.list1.get(i+1) + diamond.list2.get(i) + "\n";
			globalCSV += diamond.list1.get(i+1) + diamond.list2.get(i) + "\n";
		}
		diamond.write(file, "globalCSV", globalCSV);
		diamond.write(file+ "/" + folder, "csvData2", csv);
		}
		//}

	/*
	 * Falls die funktionen t(n und f(n) selbst gewählt werden sollen 
	 * public void conditionParameter(Generator g){ boolean repeat = true;
	 * switch(g.conditionFunction){ case 0: while (repeat) { try {
	 * System.out.println("\ninput constant!(0<c<0.5)\nc= "); g.c0 =
	 * Integer.valueOf(eingabe()); repeat = false;
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * System.out.println("fehler bitte nochmal..."); repeat = true; } } case 1:
	 * 
	 * case 2:
	 * 
	 * case 3:
	 * 
	 * default:
	 *  riesige karte mod
	 * } }
	 */
/**
 * Konstruktor für den ADFGenerator
 * @param min minimale Anzahl an Statementes
 * @param max maximale Anzahl an Statements
 * @param maxLinks maximale Anzahl an Links
 * @param folder Ordnername
 * @param count anzahl der zu genenerierenden ADFs
 * @param conditionFunction Auswahl der zufallsgeneratoren
 */
	public ADFGenerator(int min, int max, int maxLinks, String folder, int count,
			int conditionFunction) {
		this.min = min;
		this.max = max;
		this.maxLinks = maxLinks;
		this.folder = folder;
		this.count = count;
		this.conditionFunction = conditionFunction;
	
	
	
	
	
	}
	/**
	 * schreibt in generateData.txt die Generierungsparameter 
	 * @throws IOException
	 */
	public void writeGenerateData() throws IOException{
		String gd = "";
		gd+= "\nmin =" + min;
		gd+= "\nmax =" + max;
		gd+= "\nmaxLinks =" + maxLinks;
		gd+= "\nfolder =" + folder;
		gd+= "\ncount =" + count;
		gd+= "\nconditionFunction = " + conditionFunction;
		String file = new Config().getFile();
		
		
		File f = new File(file + folder);
		f.mkdir();
		FileWriter writer = new FileWriter(file + folder + "/generateData.txt");
		writer.write(gd);
		writer.close();	
		
	}
	/**
	 * schreibt die ersten Daten in die CSV
	 * @param csv String der Dateien
	 * @throws IOException
	 */
	public void writeCsv(String csv) throws IOException{
		String file = new Config().getFile();	
		File f = new File(file + folder);
		f.mkdir();
		FileWriter writer = new FileWriter(file + folder + "/csvData.txt");
		writer.write(csv);
		writer.close();	
		
	}
	
	

	// --------------------------------------------nodes
	/**
	 * generiert die Statements
	 * @param min minimale Anzahl
	 * @param max maximale Anzahl
	 * @return stringarray der statements in der ADF
	 */
	public String[] generateNodes(int min, int max) {
		int rand = (int) (Math.random() * (max - min) + min);
		this.nodeCount = rand;
		String[] nodes = new String[rand];
		for (int i = 0; i < rand; i++) {
			nodes[i] = "a" + i;
			// System.out.print(nodes[i] + " ");
		}
		// System.out.println();
		return nodes;
	}
/**
 * erstellt string aus stringarray der statements
 * @param nodes statements
 * @return string
 */
	public String getStatements(String[] nodes) {
		String statements = "";
		for (int i = 0; i < nodes.length; i++) {
			statements += "s(" + nodes[i] + "). ";
		}
		return statements;
	}

	/**
	 * generiert die Links zwischen Statements
	 * @param nodes Statements
	 * @param maxLinks maximale Anzahl an Links
	 * @return
	 */
	public ArrayList<String[]> generateLinks1(String[] nodes, int maxLinks) {
		int rand, rand2;
		ArrayList<String[]> links = new ArrayList<String[]>();
		for (int i = 0; i < nodes.length; i++) {
			if (maxLinks >= nodes.length - 1) {
				maxLinks = nodes.length - 1;
				// optimierung
			}
			rand = Math.round((float) (Math.random() * (maxLinks-1) +1));
			// System.out.println("\n\n"+rand+"\n\n\n");
			links.add(new String[rand]);
			fillArray(links.get(i)); 

			int j = 0;
			// System.out.print(i + " mit " + (rand) + " links: ");
			while (j < rand) {

				rand2 = (int) (Math.random() * nodes.length);
				if (false == contains(links.get(i), nodes[rand2])) {
					links.get(i)[j] = nodes[rand2];
					
					j++;
				//	 System.out.print(nodes[rand2] + " ");
				}

			}
			// System.out.println();

		}
		return links;
	}
/**
 * initialisiert den Array																																		eew
 * @param list liste der statements
 */
	private void fillArray(String[] list) {
		for (int i = 0; i < list.length; i++) {
			list[i] = "";
		}
	}
/**
 *  prüft ob die liste den das statement enthält
 * @param list lsite
 * @param str statement
 * @return boolean
 */
	private boolean contains(String[] list, String str) {
		boolean b = false;
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(str)) {
				return true;
			}
		}
		return false;

	}
/*

	public String generateConditions(String[] list) {
		int n = list.length;
		String str = recursiveHelper(n, "", -1, list);
		if (str == "c(f)" || str == "c(v)") {

		} else {
			str = "or(" + str.substring(0, str.length() - 1) + ")";
		}
		return str;
	}
*/
	/**
	 * generiert die Aussagenlogische Formel
	 * @param gTyp generatortyp
	 * @param list liste der statements
	 * @return gibt aussagenlogische Formel zurück
	 */
	public String generateConditions2(int gTyp, String[] list){
		return Main.start3(list.length, gTyp, list);
	}
/**
 * 
 * @param n
 * @param formula
 * @param z2
 * @param list
 * @return
 */
	/*
	public String recursiveHelper(int n, String formula, int z2, String[] list) {
		double rand = Math.random();

		// System.out.println(rand+" "+1/(Math.pow(2, n)));
		if (n >= 1) {
			if (rand < t(n)) {
				z = z + (long) Math.pow(2, n);

				if (z2 != -1) {
					formula += list[z2];
					// System.out.println("and(" + formula + "),");
					return clause(formula) + ",";
				} else {
					formula = "c(v)";// zwei leerzeichen für Stringkürzung
					// System.out.println(formula);
					return formula;
				}

				// return "1 " + n + " ";
			} else if (rand > 1 - f(n)) {
				z = z + (long) Math.pow(2, n);

				if (z2 != -1) {
					formula += "neg(" + list[z2] + ")";
					// System.out.println("and(" + formula + "),");
					return clause(formula) + ",";
				} else {
					formula = "c(f)";// zwei leerzeichen für Stringkürzung
					// System.out.println(formula);
					return formula;
				}

				// return "0 " + n + " ";
			} else {
				String s;

				if (z2 == -1) {

					return recursiveHelper(n - 1, formula, z2 + 1, list) + ""
							+ recursiveHelper(n - 1, formula, z2 + 1, list);
				} else {
					return recursiveHelper(n - 1, formula + list[z2] + ",",
							z2 + 1, list)
							+ recursiveHelper(n - 1, formula + "neg("
									+ list[z2] + ")" + ",", z2 + 1, list);
				}
			}
		} else {
			if (rand > 0.5) {
				z++;

				if (z2 != -1) {
					formula += list[z2];
					// System.out.println("and(" + formula + "),");
					return clause(formula) + ",";
				} else {
					formula = "c(v)";
					// System.out.println(formula);
					return formula;
				}

				// return "1";
			} else {
				z++;

				if (z2 != -1) {
					formula += "neg(" + list[z2] + ")";

					return clause(formula) + ",";
				} else {
					formula = "c(f)";
					// System.out.println(formula);
					return formula;
				}
				// return "0";
			}
		}
	}
*/

	/**
	 * generiert die ADFs
	 */
	public String generate() {
		String text = "";
		String[] nodes = generateNodes(min, max);
		text += getStatements(nodes) + "\n";

		ArrayList<String[]> links = generateLinks1(nodes, maxLinks);

		for (int i = 0; i < links.size(); i++) {
			text += "ac(" + nodes[i] + ",";
			text += generateConditions2(conditionFunction,links.get(i));
			text += ").\n";
		}

		// System.out.println(text);
		return text;
	}
/**
 * prüft ob im string ein komma enthalten ist
 * @param s string
 * @return boolean
 */
	private boolean containsComma(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ',')
				return true;
		}
		return false;
	}
/**
 * erstellt einen clauselstring
 * @param formula
 * @return
 */
	private String clause(String formula) {
		String s = "";
		if (containsComma(formula)) {
			s = "and(" + formula + ")";
		} else {
			s = formula;
		}
		return s;
	}
	
	/**
	 * 
	 * @param formula
	 * @return
	 */
	/*
	private String notClause(String formula) {
		String s = "";
		if (containsComma(formula)) {
			s = "and(" + formula + ")";
		} else {
			s = formula;
		}
		return s;
	}
	*/
/*
	private double t(int n) {

		switch (this.conditionFunction) {
		case 0:
			return 0.2;
		case 1:
			return 0.05 + 0.35 / maxLinks * (maxLinks - n);
		case 2:
			return 0.05 + Math.sqrt(0.22 / maxLinks * (maxLinks - n));
		case 3:
			return 0.15 / 2 + (1 - 0.15) / (Math.pow(2, n));
		default:
			return 0.1 / 2 + (1 - 0.1) / (Math.pow(2, n));
		}

	}
*/
	/*
	private double f(int n) {

		// diese funktion sollte noch geändert werden
		return t(n);
	}
	 */
	/**
	 * eingabefunktion
	 * @return
	 */
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

}
