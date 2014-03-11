package dataIO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import entity.*;

public class ReadProcess {
	
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public static CheckIn readLine(String line)
	{
		String para[] = new String [9];
		para = line.split("\t");
		if (para.length < 6)
			return null;
//		System.out.println(para.length);
	
		//para[0] => user Id
		User user = new User(para[0]);
		
		//para[1],para[5] => tweet id, content 
		Tweet tweet = new Tweet(para[1], para[5]);
		
		//para[2],para[3] ==> latitude, longitude
		Coordinate coordinate = new Coordinate(Double.valueOf(para[2]),Double.valueOf(para[3]));
		
		//para[4] => time
		Date date = null;
		try {
			date = formatter.parse((para[4]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//para[6] ==> location, para[length-1]
		Location location;
		FoursquareLocation fl = FoursquareLocation.getInstanceFromString(para[para.length-1]);
//		if (para.length == 7)
//			location = new Location(para[6],fl);
//		else
			location = new Location("FAKE_ID",fl);
		
		CheckIn checkIn = new CheckIn(user, coordinate, date, location, tweet);
		
		return checkIn;
		
	}
	
	public static CheckIn readLine_flickr(String line)
	{
		String para[] = new String [4];
		para = line.split(" ");
		if (para.length < 4)
			return null;
		
		//para[0] => user Id
		User user = new User(para[0]);
				
		//para[1],para[2] ==> latitude, longitude
		Coordinate coordinate = new Coordinate(Double.valueOf(para[1]),Double.valueOf(para[2]));
		
		CheckIn checkIn = new CheckIn(user, coordinate, null, null, null);
		
		return checkIn;
	}
	
	public static Sequence readLine_sequence(String line)
	{
		String para[];
		String locations[];
		para = line.split("\t");
		if (para.length < 4)
			return null;
		String user = para[0];
		Date date = null;
		try {
			date = dateFormatter.parse(para[1]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int locationCount = Integer.valueOf(para[2]);
		locations = para[3].split("\t->\t");
		List<CheckIn> list = new LinkedList<CheckIn>();
		Sequence sequence = new Sequence(user,date,locationCount,new LinkedList<CheckIn>());
		return sequence;
		
	}
}
