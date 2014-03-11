package processor;

import dataIO.ReadProcess;
import entity.Sequence;

public class SequenceAnalyzer {

	public int processCount = 0;
	
	public void processSequence(Sequence sequence)
	{
		System.out.println("father");
	}
	
	public void output()
	{
		
	}
	
	public void process(String lineString)
	{
		Sequence sequence = line2Sequence(lineString);
		processSequence(sequence);
		System.out.println(processCount);
		this.increaseCount();
	}
	
	private Sequence line2Sequence(String lineString)
	{
		Sequence sequence = ReadProcess.readLine_sequence(lineString);
		return sequence;
	}
	
	private void increaseCount()
	{
		this.processCount++;
	}
}
