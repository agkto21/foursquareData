package processor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import entity.CheckIn;

public class PlsaVocProcessor extends ProcessHandler{
	
	public static Set<String> uniqLocationSet = new HashSet<String>();
	public static List<String> uniqLocationList = new LinkedList<String>();
	
	/*创建字典的过程*/
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		String locationId = checkIn.getLocation().getFoursquareLocation().getId();
		if (!uniqLocationSet.contains(locationId)) {
			uniqLocationSet.add(locationId);
        }
		
	}
	
	@Override
	public void output()
	{
		uniqLocationList = new LinkedList<String> (uniqLocationSet);
		System.out.println(uniqLocationList.size());
	}

}
