package fr.raksrinana.youtubestatistics.parser;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.raksrinana.youtubestatistics.cli.CLIParameters;
import fr.raksrinana.youtubestatistics.interfaces.IYouTubeVideo;
import fr.raksrinana.youtubestatistics.parser.youtubehistoryfile.YoutubeHistoryVideo;
import fr.raksrinana.youtubestatistics.settings.UserConfiguration;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class YouTubeHistoryFileParser implements FileParser{
	private static final ObjectReader objectReader;
	
	@Override
	public Set<? extends IYouTubeVideo> getVideos(@NonNull final UserConfiguration configuration, @NonNull final CLIParameters parameters){
		if(Objects.isNull(parameters.getInputFile())){
			log.error("No input file provided");
			return Set.of();
		}
		return getModel(parameters.getInputFile()).orElse(Set.of());
	}
	
	@Override
	public Optional<String> getUserId(){
		return Optional.empty();
	}
	
	private Optional<Set<YoutubeHistoryVideo>> getModel(@NonNull final Path filePath){
		if(Files.exists(filePath)){
			try(final var fis = Files.newInputStream(filePath)){
				return Optional.ofNullable(objectReader.readValue(fis));
			}
			catch(final IOException e){
				log.error("Failed to read object in {}", filePath, e);
			}
		}
		return Optional.empty();
	}
	
	static{
		final var mapper = new ObjectMapper();
		mapper.setVisibility(mapper.getSerializationConfig()
				.getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectReader = mapper.readerFor(new TypeReference<Set<YoutubeHistoryVideo>>(){});
	}
}
