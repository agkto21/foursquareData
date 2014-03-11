package dataIO;

public class DatabaseManager {
	
	private String driver = "jdbc:mysql://127.0.0.1:3306/flickr";
	private String user = "root";
	private String password = "";
	
	private void connectMySQL()
	{
		try {
			Class.forName(driver);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
