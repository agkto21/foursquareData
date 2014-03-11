package processor;

import dataIO.WriteProcess;
import entity.CheckIn;

public class TextFilter extends ProcessHandler{

	public int userCount;
	private String lastUser = "";
	
	public TextFilter()
	{
		this.userCount = 0;
	}
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		if (!filterPrefix("I'm at",checkIn.getTweet().getContent()))
			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_None.txt", lineString);
		if (!lastUser.equals(checkIn.getUser().getId()))
			userCount++;
		System.out.println(this.userCount + " " + this.processCount);
	}
	
	private boolean filterPrefix(String prefix, String source)
	{
		if (source.startsWith(prefix))
			return true;
		else
			return false;
	}
}
