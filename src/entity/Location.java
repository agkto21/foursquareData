package entity;

public class Location {

	private String id;
	private FoursquareLocation foursquareLocation;

	public FoursquareLocation getFoursquareLocation() {
		return foursquareLocation;
	}

	public void setFoursquareLocation(FoursquareLocation foursquareLocation) {
		this.foursquareLocation = foursquareLocation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location(String id, FoursquareLocation foursquareLocation) {
		super();
		this.id = id;
		this.foursquareLocation = foursquareLocation;
	}

}
