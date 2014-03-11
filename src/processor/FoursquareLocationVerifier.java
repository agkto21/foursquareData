package processor;

import java.util.LinkedList;
import java.util.List;

import dataIO.WriteProcess;
import dataModel.GeoQuery;
import entity.CheckIn;
import entity.FoursquareCategory;
import entity.FoursquareLocation;

public class FoursquareLocationVerifier extends ProcessHandler{
	
	
	private static final int sleepGap_short = 10;
	private static final int sleepTime_short = 500;
	private static int sleepCount_short = 0;
	private static final int sleepGap_long = 500;
	private static final int sleepTime_long  = 5000;
	private static int sleepCount_long  = 0;
	
	private static final int sleepTime_keyChange  = 5000;
	
	public static String[] foursquareKeySet = {"QJH3VZ5YQJT3HES5R1MTM0RFFFI2GP13MHDHHKKX3F0HJLJ4",
												"Z4F53HFVNFIFQ53UTEQDNTYWY54MSSSAVBYHXHRTRZP2BCYS",
												"YA554GY2EATC3JOYLIOJCOE4EUUN12MCQO2TDOTZUWPZY1F3",
												"51VC2N132G0IOLUDELYUI5ADYZGOARGUBZUJPUGZLNQLEJSJ",
												"HPOE2224PC3XDUH1E5PHLROG0HAF3QPWEZEM1XZMRKENLQHR",
												"OHDTA510T2WTIX4ICKD0ABECEAWRXEQGOWURI50CMGMN4PVD",
												"A1WJZAO0CDRQUGQNJTB1PYUEYPVUDLNZ5ZB0JSOCBSYVWOX4",
												"0T5A5NPMCZTN3EVIQIRXGGHAL2KATVLRSLLH5YSYF5Y3MDSY",
												"VCNUIIXQJUAOYNS3LQ5RHE2JBP2LDDLZCLWA3T2NK4FMBCM0",
												"3PWU10KE41X20F4GRM5QISTV2MXEEQRV4EVKRGW50JOCA5B0",
												"4JRMKAC0N1F1BU0CWH3ROZDQWVOF4CDZW1Z2LDKWK2TAYYA5",
												"OZHILYCXEA1SUZ5UJXA2ZECJNRULDRQUP5ZG3RNO0CIVXFZ2",
												"GBBPK4LWA5L4FT2FQG1LKS3QRUU4LPGTVKB2M2JJBSOY5YNB"};
	public static int foursquareKeyIndex = 0;
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		if (sleepCount_short == sleepGap_short)
		{
			sleepCount_short = 0;
			try {
				Thread.sleep(sleepTime_short);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (sleepCount_long == sleepGap_long)
		{
			sleepCount_long = 0;
			try {
				Thread.sleep(sleepTime_long);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FoursquareLocation fl = GeoQuery.verifyLocation(checkIn,foursquareKeySet[foursquareKeyIndex]);
		List<FoursquareCategory> fc = fl.getCategorys();
		
		if (fl.isVeryfied())
		{
			sleepCount_short++;
			sleepCount_long++;
		}
		System.out.println(fl.getId() + "\t" + fl.getName());
		
		int sleepChangeTime = sleepTime_keyChange;//∑≠±∂
		//foursquare api Ω˚÷π∑√Œ 
		while (fl.getId().equals("403_ERROR"))
		{
//			this.stopProcess();
			if (sleepChangeTime < 300000)
				sleepChangeTime = sleepChangeTime * 2;
			try {
				Thread.sleep(sleepChangeTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.changeKey();
			fl = GeoQuery.verifyLocation(checkIn,foursquareKeySet[foursquareKeyIndex]);
			System.out.println("key_changed:" + foursquareKeyIndex + "\t" + foursquareKeySet[foursquareKeyIndex] + "\t" +
								fl.getId() + "\t" + fl.getName());
		}
		String categories = "";
		for (int i = 0;i < fc.size();i++)
		{
			if (i == 0)
				categories += fc.get(i).getId() + ":" + fc.get(i).getName();
			else
				categories += "," + fc.get(i).getId() + ":" + fc.get(i).getName();
		}
		
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FoursquareLocations_NEW_2_segment_2_last14w.txt", 
				lineString + "\t{" + fl.getId() + ":" + fl.getName() + "|" + categories + "}");
	}
	
	private void changeKey()
	{
		foursquareKeyIndex = (foursquareKeyIndex + 1) % 13;
	}

}
