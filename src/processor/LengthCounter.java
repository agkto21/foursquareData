package processor;

import entity.Sequence;

public class LengthCounter extends SequenceAnalyzer{

	private static int count_0_10 = 0, count_10_20 = 0, count_20_30 = 0, count_30_40 = 0, count_large = 0;
	private static int user_count = 0;
	private static String pre_user = "";
	
	@Override
	public void processSequence(Sequence sequence)
	{
		classify(sequence);
		countUser(sequence);
	}
	
	
	@Override
	public void output()
	{
		System.out.println(count_0_10 + " " + count_10_20 + " " + count_20_30 + " " + count_30_40 + " " + count_large);
		System.out.println(user_count);
	}
	
	private void countUser(Sequence sequence)
	{
		if (!sequence.getUser().equals(pre_user))
		{
			if (this.processCount != 1)
				user_count++;
			pre_user = sequence.getUser();
		}
	}
	
	private void classify(Sequence sequence)
	{
		if (sequence.getLocationCount() <= 10)
			count_0_10++;
		else if (sequence.getLocationCount() > 10 && sequence.getLocationCount() < 20)
			count_10_20++;
		else if (sequence.getLocationCount() > 20 && sequence.getLocationCount() < 30)
			count_20_30++;
		else if (sequence.getLocationCount() > 30 && sequence.getLocationCount() < 40)
			count_30_40++;
		else 
			count_large++;
	}
}
