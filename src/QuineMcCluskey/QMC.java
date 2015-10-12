package QuineMcCluskey;

import java.util.ArrayList;

import Generator.Generator;

public class QMC {
	/**
 	* Maximale Anzahl abhängiger Variablen
 	*/
	private int n;
	/**
	 * die zu minimierende Formel
	 */
	public ArrayList<byte[]> formula;
	/**
	 * listen für das Sortieren der Formeln bezüglich klauselgröße
	 */
	private ArrayList<ArrayList<byte[]>> clauseTyp; // aufteilung der mit i
													// verschiedenen

/**
 * Fügt neue Klausel der Formel hinzu
 * @param s String aus 0,1,2 mit länge n
 * @return gibt an, ob das Hinzufügt geglückt ist
 */
	public boolean add(String s) {
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++) {
			if (s.charAt(i) == '0' || s.charAt(i) == '1' || s.charAt(i) == '2') {
				b[i] = Byte.valueOf((s.substring(i, i + 1)));
				// System.out.println(b[i]);
			} else {
				return false;
			}
		}
		formula.add(b);
		return true;

	}

	/**
	 * Konstruktor für QMC
	 * @param n
	 */
	public QMC(int n) {
		this.n = n;
		this.formula = new ArrayList<byte[]>();
	}

	/**
	 * Startet die Minimierung
	 * @return die minierte Formel
	 */
	public ArrayList<byte[]> start() {
		if (formula.isEmpty() || formula.equals(fa())) {
			return fa();// false
		}
		if (formula.equals(tr())) {
			return tr();
		}
		// deleteDupl(formula);
		divideClauses();// sortiere größe nach größe der klauseln
		sortNumOfOnes();// sortiere nach anzahl der 1en,damit algorithmus
						// schneller arbeitet
		ArrayList<byte[]> b = new ArrayList<byte[]>();

		for (int i = 0; i < n; i++) {
			deleteDupl(clauseTyp.get(i));// lösche doppelte vorkommen
			int j = 0;
			while (j < clauseTyp.get(i).size() - 1) {
				for (int l = j + 1; l < clauseTyp.get(i).size()
						&& countOnes(clauseTyp.get(i).get(j)) < countOnes(clauseTyp
								.get(i).get(l)) + 2; l++) {
					// System.out.print(i + "," + j + "," + l + " :  "
					// + bOut(clauseTyp.get(i).get(j)) +
					// " + "//-------------------------------------
					// + bOut(clauseTyp.get(i).get(l)) + " --->");
					byte[] term = join(clauseTyp.get(i).get(j), clauseTyp
							.get(i).get(l));
					// System.out.println(bOut(term));//---------------------------------------

					// System.out.println();
					if (term == null) { // fall von keiner zusammenführung
						if (sameClause(clauseTyp.get(i).get(j),
								(clauseTyp.get(i).get(l)))) {
							clauseTyp.get(i).remove(l);
							l--;

						}
					} else {

						

						clauseTyp.get(i).remove(l);
						clauseTyp.get(i).remove(j);
						l--;
						j--;
						if (i + 1 != n) {
							clauseTyp.get(i + 1).add(term);
						} else {
							return tr();// true
						}
						break;
					}

				}
				j++;
			}
		}
		deleteImpl(); // implikationen löschen
		deleteRedundant();// redudanzen löschen A oder (nA und B) = A oder B
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < clauseTyp.get(i).size(); j++) {
				// System.out.println(bOut(clauseTyp.get(i).get(j)));//----------------------------------
				b.add(clauseTyp.get(i).get(j));
			}

		}

		return b;
	}
/**
 * Gibt eine Klausel als String zurück
 * @param b Klausel
 * @return String
 */
	public static String bOut(byte[] b) {
		String s = "";
		if (b == null) {
			return null;
		} else {
			for (int k = 0; k < b.length; k++) {
				s += b[k];
			}
		}
		return s;
	}

	/**
	 * Sortiert die Klauseln nach Größe
	 */
	private void divideClauses() {
		clauseTyp = new ArrayList<ArrayList<byte[]>>();
		for (int i = 0; i < n; i++) {
			clauseTyp.add(new ArrayList<byte[]>());
		}
		for (int i = 0; i < formula.size(); i++) {

			clauseTyp.get(countDCares(formula.get(i))).add(formula.get(i));
			// System.out.print(countDCares(formula.get(i)) + "-- ");
			// System.out.println(bOut(formula.get(i)));//------------------------------------

		}
	}

	/**
	 * zählt die don't cares für das Feststellen der Klauselgröße
	 * @param b Klausel
	 * @return Anzahl
	 */
	public static int countDCares(byte[] b) {
		int z = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] == 2)
				z++;
		}
		return z;
	}
/**
 * Zählt die Anzahl der Positiven Literale in der Klausel
 * @param b Klausel
 * @return Anzahl
 */
	private int countOnes(byte[] b) {
		int z = 0;
		for (int i = 0; i < b.length; i++) {
			if (b[i] == 1)
				z++;
		}
		return z;
	}

	/**
	 * Prüft, ob 2 Klauseln zusammengefügt werden können
	 * 
	 * @param k1
	 *            erste Klausel
	 * @param k2
	 *            zweite Klausel
	 * @return Resultat
	 */

	public byte[] join(byte[] k1, byte[] k2) {
		if (k1.length != k2.length || !sameLiterals(k1, k2)
				|| sameClause(k1, k2)) {
			return null;
		}
		int counter = 0;
		int index = 0;
		for (int i = 0; i < k1.length; i++) {
			if (k1[i] != k2[i]) {
				counter++;
				index = i;
				if (counter > 1) {
					return null;
				}
			}

		}
		byte[] kNew = new byte[k1.length];
		for (int i = 0; i < kNew.length; i++) {
			kNew[i] = k1[i];
		}
		if (counter == 1) {
			kNew[index] = 2;
		}

		return kNew;
	}

	private boolean sameLiterals(byte[] k1, byte[] k2) {
		if (k1.length != k2.length) {
			return false;
		}
		for (int i = 0; i < k1.length; i++) {
			if (k1[i] == 2 && k2[i] != 2 || k1[i] != 2 && k2[i] == 2) {
				return false;
			}
		}
		return true;
	}
/**
 * prüft, ob die klauseln identisch sind
 * @param k1 Klausel1
 * @param k2 Klausel2
 * @return ob oder ob nicht
 */
	private boolean sameClause(byte[] k1, byte[] k2) {
		for (int i = 0; i < k1.length; i++) {
			if (k1[i] != k2[i]) {
				return false;
			}
		}
		return true;
	}
/**
 * prüft, ob k1 k2 impliziert
 * @param k1 klausel 1
 * @param k2 klausel 2
 * @return boolean true, wenn k1 k2 impliziert
 */
	private boolean implicate(byte[] k1, byte[] k2) {
		for (int i = 0; i < k1.length; i++) {
			if (k1[i] != 2) {

				if (k1[i] != k2[i]) {
					return false;
				}
			}
		}
		return true;
	}
/**
 * Löscht implikationen in der clauselmenge bsp (A) --> (A und B) , somit ist(A und B überflüssig)
 */
	private void deleteImpl() {
		for (int i = n - 1; i > 0; i--) {
			for (int j = 0; j < clauseTyp.get(i).size(); j++) {
				for (int k = i - 1; k >= 0; k--) {
					for (int l = 0; l < clauseTyp.get(k).size(); l++) {
						// if(i==n-1){
						// byte[] bNew = new byte[clauseTyp.get(i).size()];
						// for(int m=0;m<clauseTyp.get(i).size();m++){
						//
						// }
						// }
						if (implicate(clauseTyp.get(i).get(j), clauseTyp.get(k)
								.get(l))) {
							clauseTyp.get(k).remove(l);
							l--;
						}
					}
				}
			}
		}
	}
/**
 * löscht redundanzen, indem Clauseln verkleinert werden bsp: (A) oder (nA und B) ---> (A) oder(B)
 */
	private void deleteRedundant() {
		for (int i = 0; i < clauseTyp.get(n - 1).size(); i++) {
			for (int j = 0; j < n - 2; j++) {
				for (int k = 0; k < clauseTyp.get(j).size(); k++) {
					int pos = getPosition(clauseTyp.get(n - 1).get(i));
					if (clauseTyp.get(n - 1).get(i)[pos] != clauseTyp.get(j)
							.get(k)[pos] && clauseTyp.get(j).get(k)[pos] != 2) {

						// System.out.print(bOut(clauseTyp.get(n -
						// 1).get(i))+"+"+bOut(clauseTyp.get(j).get(k))
						// +"--->");
						clauseTyp.get(j).get(k)[pos] = 2;
						clauseTyp.get(j + 1).add(clauseTyp.get(j).get(k));
						// System.out.println(bOut(clauseTyp.get(j).get(k)) +
						// "\n\n");
						clauseTyp.get(j).remove(k);
						k--;
					}
				}

			}
		}
	}
/**
 * sucht die position, welche keinen don't care zustand hat
 * @param b zu untersuchende clausel
 * @return position
 */
	private int getPosition(byte[] b) {
		if (this.countDCares(b) != b.length - 1) {
			return -1;
		}
		int j = 0;
		while (b[j] != 1 && b[j] != 0) {
			j++;
		}
		return j;
	}
/**
 * Löscht in der Clauselliste die duplikate
 * @param clauses zu untersuchende clauselliste
 */
	private void deleteDupl(ArrayList<byte[]> clauses) {

		for (int i = 0; i < clauses.size(); i++) {
			for (int j = i + 1; j < clauses.size(); j++) {
				if (sameClause(clauses.get(i), clauses.get(j))) {
					clauses.remove(j);
					j--;
					// System.out.println("lösche " + bOut(clauses.get(i)));
				}
			}
		}

	}
/**
 * Sortiert für jede Liste in ClauseTyp nach der anzahl der einsen
 */
	private void sortNumOfOnes() {
		for (int i = 0; i < clauseTyp.size(); i++) {

			ArrayList<ArrayList<byte[]>> list = new ArrayList<ArrayList<byte[]>>();
			for (int k = 0; k < n + 1; k++) {
				list.add(new ArrayList<byte[]>());
			}
			// System.out.println(clauseTyp.get(i).size());
			for (int j = 0; j < clauseTyp.get(i).size(); j++) {
				// System.out.println(i + "  - " + j);
				int number = countOnes(clauseTyp.get(i).get(j));
				// System.out.println(number);
				list.get(number).add(clauseTyp.get(i).get(j));
			}
			clauseTyp.get(i).clear();
			for (int k = 0; k < n; k++) {
				clauseTyp.get(i).addAll((list.get(k)));
			}

		}
	}

	/**
	 * gibt die triviale Formel wahr zurück
	 * 
	 * @return Formel
	 */
	public static ArrayList<byte[]> tr() {
		byte[] tr = new byte[1];
		tr[0] = 3;
		ArrayList<byte[]> t = new ArrayList<byte[]>();
		t.add(tr);
		return t;
	}

	/**
	 * gibt die triviale Formel falsch zurück
	 * 
	 * @return Formel
	 */
	public static ArrayList<byte[]> fa() {
		ArrayList<byte[]> f = new ArrayList<byte[]>();
		return f;
	}

}
