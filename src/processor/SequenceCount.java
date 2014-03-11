package processor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataIO.WriteProcess;

import entity.CheckIn;
import entity.FoursquareCategory;
import entity.FoursquareLocation;
import entity.HierarchicalCategory;
import entity.Sequence;
import entity.WeeklyStatistic;

public class SequenceCount extends ProcessHandler{
	
	public static final int r = 19;

	private List<Sequence> sequenceList;
	private String formerUser = null;
	private Date formerDate = null;
	
	private List<FoursquareLocation> locationList;
	private List<Date> timeList;
	private List<CheckIn> checkInList;
	private static Sequence sequence;
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	
	public static int[][] markovCount = new int[r][r];//range0-18
	public static int[] totalCount = new int[r];
	
	public static String pre_user = "";
	public static int count_user = 0;
	public static double[][] markovMatrix = new double[r][r];//range0-18
	
	public SequenceCount()
	{
		this.sequenceList = new LinkedList<Sequence>();
		this.locationList = null;
	}
	
	@Override
	public void checkInProcess(CheckIn checkIn)
	{
		if (checkIn.getLocation().getFoursquareLocation().getId().equals("EMPTY"))
			return ;
		if (userRemain(checkIn) && dateRemain(checkIn))
		{
			locationList.add(checkIn.getLocation().getFoursquareLocation());
			timeList.add(checkIn.getDate());
			checkInList.add(checkIn);
		}
		else
		{
			if (formerUser!=null && formerDate!=null && locationList!=null)
			{
				this.sortCheckInList(checkInList);
				sequence = new Sequence(formerUser,formerDate,checkInList.size(),checkInList);
				
				//完成了sequence的建立, 可以进一步处理
			//	sequenceList.add(sequence);
//				printSequence(sequence);
				calMatrix(sequence);
			}
			formerDate = checkIn.getDate();
			formerUser = checkIn.getUser().getId();
			
			locationList = new LinkedList<FoursquareLocation>();
			timeList = new LinkedList<Date>();
			checkInList = new LinkedList<CheckIn>();
			
			locationList.add(checkIn.getLocation().getFoursquareLocation());
			timeList.add(checkIn.getDate());
			checkInList.add(checkIn);
		}
	}
	
	private boolean userRemain(CheckIn checkIn)
	{
		if (formerUser != null && checkIn.getUser().getId().equals(formerUser))
			return true;
		else
			return false;
	}
	
	private boolean dateRemain(CheckIn checkIn)
	{
		if (formerDate != null && dateFormatter.format(checkIn.getDate()).equals(dateFormatter.format(formerDate)))
			return true;
		else
			return false;
	}
	
	@Override
	public void output()
	{
//		printMarkov();
//		outputKMeasMatrix("D:/Dataset/icwsm_2011/initKMeansMatrix.txt");
		outputKMeasMatrix("D:/Dataset/icwsm_2011/KMeansResult.txt");
	}
	
	private void sortCheckInList(List<CheckIn> checkInList)
	{
		Collections.sort(checkInList, new Comparator<CheckIn>(){
			@Override
			public int compare(CheckIn o1,CheckIn o2) 
			{
				boolean value = o1.getDate().after(o2.getDate());
				return (value ? 1 : -1);
			}
		});
	}
	
	public void printSequence(Sequence sequence)
	{
		if (sequence.getLocationCount() < 3)
			return ;
		String sequenceStr = "";
		for (int i=0;i<sequence.getChechInList().size();i++)
		{
			String locationStr = "";
			CheckIn ci = sequence.getChechInList().get(i);
			FoursquareLocation fl = ci.getLocation().getFoursquareLocation();
			if (fl.getId().equals("EMPTY"))
				locationStr = "{EMPTY|";
			else
			{
				locationStr = "{" /* + fl.getId() + ":" */ + fl.getName() + "|";
				for (FoursquareCategory fc : fl.getCategorys())
				{
					locationStr += /* fc.getId() + ":" + */ HierarchicalCategory.hCategories[ClassifierNew.classMap.get(fc)] + "|";
//					locationStr += /* fc.getId() + ":" + */ fc.getName() + "|";
				}
			}
			locationStr += timeFormatter.format(sequence.getChechInList().get(i).getDate()) + "}";
			sequenceStr += locationStr;
			if (i != sequence.getChechInList().size()-1)
				sequenceStr += "\t->\t";
		}
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FLSequenceByDay_newCategory.txt", 
								sequence.getUser()+"\t"+dateFormatter.format(sequence.getDate())+"\t"+
								sequence.getLocationCount()+"\t"+sequenceStr);
	}
	
	public void establishSimpleSequence(Sequence sequence)
	{
		
	}
	
	public void printMarkov()
	{
		String content = "MARKOV\t";
		
		for (int i=0;i<r;i++)
			content += HierarchicalCategory.hCategories[i] + "\t";
		WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FLSequenceByDay_markovTest.txt",content);
		
		for (int i=0;i<r;i++)
		{
			content = HierarchicalCategory.hCategories[i] + "\t";
			for (int j=0;j<r;j++)
			{
//				content += markovCount[i][j] + "\t"; 
				content += (double)markovCount[i][j]/totalCount[i] + "\t"; 
			}
			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FLSequenceByDay_markovTest.txt",content); 
		}
	}
	
	public void outputKMeasMatrix(String fileName)
	{
		for (int k=0;k<8;k++)
		{
			WriteProcess.appendFile(fileName, k + "\tuser:" + KMeans.clusterUserCount[k]);
			for (int i=0;i<19;i++)
			{
				String content = "";
				for (int j=0;j<19;j++)
				{
					content += String.valueOf(KMeans.pointMatrix[k][i][j]);
					if (j!=18) 
						content += "\t";
				}
				WriteProcess.appendFile(fileName, content);
			}
		}
	}
	
	public void calMatrix(Sequence sequence)
	{
		//对每个user单独算markovMatrix，然后聚类
		if (!sequence.getUser().equals(pre_user))
		{
			if (!pre_user.equals(""))
			{
				count_user++;
				System.out.println("user:" + count_user);
				
				clearMatrixForKMeans();
				
				/***********************************************/
//				int ki = count_user / 2500;
//				if (ki >= 8)
//					ki = 7;
//				testKMeans(ki);
				/***********************************************/
				
			}
			pre_user = sequence.getUser();
		}
		//算全部的matrix，只需要把以上这段注释掉
		
		FoursquareLocation pre_fl = null;
		for (int i =0;i<sequence.getLocationCount();i++)
		{
			FoursquareLocation fl = sequence.getChechInList().get(i).getLocation().getFoursquareLocation();
			if (pre_fl != null)
			{
				markovCount[getNewCategory(pre_fl)][getNewCategory(fl)]++;
				totalCount[getNewCategory(pre_fl)]++;
			}
//			totalCount[getNewCategory(fl)]++;
			
			pre_fl = fl;
		}
	}
	private int getNewCategory(FoursquareLocation foursquareLocation)
	{
		List<FoursquareCategory> fl = foursquareLocation.getCategorys();
		return ClassifierNew.classMap.get(fl.get(0));
	}
	
	private void clearMatrixForKMeans()
	{
		for (int i=0;i<r;i++)
		{
			for (int j=0;j<r;j++)
			{
				if (totalCount[i] == 0)
					markovMatrix[i][j] = 0.0;
				else
					markovMatrix[i][j] = (double)markovCount[i][j] / (double)totalCount[i];
				markovCount[i][j] = 0;
			}
			totalCount[i] = 0;
		}
		KMeans.runKMeans(markovMatrix);
		
	}
	//仅用来生成文件
	private void testKMeans(int ki)//直接写入，不进行判断
	{
		for (int i=0;i<r;i++)
		{
			for (int j=0;j<r;j++)
			{
				if (totalCount[i] == 0)
					markovMatrix[i][j] = 0.0;
				else
					markovMatrix[i][j] = (double)markovCount[i][j] / (double)totalCount[i];
				markovCount[i][j] = 0;
			}
			totalCount[i] = 0;
		}
		KMeans.testKMeans(markovMatrix, ki);//测试 
	}
}
