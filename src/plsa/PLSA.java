package plsa;

public class PLSA {
	
	public static void main(String[] args) {
		
		ProbabilisticLSA plsa = new ProbabilisticLSA();
		//the file is not used, the hard coded data is used instead, but file name should be valid,
		//just replace the name by something valid.
		plsa.doPLSA("F:\\[A]ACT-work\\Dataset\\icwsm_2011\\PLSA\\test.txt", 2, 60);
        System.out.println("end PLSA");
    }

}
