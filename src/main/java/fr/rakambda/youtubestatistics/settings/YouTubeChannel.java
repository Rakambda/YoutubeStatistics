package fr.rakambda.youtubestatistics.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.rakambda.youtubestatistics.interfaces.IYouTubeChannel;
import fr.rakambda.youtubestatistics.interfaces.IYouTubeVideo;
import fr.rakambda.youtubestatistics.utils.PeriodDurationCollector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.threeten.extra.PeriodDuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class YouTubeChannel implements IYouTubeChannel{
	@JsonProperty("videos")
	private final Map<String, YouTubeVideo> videos = new HashMap<>();
	@JsonProperty("channelId")
	private String channelId;
	@JsonProperty("title")
	private String title;
	
	public YouTubeChannel(@NonNull String channelId, @NonNull String title){
		this.channelId = channelId;
		this.title = title;
	}
	
	public boolean contains(@NonNull IYouTubeVideo video){
		return video.getVideoId().map(videoId -> this.getVideos().containsKey(videoId)).orElseGet(() -> this.videos.containsKey("null"));
	}
	
	public void addVideo(@NonNull YouTubeVideo youTubeVideo){
		youTubeVideo.getVideoId().ifPresent(videoId -> videos.put(videoId, youTubeVideo));
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getChannelId());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof IYouTubeChannel that)){
			return false;
		}
		return Objects.equals(getChannelId(), that.getChannelId());
	}
	
	public void merge(@NonNull YouTubeChannel other){
		other.getVideos().forEach((videoId, video) -> getVideos().putIfAbsent(videoId, video));
	}
	
	public PeriodDuration getTotalPeriodDuration(){
		return videos.values().stream().map(YouTubeVideo::getDuration).collect(new PeriodDurationCollector());
	}
	
	public static YouTubeChannel getUnknown(){
		return new YouTubeChannel("unknown", "Unknown");
	}
	
	public int getVideoCount(){
		return getVideos().size();
	}
}
