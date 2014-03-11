package dataIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processor.ClassifierNew;
import processor.KMeans;
import processor.ProcessHandler;
import processor.SequenceAnalyzer;
import entity.CheckIn;
import entity.FoursquareCategory;


public class FileManager {
	
	private String fileName;
	private File file;
	
	public FileManager(String fileName)
	{
		this.fileName = fileName;
		this.file = new File(fileName);
	}
	
	public void fileProcess(ProcessHandler processHandler)
	{
		String lineString;
		CheckIn checkIn;
		int lineNumber = 1;
		
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
			while ((lineString = bufferedReader.readLine())!=null && processHandler.isStatus())
			{
				
				
				processHandler.process(lineString);
				
				lineNumber++;
     
				
//		    DetailedLocation detailedLocation;
				//	detailedLocation = GeoQuery.getDetailedLocation(checkIn);
				//	detailedLocation = GeoQuery.filterNewYork(checkIn);
//        		detailedLocation = GeoQuery.filterFrasco(checkIn);
//        		
//        		if (detailedLocation == null) 
//        			detailedLocation = new DetailedLocation("NULL","NULL","NULL");
//        		if (detailedLocation.getAdmin_level_1().equals("San Francisco"))
//        			WriteProcess.appendFile("F:/[A]ACT-work/Dataset/SHTiesData/FoursquareCheckinAddress_Frasco.csv", lineString+","+
//        								detailedLocation.getCountry()+","+
//        								detailedLocation.getAdmin_level_1()+","+
//        								detailedLocation.getAdmin_level_2());
				
			}
			
			processHandler.output();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void sequenceFileProcess(SequenceAnalyzer sequenceAnalyzer)
	{
		String lineString;
		CheckIn checkIn;
		int lineNumber = 1;
		
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
			while ((lineString = bufferedReader.readLine())!=null)
			{
				
				
				sequenceAnalyzer.process(lineString);
				
				lineNumber++;
			}
			
			sequenceAnalyzer.output();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void copyProcess(String targetFile)
	{
		String lineString;
		int lineNumber = 1;
		
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
			while ((lineString = bufferedReader.readLine())!=null)
			{
				System.out.println(this.fileName + ":" + lineNumber);
				WriteProcess.appendFile(targetFile, lineString);
				lineNumber++;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void initPointMatrixs()
	{
		String lineString;
		int lineNumber = 1;
		int currentMatrix = 0;
		int lineRead = 19;
		
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
			while ((lineString = bufferedReader.readLine())!=null)
			{
//				System.out.println(this.fileName + ":" + lineNumber);
//				WriteProcess.appendFile(targetFile, lineString);
				
				if (lineRead == 19)
				{
					String[] title = lineString.split("\t");
					currentMatrix = Integer.valueOf(title[0]);
					lineRead = 0;
				}
				else
				{
					String[] num = lineString.split("\t");
					for (int i=0;i<19;i++)
						KMeans.pointMatrix[currentMatrix][lineRead][i] = Double.valueOf(num[i]);
					lineRead++;
				}
				
				lineNumber++;
			}
			
			
			System.out.println("init_suc");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void newClassifierProcess()
	{
		String lineString;
		int lineNumber = 1;
		
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
			while ((lineString = bufferedReader.readLine())!=null)
			{
//				System.out.println(this.fileName + ":" + lineNumber);
//				WriteProcess.appendFile(targetFile, lineString);
				ClassifierNew.readConfigFile(lineString);
				lineNumber++;
			}
			
			Map<FoursquareCategory,Integer> m = ClassifierNew.classMap;
			System.out.println("suc");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}
