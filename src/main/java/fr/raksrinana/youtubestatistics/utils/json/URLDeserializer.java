package fr.raksrinana.youtubestatistics.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j2
public class URLDeserializer extends JsonDeserializer<URL>{
	@Override
	public URL deserialize(@NonNull final JsonParser jsonParser, @NonNull final DeserializationContext deserializationContext) throws IOException{
		try{
			return new URL(jsonParser.getValueAsString());
		}
		catch(final MalformedURLException e){
			log.trace("Failed to parse URL {}", jsonParser.getValueAsString());
		}
		return null;
	}
}
