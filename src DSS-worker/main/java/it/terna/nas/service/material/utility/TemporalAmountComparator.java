package it.terna.nas.service.material.utility;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Comparator;

public class TemporalAmountComparator implements Comparator<TemporalAmount> {

	@Override
	public int compare(TemporalAmount a1, TemporalAmount a2) {
		Duration d1 = toDuration(a1);
		Duration d2 = toDuration(a2);
		return d1.compareTo(d2);
	}
	
	private Duration toDuration(TemporalAmount ta) {
		if(ta instanceof Duration) {
			return (Duration) ta;
		} else if(ta instanceof Period) {
			Period p = (Period) ta;
			long totalDays = p.getYears() * 365L + p.getMonths() * 30L + p.getDays();
			return Duration.ofDays(totalDays);
		} else {
			throw new IllegalArgumentException("Unsupported type: " + ta.getClass());
		}
	}

}
