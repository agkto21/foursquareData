package execution;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import processor.CityFilter;
import processor.ClassifierNew;
import processor.FileCutter;
import processor.FoursquareLocationVerifier;
import processor.HTMMInputFileGenerator;
import processor.LengthCounter;
import processor.LocationCounter;
import processor.LocationRecognizer;
import processor.PlsaVocProcessor;
import processor.SequenceCount;
import processor.TextFilter;
import processor.UserCounter;

import dataIO.FileManager;
import dataIO.ReadProcess;
import entity.CheckIn;


public class ExecutionManager {

	public static void file2DetailAddress(String fileName)
	{
		String lineString;
//		AddressProcess addressProcess = new AddressProcess();
		CityFilter cityFilter = new CityFilter();
		FileCutter fileCutter = new FileCutter();
		TextFilter textFilter = new TextFilter();
		UserCounter userCounter = new UserCounter();
		SequenceCount sequenceCount = new SequenceCount();
		LocationCounter locationCounter = new LocationCounter();
		LocationRecognizer locationRecognizer = new LocationRecognizer();
		FoursquareLocationVerifier foursquareLocationVerifier = new FoursquareLocationVerifier();
		
		PlsaVocProcessor plsaVocProcessor = new PlsaVocProcessor();
		HTMMInputFileGenerator hTMMInputFileGenerator = new HTMMInputFileGenerator();
		
//		foursquareLocationVerifier.skipLineCount = 51520;
//		foursquareLocationVerifier.skipLineCount = 52359;
//		foursquareLocationVerifier.skipLineCount = 53173;
//		foursquareLocationVerifier.skipLineCount = 28673;
//		foursquareLocationVerifier.skipLineCount = 45662;
		
//		FileManager fm = new FileManager(fileName);
//		fm.fileProcess(plsaVocProcessor);
		FileManager fm1 = new FileManager(fileName);
		fm1.fileProcess(hTMMInputFileGenerator);
		
	}
	
	public static void sequenceFileProcess(String fileName)
	{
		LengthCounter lengthCounter = new LengthCounter();
		ClassifierNew classifierNew = new ClassifierNew();
		
		FileManager fm = new FileManager(fileName);
		fm.sequenceFileProcess(classifierNew);
	}
	
	public static void copyFile(String sourceFile, String targetFile)
	{
		FileManager fm = new FileManager(sourceFile);
		fm.copyProcess(targetFile);
	}
	
	public static void newClassifier(String fileName)
	{
		FileManager fm = new FileManager(fileName);
		fm.newClassifierProcess();
	}
	
	public static void initKMeansVals(String fileName)
	{
		FileManager fm = new FileManager(fileName);
		fm.initPointMatrixs();
	}
}
