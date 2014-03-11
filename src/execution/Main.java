package execution;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import processor.KMeans;
import dataIO.FileManager;
import dataIO.ReadProcess;
import dataIO.WriteProcess;
import dataModel.CheckInInfo;
import dataModel.DetailedLocation;
import dataModel.GeoQuery;
import dataModel.LocationStatistic;
import entity.CheckIn;
import entity.Coordinate;
import entity.FoursquareLocation;
import entity.Location;
import entity.Tweet;
import entity.User;
import entity.WeeklyStatistic;

public class Main {

	public static void main(String[] args) {
		
		
//		ExecutionManager.newClassifier("D:/Dataset/icwsm_2011/checkin_NewYork_CategoryCount_h.txt");
		
//		KMeans.initialPointMatrixFromFile("D:/Dataset/icwsm_2011/initKMeansMatrix.txt");
		
		ExecutionManager.file2DetailAddress("E:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FoursquareLocations_complete.txt");
				
//		ExecutionManager.copyFile("F:/[A]ACT-work/Dataset/icwsm_2011/Rendered_file/checkin_NewYork_FoursquareLocations_NEW_2_segment_0.txt",
//									"F:/[A]ACT-work/Dataset/icwsm_2011/Rendered_file/checkin_NewYork_FoursquareLocations.txt");
		
		
		
//		ExecutionManager.sequenceFileProcess("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FLSequenceByDay_test.txt");
		
		
	}
}
