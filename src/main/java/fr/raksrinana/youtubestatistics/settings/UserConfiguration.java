package fr.raksrinana.youtubestatistics.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.youtubestatistics.interfaces.IYouTubeVideo;
import fr.raksrinana.youtubestatistics.utils.PeriodDurationCollector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.threeten.extra.PeriodDuration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class UserConfiguration{
	@JsonProperty("channels")
	private final Map<String, YouTubeChannel> channels = new HashMap<>();
	@JsonProperty("userId")
	private String userId;
	
	public UserConfiguration(@NonNull final String userId){
		this.userId = userId;
	}
	
	public boolean contains(@NonNull IYouTubeVideo video){
		return this.getChannels().values().stream().anyMatch(channel -> channel.contains(video));
	}
	
	public void addChannels(Collection<YouTubeChannel> channels){
		channels.forEach(this::addChannel);
	}
	
	public void addChannel(YouTubeChannel youTubeChannel){
		channels.compute(youTubeChannel.getChannelId(), (channelId, channel) -> {
			if(Objects.isNull(channel)){
				return youTubeChannel;
			}
			channel.merge(youTubeChannel);
			return channel;
		});
	}
	
	public int getChannelCount(){
		return getChannels().size();
	}
	
	public PeriodDuration getTotalPeriodDuration(){
		return getChannels().values().stream().flatMap(channel -> channel.getVideos().values().stream()).map(YouTubeVideo::getDuration).collect(new PeriodDurationCollector());
	}
	
	public int getVideoCount(){
		return getChannels().values().stream().mapToInt(YouTubeChannel::getVideoCount).sum();
	}
}
