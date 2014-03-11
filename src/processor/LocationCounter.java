package processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataIO.WriteProcess;

import entity.CheckIn;
import entity.FoursquareCategory;
import entity.WeeklyStatistic;

public class LocationCounter extends ProcessHandler{

	private Map<String, Integer> locationCount;
	private Map<FoursquareCategory, Integer> categoryCount; 
	private Map<FoursquareCategory, WeeklyStatistic> dayCount;
	
	public LocationCounter()
	{
		this.locationCount = new HashMap<String, Integer>();
		this.categoryCount = new HashMap<FoursquareCategory, Integer>();
		this.dayCount = new HashMap<FoursquareCategory, WeeklyStatistic>();
	}
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
//		String locationName = LocationRecognizer.filterPlaceByPtn(checkIn.getTweet().getContent());
//		if (locationCount.get(locationName) == null)
//			locationCount.put(locationName, 1);
//		else
//		{
//			int count = locationCount.get(locationName);
//			locationCount.put(locationName, count+1);		
//		}
		
		if (checkIn.getLocation().getFoursquareLocation().getId().equals("EMPTY"))
			return ;
		
		List<FoursquareCategory> catlist = checkIn.getLocation().getFoursquareLocation().getCategorys();
		FoursquareCategory category = catlist.get(0);
		if (dayCount.get(category) == null)
		{
			WeeklyStatistic ws = new WeeklyStatistic();
			ws.addDayCount(checkIn.getDate());
			dayCount.put(category, ws);
		}
		else
		{
			WeeklyStatistic ws = dayCount.get(category);
			ws.addDayCount(checkIn.getDate());
			dayCount.put(category, ws);		
		}
		
	}
	
	@Override
	public void output()
	{
		ArrayList<Entry<FoursquareCategory, WeeklyStatistic>> list = new ArrayList<Entry<FoursquareCategory, WeeklyStatistic>>(dayCount.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<FoursquareCategory,WeeklyStatistic>>(){
					@Override
					public int compare(Entry<FoursquareCategory, WeeklyStatistic> o1,Entry<FoursquareCategory, WeeklyStatistic> o2) 
					{
						int value1 = o1.getValue().getSum();
						int value2 = o2.getValue().getSum();
						return (value2 < value1 ? -1 : (value2 == value1 ? 0 : 1));
					}
				});
		for(Entry<FoursquareCategory, WeeklyStatistic> en : list)
		{
			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_CategoryCount_h.txt", 
					en.getKey().getId() + ":" + en.getKey().getName()/* + "\t" + en.getValue().outputString()*/ );
		}
	}
}
