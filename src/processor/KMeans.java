package processor;

import execution.ExecutionManager;

public class KMeans {
	
	private static final int k = 8;
	private static final int r = SequenceCount.r;
	
	
	public static double[][][] pointMatrix = new double[k][r][r];
	
	public static int[] clusterUserCount = new int[k];
	
	public static void initialPointMatrixFromFile(String filename)
	{
		for (int i=0;i<k;i++)
			clusterUserCount[i] = 0;
		
		//initial every POINT
		ExecutionManager.initKMeansVals(filename);
	}
	
	public static double getDistance(double[][] m1, double [][] m2)
	{
		double result = 0.0;
		for (int i=0;i<r;i++)
		{
			for (int j=0;j<r;j++)
			{
				result += (m1[i][j] - m2[i][j]) * (m1[i][j] - m2[i][j]);
			}
		}
		return result;
	}
	
	public static void assign(double[][] m, int target)
	{
		double percentage = (double)clusterUserCount[target] / (double)(clusterUserCount[target] + 1);
		
		for (int i=0;i<r;i++)
		{
			for (int j=0;j<r;j++)
			{
				pointMatrix[target][i][j] = pointMatrix[target][i][j] * percentage + m[i][j] * (1 - percentage);
			}
		}
	}
	
	public static void runKMeans(double[][] m)
	{
		double shortestDistance = -1.0, distance;
		int nearest = -1;
		
		for (int i=0;i<k;i++)
		{
			distance = getDistance(m, pointMatrix[i]);
			if (shortestDistance < 0 || shortestDistance > distance)
			{
				nearest = i;
				shortestDistance = distance;
			}
		}
		
		clusterUserCount[nearest]++;
		assign(m, nearest);
	}
	public static void testKMeans(double[][] m, int ki)
	{
		clusterUserCount[ki]++;
		assign(m, ki);
	}
}
