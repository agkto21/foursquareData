package processor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import dataIO.WriteProcess;

import entity.CheckIn;
import entity.Sequence;
import entity.FoursquareCategory;
import entity.HierarchicalCategory;

public class ClassifierNew extends SequenceAnalyzer{

	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	public static Map<FoursquareCategory,Integer> classMap = new HashMap<FoursquareCategory,Integer>();
	
	@Override
	public void processSequence(Sequence sequence)
	{
		String content = sequence.getUser() + "\t" + dateFormatter.format(sequence.getDate()) + "\t" + sequence.getLocationCount() + "\t";
		String checkInList = "";
		for (int i=0;i<sequence.getChechInList().size();i++)
		{
			CheckIn ci = sequence.getChechInList().get(i);
			checkInList += "{" + ci.getLocation().getFoursquareLocation().getName() + "|";
			for (FoursquareCategory fc : ci.getLocation().getFoursquareLocation().getCategorys())
			{
				checkInList += HierarchicalCategory.hCategories[classMap.get(fc)];
			}
			checkInList += "|" + timeFormatter.format(ci.getDate()) + "}";
			if (i != sequence.getLocationCount()-1)
				checkInList += "\t->\t";
		}
		content += checkInList;
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FLSequenceByDay_newCategory.txt", content);
	}
	
	public static void readConfigFile(String lineString)
	{
		String[] result;
		FoursquareCategory fc;
		result = lineString.split(":");
		fc = new FoursquareCategory(result[0],result[1]);
		classMap.put(fc, Integer.valueOf(result[2]));
	}
}
