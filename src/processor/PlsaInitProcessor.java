package processor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entity.CheckIn;
import entity.Combination;
import entity.FoursquareLocation;
import entity.Sequence;

public class PlsaInitProcessor extends ProcessHandler{

	private List<Sequence> sequenceList;
	private String formerUser = null;
	private Date formerDate = null;
	
	private List<FoursquareLocation> locationList;
	private List<Date> timeList;
	private List<CheckIn> checkInList;
	private static Sequence sequence;
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	
	public static Map<Combination, Integer> userLocationMatrix = new HashMap<Combination, Integer>();
//	private static int[][] userLocationMatrix = new int[10000][65000];
	public static List<String> uniqUserList = new LinkedList<String>();
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		if (checkIn.getLocation().getFoursquareLocation().getId().equals("EMPTY"))
			return ;
		if (userRemain(checkIn) && dateRemain(checkIn))
		{
			locationList.add(checkIn.getLocation().getFoursquareLocation());
			timeList.add(checkIn.getDate());
			checkInList.add(checkIn);
		}
		else
		{
			if (formerUser!=null && formerDate!=null && locationList!=null)
			{
				this.sortCheckInList(checkInList);
				sequence = new Sequence(formerUser,formerDate,checkInList.size(),checkInList);
				
				//完成了sequence的建立, 可以进一步处理
				if (sequence.getLocationCount()>=3)
				{
					String userId = sequence.getUser();
					if (!uniqUserList.contains(userId))
						uniqUserList.add(userId);
					for (int i=0;i<sequence.getLocationCount();i++)
					{
						int locationIndex = PlsaVocProcessor.uniqLocationList.indexOf(sequence.getChechInList().get(i).getLocation().getFoursquareLocation().getId());
						Combination c = new Combination(0,uniqUserList.size()-1,locationIndex,0);
						if (userLocationMatrix.get(c) == null)
							userLocationMatrix.put(c, 1);
						else
							userLocationMatrix.put(c, userLocationMatrix.get(c)+1);
//						userLocationMatrix[uniqUserList.size()-1][locationIndex]++;
					}
				}
			//	sequenceList.add(sequence);
//				printSequence(sequence);
//				calMatrix(sequence);
			}
			formerDate = checkIn.getDate();
			formerUser = checkIn.getUser().getId();
			
			locationList = new LinkedList<FoursquareLocation>();
			timeList = new LinkedList<Date>();
			checkInList = new LinkedList<CheckIn>();
			
			locationList.add(checkIn.getLocation().getFoursquareLocation());
			timeList.add(checkIn.getDate());
			checkInList.add(checkIn);
		}
	}
	
	private boolean userRemain(CheckIn checkIn)
	{
		if (formerUser != null && checkIn.getUser().getId().equals(formerUser))
			return true;
		else
			return false;
	}
	
	private boolean dateRemain(CheckIn checkIn)
	{
		if (formerDate != null && dateFormatter.format(checkIn.getDate()).equals(dateFormatter.format(formerDate)))
			return true;
		else
			return false;
	}
	
	private void sortCheckInList(List<CheckIn> checkInList)
	{
		Collections.sort(checkInList, new Comparator<CheckIn>(){
			@Override
			public int compare(CheckIn o1,CheckIn o2) 
			{
				boolean value = o1.getDate().after(o2.getDate());
				return (value ? 1 : -1);
			}
		});
	}
	
	
	public Map<Combination, Integer> toMatrix()
	{
		return userLocationMatrix;
	}
	public List<String> toUserList()
	{
		return uniqUserList;
	}
}
