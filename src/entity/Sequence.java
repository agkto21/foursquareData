package entity;

import java.util.Date;
import java.util.List;

public class Sequence {

	private String user;
	private Date date;
	private int locationCount;
	private List<CheckIn> chechInList;
	public Sequence(String user, Date date, int locationCount,
			List<CheckIn> chechInList) {
		super();
		this.user = user;
		this.date = date;
		this.locationCount = locationCount;
		this.chechInList = chechInList;
	}
	public List<CheckIn> getChechInList() {
		return chechInList;
	}
	public void setChechInList(List<CheckIn> chechInList) {
		this.chechInList = chechInList;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getLocationCount() {
		return locationCount;
	}
	public void setLocationCount(int locationCount) {
		this.locationCount = locationCount;
	}
	
	
}
