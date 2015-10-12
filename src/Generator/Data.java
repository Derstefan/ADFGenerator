package Generator;

/**
 * Klasse zur verarbeitung von dateien
 * @author ich
 *
 */
public class Data {
	public static final int MODELS = 0;
	public static final int CALLS = 1;
	public static final int TIME = 2;
	public static final int TIME_SOLVING = 3;
	public static final int FIRST_MODEL = 4;
	public static final int UNSAT = 5;
	public static final int CPU_TIME = 6;

	private String sourceName;
	private boolean noError;
	private boolean satisfiable;
	
	public Data(String[][] data){
		this.data = data;
	}
	private String[][] data;
	private String[] adm = new String[7];
	private String[] cfi = new String[7];
	private String[] com = new String[7];
	private String[] grd = new String[7];
	private String[] mod = new String[7];
	
	private String[] nai = new String[7];
	private String[] prf = new String[7];
	private String[] sem = new String[7];
	private String[] stg = new String[7];
	private String[] stm = new String[7];
	
	/**
	 * erstellt ein doublearray
	 * @param values
	 * @return
	 */
	private double[] stringToDouble(String[] values){
		if(values== null)return null;
		double [] data = new double[values.length];
		
		for(int i = 0;i<values.length;i++){
			
			System.out.print(data[i]+ "   ");
			data[i] = Double.valueOf(values[i]);
			
		}
		System.out.println();
		return data;
	}
	
	
	
	/**
	 * gibtr die dateien aus
	 */
	public String toString(){
		
		String s = "";
		for(int i = 0;i<data.length;i++){
			s+="(" + data[i][0];
			for(int j = 1;j<data[0].length;j++){
				s+="," + data[i][j];
			}
			s+="),";
		}
		
		
		return s;
	}
	
	
}