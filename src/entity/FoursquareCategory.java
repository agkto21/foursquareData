package entity;

import dataModel.DetailedLocation;

public class FoursquareCategory {
	
	private String id;
	private String name;
	
	public FoursquareCategory(String id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof FoursquareCategory))
			return false;
		FoursquareCategory foursquareCategory = (FoursquareCategory)o;
		return (foursquareCategory.getId().equals(id) &&
				foursquareCategory.getName().equals(name));
	}
	
	@Override
	public int hashCode()
	{
		int result = 17;
		int mid = 0;
		
		mid = this.id.hashCode();
		result = result * 31 + mid;
		mid = this.name.hashCode();
		result = result * 31 + mid;
		
		return result;
	}

}
