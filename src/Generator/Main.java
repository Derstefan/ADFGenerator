package Generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static final String FILE = "/home/ich/Dropbox/Studium/Informatik/Semester VI/Bachelorarbeit/Formulas/";
	public static long SEED = 987623;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// for (int j = 40; j <= 40; j++) {
		// for (double c = 0; c <= 10; c++) {
		// for (double m = 10; m <= 50; m ++) {
		// start(j, Generator.RECURSIVE_GENERATOR, FILE, 1000, c/100, m/10);
		// }
		// }
		// }
		// start(15, Generator.RECURSIVE_GENERATOR, FILE, 10000, 0, 0);
		String[] list = new String[10];
		list[0] = "a";
		list[1] = "b";
		list[2] = "c";
		list[3] = "d";
		list[4] = "e";
		list[5] = "f";
		list[6] = "g";
		list[7] = "h";
		list[8] = "i";
		list[9] = "j";
		Generator g = new Generator(3,921837, 10);
		System.out.println(Main.getFormula(g.generate(), list));
		//System.out.println(start3(12, Generator.LAPLACE_GENERATOR, list));
		// for(int n=17;n<25;n++){
		// for(int c=30;c<50;c++){
		// for(int m=12;m<20;m++){
		// start(n, Generator.RECURSIVE_GENERATOR, FILE, 1000, (double)c,
		// (double)m);
		// }
		// }
		// }

	}

	public static void start(int n, int gTyp, String file, int count, double c,
			double m) {
		long l;
		long seed = SEED;
		String gen;
		int paramFunction = 3;
		String folder = "F_ " + n + " _" + seed;
		Generator g = new Generator(gTyp, seed, n);
		String t = "gerneratorTyp";

		if (gTyp == 0) {
			gen = "LAPLACE";
		} else if (gTyp == 1) {

			gen = "REC";
			g.setcF(paramFunction);
			t += ",c,m";
			g.c = c / 100;
			g.m = m / 4;

		} else {
			gen = "KDNF";
		}
		t += ",ms,clauses,vars";
		String name = "F_ " + (int) (m) + " _ " + (int) (c) + " _" + gen;
		// String name = "F_ " + (int)(c) +" _" + gen;
		for (int i = 1; i <= n; i++) {
			t += ",c" + i;
		}
		for (int i = 0; i < n; i++) {
			t += ",a" + i;
		}

		write(file, folder, name, t + "\n");
		System.out.println(t);

		// g.m=0;
		// for (double c = 0.05; c < 0.4; c += 0.01) {
		// for (double m = c; m < 0.4; m += 0.01) {
		// g.c=c;
		// g.m=m;

		for (int i = 0; i < count; i++) {

			l = System.currentTimeMillis();

			ArrayList<byte[]> b2 = g.generate();
			double[] list = g.countclauseSize(b2);
			double[] list2 = g.countVarDepend(b2);
			String s = "" + gTyp;
			if (gTyp == 1) {
				s += "," + g.c + "," + g.m;
			}
			s += "," + (System.currentTimeMillis() - l) + "," + b2.size() + ","
					+ g.countVars(b2) + "," + Generator.arrayToString(list)
					+ "," + Generator.arrayToString(list2);
			System.out.println(i + " : " + s);
			write(file, folder, name, s + "\n");

			// System.out.println("Formel Nummer " + i + "  :");
			//
			//
			// System.out.println(g.outPut(b2));
			// System.out.println("Anzahl der Klauseln : " + b2.size());
			// System.out.println("Anzahl abhängiger Variablen : "
			// + g.coun tVars(b2));
			//

			// System.out.println("Anzahl der Klauseln mit größe m: "
			// + Generator.arrayToString(list));

			// System.out.println("Anzahl der Vorkommen von Variablen: "
			// + Generator.arrayToString(list2) + "");
			// System.out.println(System.currentTimeMillis() - l +
			// " ms\n\n");

			// }
			// }
		}

	}

	/**
	 * getFormula
	 * 
	 * @param n
	 * @param gTyp
	 * @param file
	 * @param count
	 * @param c
	 * @param m
	 */
	public static String start3(int n, int gTyp, String[] list) {
		long l;
		long seed = SEED;
		String gen;
		String f = "";

		String folder = "F_ " + n + " _" + seed;
		Generator g = new Generator(gTyp, seed, n);

		if (gTyp == 0) {
			gen = "LAPLACE";
		} else if (gTyp == 1) {
			gen = "KDNF";
		} else if (gTyp == 2) {
			gen = "REC";
			int paramFunction = 3;
			g.setcF(paramFunction);
			g.c = 0.15;

		} else {
			gen = "REC";
			int paramFunction = 2;
			g.setcF(paramFunction);
			g.c = 0.3;
			g.m = 3;
		}
		l = System.currentTimeMillis();

		ArrayList<byte[]> b2 = g.generate();
		if (b2.isEmpty()) {
			return "c(f)";
		}
		if (b2.size() == 1 && b2.get(0)[0] == 3) {
			return "c(v)";
		}
//		boolean comma1 = false;
//		boolean comma2 = false;
//		f = "or(";
//		for (int i = 0; i < b2.size(); i++) {
//
//			f += "and(";
//			for (int j = 0; j < b2.get(0).length; j++) {
//				if (b2.get(i)[j] != 2) {
//					if (comma1) {
//						f += ",";
//					}
//					if (b2.get(i)[j] == 1) {
//						comma1 = true;
//						f += list[j];// list[j]
//					} else {
//						comma1 = true;
//						f += "neg(" + list[j] + ")";// list[j]
//					}
//				}
//			}
//			f += ")";
//			comma1 = false;
//			if (i + 1 < b2.size()) {
//				f += ",";
//			}
//		}
//		f += ")";
		
		return getFormula(b2,list);
	}

	public static String getFormula(ArrayList<byte[]> list1,String[] vars) {
		ArrayList<String> list2 = new ArrayList<String>();
		for (int i = 0; i < list1.size(); i++) {
			ArrayList<String> list3 = new ArrayList<String>();
			for (int j = 0; j < list1.get(i).length; j++) {
				if (list1.get(i)[j] == 1) {
					list3.add(vars[j]);
				} else if(list1.get(i)[j] == 0){
					list3.add("neg(" + vars[j] + ")");
				}

				
			}
			
			list2.add(getTree("and(",list3));
		}
		return getTree("or(",list2);
	}

	private static String getTree(String operator, ArrayList<String> list) {

		ArrayList<String> newList = new ArrayList<String>();
		if (list.size() == 0){
			if(list.isEmpty())System.out.println("ööööööö\n\n\nöööööööööööööööööööööööööööööööööööööööööö");
			return "error";
		
		}
		for (int i = 0; i < list.size(); i += 2) {
			if (i + 1 == list.size()) {
				newList.add(list.get(i));
			} else {
				newList.add(operator + list.get(i) + "," + list.get(i + 1)
						+ ")");
			}
		}
		if (newList.size() > 1) {
			return getTree(operator, newList);
		} else {
			return newList.get(0);
		}

	}

	public static void setParam(Generator g) {
		double maxC = 0.3;
		double minC = 0.005;

		if (g.generatorTyp == 1) {
			g.c = minC + Math.random() * maxC;
			g.m = ((maxC - g.c + minC) / (g.n + 1)) * Math.random();

			// System.out.println(g.c + " "+g.n+" " + g.m);
		} else {
			g.c = 0;
			g.m = 0;
		}
	}

	public static boolean write(String file, String folder, String name,
			String txt) {
		File f = new File(file + folder);
		f.mkdir();
		FileWriter writer;
		try {
			writer = new FileWriter(file + folder + "/" + name + ".txt", true);
			writer.write(txt);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
