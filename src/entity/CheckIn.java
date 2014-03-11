package entity;

import java.util.Date;

public class CheckIn {
	
	private User user;
	private Location location;
	private Coordinate coordinate;
	private Date date;
	private Tweet tweet;
	
	public Tweet getTweet() {
		return tweet;
	}
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public CheckIn(User user, Coordinate coordinate, Date date, Location location, Tweet tweet) 
	{
		super();
		this.user = user;
		this.location = location;
		this.coordinate = coordinate;
		this.date = date;
		this.tweet = tweet;
	}
	
	
	
}
