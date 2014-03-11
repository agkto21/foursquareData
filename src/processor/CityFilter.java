package processor;

import dataIO.ReadProcess;
import dataIO.WriteProcess;
import entity.CheckIn;

public class CityFilter extends ProcessHandler{
	
	private static double lowerLatitude_NewYork = 40.42;
	private static double upperLatitude_NewYork = 41.16; 
	private static double lowLongitude_NewYork = -74.72; 
	private static double upperLongitude_NewYork = -73.24; 
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		if (filterNewYork(checkIn))
			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/flickr_metaData/MetaDataPlus/OUTPUT/flickr_NewYork.txt", lineString);
	//	System.out.println(this.processCount);
	}

	
	private boolean filterNewYork(CheckIn checkIn)
	{
		if (checkIn.getCoordinate().getLatitude()>lowerLatitude_NewYork && 
				checkIn.getCoordinate().getLatitude()<upperLatitude_NewYork &&
				checkIn.getCoordinate().getLongitude()>lowLongitude_NewYork && 
				checkIn.getCoordinate().getLongitude()<upperLongitude_NewYork)
			return true;
		else
			return false;
	}
}
