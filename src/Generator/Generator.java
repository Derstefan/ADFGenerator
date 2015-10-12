package Generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import QuineMcCluskey.QMC;

public class Generator {

	private long nextSeed;
	public static final int LINEAR = 0;
	public static final int LOGARITHM = 1;
	public static final int ROOT = 2;
	

	public static final int LAPLACE_GENERATOR = 0;
	public static final int RECURSIVE_GENERATOR = 1;
	public static final int KDNF_GENERATOR = 2;

	public int generatorTyp;
	public int conditionFunction;
	public int n;
	// parameter for
	public double c;
	public double m;
/**
 * Konstruktor
 * @param generatorTyp
 * @param seed
 * @param n
 */
	public Generator(int generatorTyp, long seed, int n) {
		this.generatorTyp = generatorTyp;
		nextSeed = seed;
		this.n = n;

	}

	public double rand() {
		Random generator = new Random(nextSeed);
		double num = generator.nextDouble();
		nextSeed = Double.doubleToRawLongBits(num);
		// System.out.println(nextSeed);
		return num;
	}

	public ArrayList<byte[]> generate() {
		if (generatorTyp == 0) {
			return laPlaceGen();
		} else if (generatorTyp == 1) {
			return kdnf();
		} else if (generatorTyp == 2) {
			return STFGen();
		} else {
			return STFGen();
		}


	}
	/**
	 * 
	 * @param cf
	 */
	public void setcF(int cf) {
		this.conditionFunction = cf;
	}

	public ArrayList<Boolean> fillBitStreamSave(long n) {
		ArrayList<Boolean> bs = new ArrayList<Boolean>();
		for (long i = 0; i < n; i++) {

			double d = rand();
			if (d < 0.5) {
				bs.add(true);
			} else {
				bs.add(false);
			}
		}
		return bs;
	}

	public ArrayList<Boolean> fillBitStream(long n) {
		ArrayList<Boolean> bs = new ArrayList<Boolean>();
		String stream;
		for (long i = 53; i < n; i += 54) {
			stream = Long.toBinaryString(Double.doubleToRawLongBits(rand()))
					.substring(8);// wegen exponent sind die ersten bits nicht
									// zufällig
			for (int j = 0; j < stream.length(); j++) {
				if (stream.charAt(j) == '1')
					bs.add(true);
				if (stream.charAt(j) == '0')
					bs.add(false);
			}
			// System.out.println(bs.size());

		}
		stream = Long.toBinaryString(Double.doubleToRawLongBits(rand()))
				.substring(8);
		for (int i = 0; i < n % 54; i++) {

			if (stream.charAt(i) == '1')
				bs.add(true);
			if (stream.charAt(i) == '0')
				bs.add(false);

		}

		stream = Long.toBinaryString(Double.doubleToRawLongBits(rand()))
				.substring(8);

		return bs;
	}

	public static void out(ArrayList<Boolean> b) {

		for (int i = 0; i < b.size(); i++) {
			System.out.println(b.get(i));
		}
	}

	public ArrayList<byte[]> kdnf() {
		ArrayList<byte[]> f = new ArrayList<byte[]>();
		int d = n; // anzahl variablen
		int k = (int) (((n) * rand()) + 1);// max anzahl der variablen

		int r = (int) (((Math.pow(2, (n - 1)) + 1)) * rand());// anzahl d
																// klauseln
		if (r == 0) {
			return QMC.fa();
		}
		QMC qmc = new QMC(d);
		for (int i = 0; i < r; i++) {
			byte[] b = fillclause(k, d);
			if (QMC.countDCares(b) == b.length) {
				return QMC.tr();
			} else {
				f.add(b);
			}
		}
		qmc.formula = f;
		// System.out.println(outPut(f));

		return qmc.start();
	}

	private byte[] fillclause(int k, int d) {
		byte[] b = new byte[d];
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < d + 1; i++) {
			set.add(i);
		}
		for (int i = 0; i < (d - k); i++) {
			set.remove((int) (set.size() * rand()));
		}
		for (int i = 0; i < d; i++) {
			if (set.contains(i)) {
				b[i] = (byte) (rand() * 2);
			} else {
				b[i] = 2;
			}
		}
		return b;
	}

	public ArrayList<byte[]> laPlaceGen() {

		return minimize(n, fillBitStream((long) Math.pow(2, n)));
	}

	public ArrayList<byte[]> STFGen() {

		QMC qmc = new QMC(n);
		String f = recursiv(n, "");
		if (f == "f" && f != "") {
			// System.out.println("false");
			return QMC.fa();
		} else if (f == "t") {

			return QMC.tr();
			// System.out.println("true");
		} else {
			int i = 0;
			String s = "";
			while (i < f.length()) {

				if (f.charAt(i) != ',') {
					s += f.charAt(i);
				} else {
					while (s.length() < n) {
						s += "2";
					}
					qmc.add(s);

					s = "";
				}
				i++;

			}
		}
		return qmc.start();
	}

	public String recursiv(int n, String f) {
		double d = rand();

		if (n >= 1) {
			if (t(n) >= 0.5) {
				System.out
						.println("-----------------------------------------------error------------------------------------------------");
			}
			if (d < t(n)) {
				if (f == "") {

					return "t";

				} else {
					return f + ",";
				}

			} else if (d > 1 - f(n)) {
				if (f == "") {

					return "f";

				} else {
					return "";
				}
			} else {
				return recursiv(n - 1, f + "1") + recursiv(n - 1, f + "0");
			}
		} else {
			if (d > 1 / 2) {
				return f + ",";
			} else {
				return "";
			}
		}
	}

	public ArrayList<byte[]> minimize(int n, ArrayList<Boolean> f) {
		QMC qmc = new QMC(n);
		if (Math.pow(2, n) != f.size()) {
			System.out.println("eroorrr");
			return null;
		}
		for (int l = 0; l < f.size(); l++) {
			String s = "";
			if (f.get(l) == true) {

				if (Integer.toBinaryString(l).length() < n) {
					int d = n - Integer.toBinaryString(l).length();

					for (int i = 0; i < d; i++) {
						s = s + "0";
						// System.out.println(i);
					}

				}
				// System.out.println(s + Integer.toBinaryString(l));
				qmc.add(s + Integer.toBinaryString(l));

			}
		}
		return qmc.start();
	}

	private double t(int n) {

		//

		switch (this.conditionFunction) {
		case 0:
			return c + (this.n - n) * (m - c) / this.n;
		case 1:
			return Math.log(((Math.exp(m) - Math.exp(c)) * (this.n - n))
					/ this.n + Math.exp(c));
		case 2:
			//System.out.println((m-c)*Math.pow(((double)(this.n-n))/((double)this.n), 1/2) + c);
			return (c)*Math.pow(((double)(this.n-n))/((double)this.n), 1/m);
		
		default:
			return c;
		}

	}

	private double f(int n) {

		// diese funktion sollte noch geändert werden
		return t(n);
	}

	public int countVars(ArrayList<byte[]> f) {
		if (!f.isEmpty()) {
			if (f.get(0)[0] == 3) {
				return 0;
			}
			int m = f.get(0).length;
			byte[] b = new byte[m];
			for (int i = 0; i < m; i++) {
				b[i] = 2;
			}
			for (int i = 0; i < f.size(); i++) {
				for (int j = 0; j < f.get(i).length; j++) {
					if (f.get(i)[j] != 2 && b[j] != 1 && b[j] != 0) {
						b[j] = f.get(i)[j];
					}
				}
			}
			return m - QMC.countDCares(b);
		}
		return 0;
	}

	public double[] countclauseSize(ArrayList<byte[]> f) {

		double[] list = new double[n];
		for (int i = 0; i < list.length; i++) {
			list[i] = 0;

		}
		if (f.isEmpty()) {
			return list;
		} else if (f.get(0)[0] == 3) {
			return list;
		}

		for (int i = 0; i < f.size(); i++) {
			list[list.length - 1 - QMC.countDCares(f.get(i))]++;
		}
		int c = f.size();
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i] / c;
		}

		return list;

	}

	public double[] countVarDepend(ArrayList<byte[]> f) {
		double[] list = new double[n];
		for (int i = 0; i < list.length; i++) {
			list[i] = 0;

		}
		if (f.isEmpty()) {

			return list;
		} else if (f.get(0)[0] == 3) {
			return list;
		}

		for (int i = 0; i < f.size(); i++) {
			for (int j = 0; j < f.get(0).length; j++) {
				if (f.get(i)[j] != 2) {
					list[j]++;
				}
			}

		}

		int c = f.size();
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i] / c;

		}

		return list;
	}

	public String outPut(ArrayList<byte[]> f) {
		if (f.isEmpty())
			return "false";
		if (f.get(0)[0] == 3) {

			return "true";
		}

		String s = "";
		for (int i = 0; i < f.size(); i++) {

			s += QMC.bOut(f.get(i)) + "\n";

		}
		return s;
	}

	public static String arrayToString(double[] list) {
		if (list == null)
			return "";
		String s = "";
		for (int j = 0; j < list.length; j++) {
			s += "," + list[j];
		}

		return s.substring(1);
	}
	
	
	
	
	
	
	
	
	
	

}
