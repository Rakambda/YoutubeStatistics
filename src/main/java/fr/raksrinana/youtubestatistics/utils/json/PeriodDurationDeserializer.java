package fr.raksrinana.youtubestatistics.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.NonNull;
import org.threeten.extra.PeriodDuration;
import java.io.IOException;

public class PeriodDurationDeserializer extends JsonDeserializer<PeriodDuration>{
	@Override
	public PeriodDuration deserialize(@NonNull final JsonParser jsonParser, @NonNull final DeserializationContext deserializationContext) throws IOException{
		return PeriodDuration.parse(jsonParser.getValueAsString());
	}
}
