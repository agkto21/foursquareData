package entity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoursquareLocation {
	
	private String id;
	private String name;
	private List<FoursquareCategory> categorys;
	private boolean verified;
	
	static Pattern pattern1 = Pattern.compile("\\{(.*)}");
	
	public static FoursquareLocation getInstanceFromString(String str)
	{
		Matcher matcher = pattern1.matcher(str);
		if (matcher.find())
		{	
//			System.out.println(matcher.group(1));
			String [] twoPart = new String[2];
			String [] location = new String[2];
			String [] category = new String[2];
			twoPart = matcher.group(1).split("\\|");
			if (twoPart.length == 0)
				return new FoursquareLocation("EMPTY","",new LinkedList<FoursquareCategory>(),false);
			if (twoPart.length == 1)
				return new FoursquareLocation("EMPTY","",new LinkedList<FoursquareCategory>(),false);
			location = twoPart[0].split(":");
			category = twoPart[1].split(":");
			if (location.length != 2 || category.length != 2)
				return new FoursquareLocation("EMPTY","",new LinkedList<FoursquareCategory>(),false);
			
//			System.out.println(location[0] + "," + location[1] + "|" + category[0] + "," + category[1]);
			List<FoursquareCategory> list = new LinkedList<FoursquareCategory>();
			list.add(new FoursquareCategory(category[0],category[1]));
			return new FoursquareLocation(location[0],location[1],list,true);
			
		}
		return null;
	}
	
	public FoursquareLocation(String id, String name,
			List<FoursquareCategory> categorys, boolean veryfied) {
		super();
		this.id = id;
		this.name = name;
		this.categorys = categorys;
		this.verified = veryfied;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FoursquareCategory> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<FoursquareCategory> categorys) {
		this.categorys = categorys;
	}
	public boolean isVeryfied() {
		return verified;
	}
	public void setVeryfied(boolean veryfied) {
		this.verified = veryfied;
	}

}
