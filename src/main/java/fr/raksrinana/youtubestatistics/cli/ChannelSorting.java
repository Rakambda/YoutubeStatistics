package fr.raksrinana.youtubestatistics.cli;

import fr.raksrinana.youtubestatistics.settings.YouTubeChannel;
import java.time.Duration;
import java.time.Period;
import java.util.Comparator;

public enum ChannelSorting implements Comparator<YouTubeChannel>{
	VIDEO_COUNT(Comparator.comparingInt(YouTubeChannel::getVideoCount).reversed()), VIDEO_LENGTH(Comparator.comparing(YouTubeChannel::getTotalPeriodDuration, (d1, d2) -> {
		Period pDiff = d1.getPeriod().minus(d2.getPeriod());
		if(pDiff.isZero()){
			Duration dDiff = d1.getDuration().minus(d2.getDuration());
			return dDiff.isZero() ? 0 : (dDiff.isNegative() ? -1 : 1);
		}
		return pDiff.isNegative() ? -1 : 1;
	}).reversed());
	private final Comparator<YouTubeChannel> comparator;
	
	ChannelSorting(Comparator<YouTubeChannel> comparator){
		this.comparator = comparator;
	}
	
	@Override
	public int compare(YouTubeChannel o1, YouTubeChannel o2){
		return this.comparator.compare(o1, o2);
	}
}
