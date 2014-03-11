package dataModel;

public class DetailedLocation {
	
	private String country;
	private String admin_level_1;
	private String admin_level_2;
	
	public String getAdmin_level_1() {
		return admin_level_1;
	}
	public void setAdmin_level_1(String admin_level_1) {
		this.admin_level_1 = admin_level_1;
	}
	public String getAdmin_level_2() {
		return admin_level_2;
	}
	public void setAdmin_level_2(String admin_level_2) {
		this.admin_level_2 = admin_level_2;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return admin_level_1;
	}
	public void setCity(String city) {
		this.admin_level_1 = city;
	}
	public DetailedLocation(String country, String admin_level_1,
			String admin_level_2) {
		super();
		this.country = country;
		this.admin_level_1 = admin_level_1;
		this.admin_level_2 = admin_level_2;
	}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof DetailedLocation))
			return false;
		DetailedLocation detailedLocation = (DetailedLocation)o;
		return (detailedLocation.getCountry().equals(country) &&
				detailedLocation.getAdmin_level_1().equals(admin_level_1) &&
				detailedLocation.getAdmin_level_2().equals(admin_level_2));
	}
	
	public int hashCode()
	{
		int result = 17;
		int mid = 0;
		
		mid = this.country.hashCode();
		result = result * 31 + mid;
		mid = this.admin_level_1.hashCode();
		result = result * 31 + mid;
		mid = this.admin_level_2.hashCode();
		result = result * 31 + mid;
		
		return result;
	}
	
}
