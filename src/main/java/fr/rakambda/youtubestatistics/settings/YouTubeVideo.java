package fr.rakambda.youtubestatistics.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.rakambda.youtubestatistics.interfaces.IYouTubeVideo;
import fr.rakambda.youtubestatistics.utils.json.PeriodDurationDeserializer;
import fr.rakambda.youtubestatistics.utils.json.PeriodDurationSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.threeten.extra.PeriodDuration;
import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class YouTubeVideo implements IYouTubeVideo{
	@JsonProperty("id")
	private String videoId;
	@JsonProperty("title")
	private String title;
	@JsonProperty("duration")
	@JsonDeserialize(using = PeriodDurationDeserializer.class)
	@JsonSerialize(using = PeriodDurationSerializer.class)
	private PeriodDuration duration;
	
	public YouTubeVideo(@NonNull String videoId, @NonNull String title, @NonNull PeriodDuration duration){
		this.videoId = videoId;
		this.title = title;
		this.duration = duration;
	}
	
	public static YouTubeVideo getUnknown(String videoId){
		return new YouTubeVideo(Objects.isNull(videoId) ? "null" : videoId, "<<Unknown>>", PeriodDuration.ZERO);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(getVideoId());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof IYouTubeVideo that)){
			return false;
		}
		return Objects.equals(getVideoId(), that.getVideoId());
	}
	
	public Optional<String> getVideoId(){
		return Optional.ofNullable(videoId);
	}
}
