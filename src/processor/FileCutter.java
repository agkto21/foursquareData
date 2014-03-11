package processor;

import dataIO.WriteProcess;
import entity.CheckIn;

public class FileCutter extends ProcessHandler {
	
	private static int lineLimit = 500000;
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_cutted.txt", lineString);
		if (this.processCount > lineLimit)
			this.status = false;
		System.out.println(this.processCount);
	}

}
