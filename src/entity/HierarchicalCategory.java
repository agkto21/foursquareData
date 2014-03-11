package entity;

public class HierarchicalCategory {
	
	private int id;
	private String name;
	
	
	public HierarchicalCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public static String[] hCategories = {"Residence",
										  "Office",
										  "Bar",
										  "Food",
										  "Coffee&Tea",
										  "Shopping(Store)",
										  "Service",
										  "Transport",
										  "Scenic",
										  "Arts",
										  "Entertainment",
										  "Outdoors",
										  "Sports&Excecise",
										  "Hotel",
										  "FunctionalBuilding&Place",
										  "School",
										  "Religion",
										  "Medical",
										  "Others"};
	
}
