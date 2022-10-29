package fr.rakambda.youtubestatistics.parser.youtubehistoryfile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.rakambda.youtubestatistics.utils.json.URLDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.net.URL;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class YouTubeVideoSubtitle{
	@JsonProperty("name")
	private String name;
	@JsonProperty("url")
	@JsonDeserialize(using = URLDeserializer.class)
	private URL url;
	
	@Override
	public int hashCode(){
		return Objects.hash(getUrl());
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if(o == null || getClass() != o.getClass()){
			return false;
		}
		YouTubeVideoSubtitle that = (YouTubeVideoSubtitle) o;
		return Objects.equals(getUrl(), that.getUrl());
	}
}
