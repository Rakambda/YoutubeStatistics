package fr.rakambda.youtubestatistics.parser.youtubehistoryfile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.rakambda.youtubestatistics.interfaces.IYouTubeVideo;
import fr.rakambda.youtubestatistics.utils.URLUtils;
import fr.rakambda.youtubestatistics.utils.json.ISO8601DateTimeDeserializer;
import fr.rakambda.youtubestatistics.utils.json.URLDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class YoutubeHistoryVideo implements IYouTubeVideo{
	@JsonProperty("header")
	private String header;
	@JsonProperty("title")
	private String title;
	@JsonProperty("titleUrl")
	@JsonDeserialize(using = URLDeserializer.class)
	private URL titleUrl;
	@JsonProperty("subtitles")
	private List<YouTubeVideoSubtitle> subtitles;
	@JsonProperty("time")
	@JsonDeserialize(using = ISO8601DateTimeDeserializer.class)
	private LocalDateTime time;
	@JsonProperty("products")
	private List<String> products;
	
	@Override
	public int hashCode(){
		return Objects.hash(getTitleUrl());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(!(o instanceof IYouTubeVideo that)){
			return false;
		}
		return getVideoId().equals(that.getVideoId());
	}
	
	public @NonNull Optional<String> getVideoId(){
		return Optional.ofNullable(this.getTitleUrl()).map(url -> URLUtils.getQueryMap(url.getQuery()).get("v"));
	}
}
