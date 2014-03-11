package dataModel;

import java.util.LinkedList;

import entity.CheckIn;

public class CheckInInfo {

	private LinkedList<CheckIn> checkInList;
	
	public CheckInInfo()
	{
		this.checkInList = new LinkedList<CheckIn>();
	}

	public LinkedList<CheckIn> getCheckInList() {
		return checkInList;
	}

	public void setCheckInList(LinkedList<CheckIn> checkInList) {
		this.checkInList = checkInList;
	}
	
	
}
