package processor;

import dataIO.WriteProcess;
import entity.CheckIn;
import java.util.regex.*;

public class LocationRecognizer extends ProcessHandler{

	static Pattern pattern1 = Pattern.compile("I'm at (.+) \\(.*\\)");
	static Pattern pattern2 = Pattern.compile("I'm at (.+) w/");
	static Pattern pattern3 = Pattern.compile("I'm at (.+) http:");
	static Pattern pattern4 = Pattern.compile("I'm at (.+)");
	static Pattern pattern5 = Pattern.compile("\\(@ (.+) w/");
	static Pattern pattern6 = Pattern.compile("\\(@ (.+)\\)");
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_Locations.txt", 
								filterPlaceByPtn(checkIn.getTweet().getContent()) + "\t\t//*\t" + 
								checkIn.getTweet().getContent());
	}
	
	public static String filterPlaceByPtn(String content)
	{
		Matcher matcher = pattern1.matcher(content);
		if (matcher.find())	return matcher.group(1);
		matcher = pattern2.matcher(content);
		if (matcher.find())	return matcher.group(1);
		matcher = pattern3.matcher(content);
		if (matcher.find())	return matcher.group(1);
		matcher = pattern4.matcher(content);
		if (matcher.find())	return matcher.group(1);
		matcher = pattern5.matcher(content);
		if (matcher.find())	return matcher.group(1);
		matcher = pattern6.matcher(content);
		if (matcher.find())	return matcher.group(1);
		return "NOT_FOUND";
	}
}
