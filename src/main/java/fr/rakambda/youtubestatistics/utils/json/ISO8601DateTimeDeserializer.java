package fr.rakambda.youtubestatistics.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ISO8601DateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
	
	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException{
		return ZonedDateTime.parse(jsonParser.getValueAsString(), FORMATTER).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
	}
}
