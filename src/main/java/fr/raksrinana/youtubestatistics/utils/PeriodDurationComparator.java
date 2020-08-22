package fr.raksrinana.youtubestatistics.utils;

import org.threeten.extra.PeriodDuration;
import java.util.Comparator;

public class PeriodDurationComparator implements Comparator<PeriodDuration>{
	@Override
	public int compare(PeriodDuration o1, PeriodDuration o2){
		final var diff = o1.minus(o2);
		if(diff.isZero()){
			return 0;
		}
		if(diff.getPeriod().isNegative() || diff.getDuration().isNegative()){
			return -1;
		}
		return 1;
	}
}
