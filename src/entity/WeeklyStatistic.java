package entity;

import java.util.Calendar;
import java.util.Date;

public class WeeklyStatistic {

	private int[] weeklyCount;

	public int[] getWeeklyCount() {
		return weeklyCount;
	}

	public void setWeeklyCount(int[] weeklyCount) {
		this.weeklyCount = weeklyCount;
	}

	public WeeklyStatistic() {
		super();
		this.weeklyCount = new int[8];
		for (int i = 0; i <= 7; i++)
			this.weeklyCount[i] = 0;
	}

	public void addDayCount(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1)
			dayForWeek = 7;
		else
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		System.out.println(dayForWeek);
		this.addDayCount(dayForWeek);
	}

	private void addDayCount(int day) {
		this.weeklyCount[day]++;
	}

	public int getSum() {
		int sum = 0;
		for (int i = 1; i <= 7; i++)
			sum += weeklyCount[i];
		return sum;
	}

	public String outputString() {
		String str = "SUM:" + this.getSum() + "\tWeekly:\t";
		for (int i = 1; i <= 7; i++)
			str += weeklyCount[i] + "\t";
		return str;
	}

}
