package entity;

public class Combination {
	
	//0-user/location
	//1-user/topic
	//2-topic/location
	//3-user/location/topic
	private int type;
	
	private int x_key;
	private int y_key;
	private int z_key;
	
	public Combination()
	{
		super();
		this.type = -1;
		x_key = -1;
		y_key = -1;
		z_key = -1;
	}
	
	public Combination(int type, int xKey, int yKey, int zKey) {
		super();
		this.type = type;
		x_key = xKey;
		y_key = yKey;
		z_key = zKey;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getX_key() {
		return x_key;
	}
	public void setX_key(int xKey) {
		x_key = xKey;
	}
	public int getY_key() {
		return y_key;
	}
	public void setY_key(int yKey) {
		y_key = yKey;
	}
	public int getZ_key() {
		return z_key;
	}
	public void setZ_key(int zKey) {
		z_key = zKey;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Combination))
			return false;
		Combination combination = (Combination)o;
		return (combination.getType() == type &&
				combination.getX_key() == x_key &&
				combination.getY_key() == y_key &&
				combination.getZ_key() == z_key);
	}
	
	@Override
	public int hashCode()
	{
		int result = 17;
		int mid = 0;
		
		mid = Integer.valueOf(this.type).hashCode();
		result = result * 31 + mid;
		mid = Integer.valueOf(this.x_key).hashCode();
		result = result * 31 + mid;
		mid = Integer.valueOf(this.y_key).hashCode();
		result = result * 31 + mid;
		mid = Integer.valueOf(this.z_key).hashCode();
		result = result * 31 + mid;
		
		return result;
	}
}
