package processor;

import dataIO.ReadProcess;
import entity.CheckIn;

public class ProcessHandler {
	
	protected boolean status = true;
	protected String lineString;
	public int processCount = 0;
	public int skipLineCount = 0;
	

	public boolean isStatus() {
		return status;
	}

	public ProcessHandler()
	{
		
	}
	
	public void process(String lineString)
	{
		this.lineString = lineString;
		CheckIn checkIn = process2CheckIn(lineString);
		if (checkIn != null && !skipLine())
		{
			checkInProcess(checkIn);
			System.out.println(this.processCount);
		}
		increaseCount();
	}
	
	public CheckIn process2CheckIn(String lineString)
	{
		CheckIn checkIn;
		checkIn = ReadProcess.readLine(lineString);
//		checkIn = ReadProcess.readLine_flickr(lineString);
		return checkIn;
	}

	public void checkInProcess(CheckIn checkIn)
	{
		System.out.println("father");
	}
	
	protected void increaseCount()
	{
		processCount++;
	}
	
	public boolean skipLine()
	{
		if (this.processCount <= skipLineCount)
			return true;
		else
			return false;
	}
	
	protected void stopProcess()
	{
		this.status = false;
	}
	
	public void output()
	{
		
	}
}
