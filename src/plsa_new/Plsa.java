package plsa_new;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import processor.PlsaInitProcessor;
import processor.PlsaVocProcessor;

import dataIO.FileManager;
import dataIO.WriteProcess;
import entity.Combination;

/**
 * 
 * This class implement plsa.
 * 
 * 
 * @author happyshelocks@gmail.com
 * 
 */
public class Plsa {

	private int topicNum;

	private int userSize;

	private int locationSize;

	// private int[][] userLocationMatrix;
	private Map<Combination, Integer> userLocationMatrix;

	// p(z|d)
	// private double[][] userTopicPros;
	private Map<Combination, Double> userTopicPros;

	// p(w|z)
	// private double[][] topicLocationPros;
	private Map<Combination, Double> topicLocationPros;

	// p(z|d,w)
	// private double[][][] userTermLocationPros;
	private Map<Combination, Double> userTermLocationPros;

	private List<String> allLocations;
	private List<String> allUsers;

	public Plsa(int numOfTopic) {
		topicNum = numOfTopic;
	}

	/**
	 * 
	 * train plsa
	 * 
	 * @param docs
	 *            all documents
	 */
	public void train(String filename, int maxIter) {

		FileManager fm = new FileManager(filename);
		PlsaVocProcessor pvp = new PlsaVocProcessor();
		PlsaInitProcessor pip = new PlsaInitProcessor();

		fm.fileProcess(pvp);
		System.out.println(PlsaVocProcessor.uniqLocationList.size());
		fm.fileProcess(pip);

		// statistics vocabularies
		allLocations = PlsaVocProcessor.uniqLocationList;
		locationSize = PlsaVocProcessor.uniqLocationList.size();

		// element represent times the word appear in this document
		allUsers = PlsaInitProcessor.uniqUserList;
		userSize = PlsaInitProcessor.uniqUserList.size();
		userLocationMatrix = PlsaInitProcessor.userLocationMatrix;

		System.out.println("userSize:" + userSize + ";locationSize:"
				+ locationSize);

		//    	
		// userLocationMatrix = new int[userSize][locationSize];
		// //init docTermMatrix
		// for (int docIndex = 0; docIndex < userSize; docIndex++) {
		// Document doc = docs.get(docIndex);
		// for (String word : doc.getWords()) {
		// if (allLocations.contains(word)) {
		// int wordIndex = allLocations.indexOf(word);
		// userLocationMatrix[docIndex][wordIndex] += 1;
		// }
		// }
		//            
		// //free memory
		// doc.setWords(null);
		// }

		userTopicPros = new HashMap<Combination, Double>();// double[userSize][topicNum];
		topicLocationPros = new HashMap<Combination, Double>();// double[topicNum][locationSize];
//		userTermLocationPros = new HashMap<Combination, Double>();// double[userSize][locationSize][topicNum];

		// init p(z|d),for each document the constraint is sum(p(z|d))=1.0
		for (int i = 0; i < userSize; i++) {
			double[] pros = randomProbilities(topicNum);
			for (int j = 0; j < topicNum; j++) {
				Combination ut = new Combination(1, i, j, 0);
				System.out.println("user - " + i + " ; topic - " + j);
				userTopicPros.put(ut, pros[j]);
				// userTopicPros[i][j] = pros[j];
			}
		}
		// init p(w|z),for each topic the constraint is sum(p(w|z))=1.0
		for (int i = 0; i < topicNum; i++) {
			double[] pros = randomProbilities(locationSize);
			for (int j = 0; j < locationSize; j++) {
				Combination tl = new Combination(2, i, j, 0);
				topicLocationPros.put(tl, pros[j]);
				System.out.println("topic - " + i + " ; location - " + j);
				// topicLocationPros[i][j] = pros[j];
			}
		}

		// use em to estimate params
		for (int i = 0; i < maxIter; i++) {
//			System.out.println("Round - " + i);
			
			em(i);
		}
		System.out.println("done");
	}

	/**
	 * 
	 * EM algorithm
	 * 
	 */
	private void em(int round) {

		// E+M
		double[] Multi_userTopic_byUser = new double[topicNum];
		double[][] Multi_topicLocation_byUser = new double[locationSize][topicNum];
		for (int i = 0; i < topicNum; i++)
			Multi_userTopic_byUser[i] = 0.0;
		for (int i = 0; i < locationSize; i++)
			for (int j = 0; j < topicNum; j++)
				Multi_topicLocation_byUser[i][j] = 0.0;

		double[][] M_userTopic_byUser = new double[userSize][topicNum];

		for (int docIndex = 0; docIndex < userSize; docIndex++) {
			System.out.println("Round - " + round + " | user - " + docIndex);

			// *****************************************************************************************
			// E -> P(Z|l,h)
			double[][] locationTopicPro2thisUser = new double[locationSize][topicNum];
			for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
				double total = 0.0;
				double[] perTopicPro = new double[topicNum];
				for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {

					Combination ut = new Combination(1, docIndex, topicIndex, 0);
					Combination tl = new Combination(2, topicIndex, wordIndex,
							0);

					double numerator = userTopicPros.get(ut)
							* topicLocationPros.get(tl);
					
					total += numerator;
					perTopicPro[topicIndex] = numerator;
				}

				if (total == 0.0) {
					total = avoidZero(total);
				}

				for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
					Combination ult = new Combination(3, docIndex, wordIndex,
							topicIndex);
					locationTopicPro2thisUser[wordIndex][topicIndex] = perTopicPro[topicIndex]
							/ total;

					// userTermLocationPros.put(ult,
					// perTopicPro[topicIndex]/total);

					// userTermLocationPros[docIndex][wordIndex][topicIndex] =
					// perTopicPro[topicIndex]
					// / total;
				}
			}

			// ******************************************************* M1 -> p

			// actually equal sum(w) of this doc

			for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
				double numerator = 0.0;
				for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {

					Combination ul = new Combination(0, docIndex, wordIndex, 0);
					numerator += getUserLocationCount(ul) * locationTopicPro2thisUser[wordIndex][topicIndex];

					// numerator += userLocationMatrix[docIndex][wordIndex]
					// * userTermLocationPros[docIndex][wordIndex][topicIndex];
				}
				Combination ut = new Combination(1,docIndex,topicIndex,0);
				userTopicPros.put(ut, numerator / userSize);
			}


			// ******************************************************* M2 -> p

			// double[][] M_topicLocation_byUser = new
			// double[locationSize][topicNum];
			for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
				double numerator = 0.0;
				for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
					Combination ul = new Combination(0, docIndex, wordIndex, 0);
					Multi_topicLocation_byUser[wordIndex][topicIndex] += getUserLocationCount(ul) * locationTopicPro2thisUser[wordIndex][topicIndex];
				}

			}

		}
		//M2
		for (int topicIndex=0;topicIndex<topicNum;topicIndex++)
		{
			double numerator = 0.0;
			for (int wordIndex=0;wordIndex<locationSize;wordIndex++)
				numerator += Multi_topicLocation_byUser[wordIndex][topicIndex];
			for (int wordIndex=0;wordIndex<locationSize;wordIndex++)
			{
				Combination tl = new Combination(2,topicIndex,wordIndex,0);
				topicLocationPros.put(tl, Multi_topicLocation_byUser[wordIndex][topicIndex] / numerator);
			}
		}
//
//		/*
//		 * E-step,calculate posterior probability p(z|d,w,&),& is model
//		 * params(p(z|d),p(w|z))
//		 * 
//		 * p(z|d,w,&)=p(z|d)*p(w|z)/sum(p(z'|d)*p(w|z')) z' represent all
//		 * posible topic
//		 */
//		for (int docIndex = 0; docIndex < userSize; docIndex++) {
//			System.out.println("E-step:" + docIndex);
//			for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
//				double total = 0.0;
//				double[] perTopicPro = new double[topicNum];
//				for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
//
//					Combination ut = new Combination(1, docIndex, topicIndex, 0);
//					Combination tl = new Combination(2, topicIndex, wordIndex,
//							0);
//
//					double numerator = userTopicPros.get(ut)
//							* topicLocationPros.get(tl);
//
//					// double numerator = userTopicPros[docIndex][topicIndex]
//					// * topicLocationPros[topicIndex][wordIndex];
//					total += numerator;
//					perTopicPro[topicIndex] = numerator;
//				}
//
//				if (total == 0.0) {
//					total = avoidZero(total);
//				}
//
//				for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
//					Combination ult = new Combination(3, docIndex, wordIndex,
//							topicIndex);
//					userTermLocationPros.put(ult, perTopicPro[topicIndex]
//							/ total);
//
//					// userTermLocationPros[docIndex][wordIndex][topicIndex] =
//					// perTopicPro[topicIndex]
//					// / total;
//				}
//			}
//		}
//
//		// M-step
//		/*
//		 * update
//		 * p(w|z),p(w|z)=sum(n(d',w)*p(z|d',w,&))/sum(sum(n(d',w')*p(z|d',w',&)))
//		 * 
//		 * d' represent all documents w' represent all vocabularies
//		 */
//		for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
//			System.out.println("M-step:" + topicIndex);
//			double totalDenominator = 0.0;
//			for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
//				double numerator = 0.0;
//				for (int docIndex = 0; docIndex < userSize; docIndex++) {
//					Combination ul = new Combination(0, docIndex, wordIndex, 0);
//					Combination ult = new Combination(3, docIndex, wordIndex,
//							topicIndex);
//
//					numerator += userLocationMatrix.get(ul)
//							* userTermLocationPros.get(ult);
//
//					// numerator += userLocationMatrix[docIndex][wordIndex]
//					// * userTermLocationPros[docIndex][wordIndex][topicIndex];
//				}
//
//				Combination tl = new Combination(2, topicIndex, wordIndex, 0);
//				topicLocationPros.put(tl, numerator);
//				// topicLocationPros[topicIndex][wordIndex] = numerator;
//
//				totalDenominator += numerator;
//			}
//
//			if (totalDenominator == 0.0) {
//				totalDenominator = avoidZero(totalDenominator);
//			}
//
//			for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
//				Combination tl = new Combination(2, topicIndex, wordIndex, 0);
//				topicLocationPros.put(tl, topicLocationPros.get(tl)
//						/ totalDenominator);
//
//				// topicLocationPros[topicIndex][wordIndex] =
//				// topicLocationPros[topicIndex][wordIndex]
//				// / totalDenominator;
//			}
//		}
//		/*
//		 * update
//		 * p(z|d),p(z|d)=sum(n(d,w')*p(z|d,w'&))/sum(sum(n(d,w')*p(z'|d,w',&)))
//		 * 
//		 * w' represent all vocabularies z' represnet all topics
//		 */
//		for (int docIndex = 0; docIndex < userSize; docIndex++) {
//			// actually equal sum(w) of this doc
//			double totalDenominator = 0.0;
//			for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
//				double numerator = 0.0;
//				for (int wordIndex = 0; wordIndex < locationSize; wordIndex++) {
//
//					Combination ul = new Combination(0, docIndex, wordIndex, 0);
//					Combination ult = new Combination(3, docIndex, wordIndex,
//							topicIndex);
//
//					numerator += userLocationMatrix.get(ul)
//							* userTermLocationPros.get(ult);
//
//					// numerator += userLocationMatrix[docIndex][wordIndex]
//					// * userTermLocationPros[docIndex][wordIndex][topicIndex];
//				}
//				Combination ut = new Combination(1, docIndex, topicIndex, 0);
//				userTopicPros.put(ut, numerator);
//
//				// userTopicPros[docIndex][topicIndex] = numerator;
//				totalDenominator += numerator;
//			}
//
//			if (totalDenominator == 0.0) {
//				totalDenominator = avoidZero(totalDenominator);
//			}
//
//			for (int topicIndex = 0; topicIndex < topicNum; topicIndex++) {
//				Combination ut = new Combination(1, docIndex, topicIndex, 0);
//				userTopicPros.put(ut, userTopicPros.get(ut) / totalDenominator);
//
//				// userTopicPros[docIndex][topicIndex] =
//				// userTopicPros[docIndex][topicIndex]
//				// / totalDenominator;
//			}
//		}
	}

	//
	// private List<String> statisticsLocations(String filename) {
	// List<String> uniqLocations = new LinkedList<String>();
	//        
	// uniqLocations = FileManager.locationSetProcess(filename);
	// locationSize = uniqLocations.size();
	//
	// return uniqLocations;
	// }

	/**
	 * 
	 * 
	 * Get a normalize array
	 * 
	 * @param size
	 * @return
	 */
	public double[] randomProbilities(int size) {
		if (size < 1) {
			throw new IllegalArgumentException(
					"The size param must be greate than zero");
		}
		double[] pros = new double[size];

		int total = 0;
		Random r = new Random();
		for (int i = 0; i < pros.length; i++) {
			// avoid zero
			pros[i] = r.nextInt(size) + 1;

			total += pros[i];
		}

		// normalize
		for (int i = 0; i < pros.length; i++) {
			pros[i] = pros[i] / total;
		}

		return pros;
	}
	
	public int getUserLocationCount(Combination c)
	{
		int userLocationCount;
		if (userLocationMatrix.get(c) != null)
			userLocationCount = userLocationMatrix.get(c);
		else
			userLocationCount = 0;
		return userLocationCount;
	}

	//
	// /**
	// *
	// * @return
	// */
	// public double[][] getUserTopics() {
	// return userTopicPros;
	// }
	//
	// /**
	// *
	// * @return
	// */
	// public double[][] getTopicLocationPros() {
	// return topicLocationPros;
	// }
	//
	// /**
	// *
	// * @return
	// */
	// public List<String> getAllLocations() {
	// return allLocations;
	// }
	//
	// /**
	// *
	// * Get topic number
	// *
	// *
	// * @return
	// */
	// public Integer getTopicNum() {
	// return topicNum;
	// }
	//
	// /**
	// *
	// * Get p(w|z)
	// *
	// * @param word
	// * @return
	// */
	// public double[] getTopicLocationPros(String word) {
	// int index = allLocations.indexOf(word);
	// if (index != -1) {
	// double[] topicWordPros = new double[topicNum];
	// for (int i = 0; i < topicNum; i++) {
	// topicWordPros[i] = topicLocationPros[i][index];
	// }
	// return topicWordPros;
	// }
	//
	// return null;
	// }
	
	public void output(String filename)
	{
		String content = "P{Z|h}\t";
		for (int i=0;i<topicNum;i++)
			content += i + "\t";
		WriteProcess.appendFile(filename, content);
		for (int i=0;i<userSize;i++)
		{
			content = allUsers.get(i) + "\t";
			for (int j=0;j<topicNum;j++)
			{
				Combination ut = new Combination(1,i,j,0);
				content += userTopicPros.get(ut) + "\t";
			}
			WriteProcess.appendFile(filename, content);
		}
		
		WriteProcess.appendFile(filename, "\n***************************************\n");
		
		content = "P{l|Z}\t";
		for (int i=0;i<topicNum;i++)
			content += i + "\t";
		WriteProcess.appendFile(filename, content);
		for (int i=0;i<locationSize;i++)
		{
			content = allLocations.get(i) + "\t";
			for (int j=0;j<topicNum;j++)
			{
				Combination tl = new Combination(2,j,i,0);
				content += topicLocationPros.get(tl) + "\t";
			}
			WriteProcess.appendFile(filename, content);
		}
		
	}

	/**
	 * 
	 * avoid zero number.if input number is zero, we will return a magic number.
	 * 
	 * 
	 */
	private final static double MAGICNUM = 0.0000000000000001;

	public double avoidZero(double num) {
		if (num == 0.0) {
			return MAGICNUM;
		}

		return num;
	}

	public static void main(String[] args) {
		Plsa plsa = new Plsa(5);
		plsa.train("F:/[A]ACT-work/Dataset/icwsm_2011/checkin_NewYork_FoursquareLocations_complete.txt",50);
		plsa.output("F:/[A]ACT-work/Dataset/icwsm_2011/PLSA/result.txt");
	}

}