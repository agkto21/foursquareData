package processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dataIO.WriteProcess;

import entity.CheckIn;

public class UserCounter extends ProcessHandler{

	private Map<Integer, List<String>> userPath;
	private int formerInt = -1;
	private List<String> locationList;
	
	public UserCounter()
	{
		this.userPath = new HashMap<Integer,List<String>>();
	}
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
//		System.out.println(this.processCount);
		int nowInt = Integer.valueOf(checkIn.getUser().getId());
		if (nowInt != formerInt)
		{
			if (formerInt != -1)
				userPath.put(formerInt, locationList);
			
			locationList = new LinkedList<String>();
			formerInt = nowInt; 

			
		}
		
		locationList.add(checkIn.getLocation().getId());
	}
	
	@Override
	public void output()
	{
		int c = 0;
		
		List<String> path;
		String pathOutput;
		for (Iterator<Integer> itr = userPath.keySet().iterator(); itr.hasNext();)
		{
			int user = itr.next();
			path = userPath.get(user);
			
			if (path.size() < 10)
				continue;
			c++;
//			
//			pathOutput = path.size() + "\t";
//			for (Iterator<String> it = path.iterator(); it.hasNext();)
//			{
//				pathOutput += it.next() + "\t";
//			}
//			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_userList.txt", ""+user);
//			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_userPath.txt", 
//					user + "\t" + pathOutput );
		}
		System.out.println(c);
	}
}
