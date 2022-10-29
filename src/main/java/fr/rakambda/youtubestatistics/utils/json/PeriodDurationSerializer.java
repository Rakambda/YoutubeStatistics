package fr.rakambda.youtubestatistics.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.NonNull;
import org.threeten.extra.PeriodDuration;
import java.io.IOException;

public class PeriodDurationSerializer extends JsonSerializer<PeriodDuration>{
	@Override
	public void serialize(@NonNull final PeriodDuration periodDuration, @NonNull final JsonGenerator jsonGenerator, final @NonNull SerializerProvider serializerProvider) throws IOException{
		jsonGenerator.writeString(periodDuration.toString());
	}
}
