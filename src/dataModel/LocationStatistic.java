package dataModel;

import java.util.HashMap;
import java.util.Map;

public class LocationStatistic {

	private Map<DetailedLocation, Integer> locationCount;

	public Map<DetailedLocation, Integer> getLocationCount() {
		return locationCount;
	}

	public void setLocationCount(Map<DetailedLocation, Integer> locationCount) {
		this.locationCount = locationCount;
	}

	public LocationStatistic() {
		this.locationCount = new HashMap<DetailedLocation, Integer>();
	}
	
	public void putLocationIn(DetailedLocation dl)
	{
		if (this.locationCount.get(dl) == null)
			this.locationCount.put(dl, 1);
		else
		{
			int value = this.locationCount.get(dl);
			this.locationCount.put(dl, value+1);
		}
	}
}
