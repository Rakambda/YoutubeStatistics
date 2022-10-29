package fr.rakambda.youtubestatistics.settings;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.rakambda.youtubestatistics.Main;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class Settings{
	private static final ObjectReader objectReader;
	private static final ObjectWriter objectWriter;
	private static final Map<String, UserConfiguration> configurations = new ConcurrentHashMap<>();
	
	@NonNull
	public static UserConfiguration get(@NonNull final String userId){
		return configurations.computeIfAbsent(userId, user -> loadConfiguration(user).orElse(new UserConfiguration(user)));
	}
	
	@NonNull
	private static Optional<UserConfiguration> loadConfiguration(@NonNull final String userId){
		final var userConfPath = getConfigPath(userId);
		if(Files.exists(userConfPath)){
			try(final var fis = Files.newInputStream(userConfPath)){
				return Optional.ofNullable(objectReader.readValue(fis));
			}
			catch(final IOException e){
				log.error("Failed to read settings in {}", userConfPath, e);
			}
		}
		return Optional.empty();
	}
	
	@NonNull
	private static Path getConfigPath(final String userId){
		return Main.getParameters().getSettingsPath().resolve(userId + ".json");
	}
	
	public static void close(){
		save();
	}
	
	public static void save(){
		configurations.forEach(Settings::saveConfiguration);
	}
	
	private static void saveConfiguration(final String userId, @NonNull final UserConfiguration value){
		final var userConfPath = getConfigPath(userId);
		try{
			Files.createDirectories(userConfPath.getParent());
			objectWriter.writeValueAsString(value);
			objectWriter.writeValue(userConfPath.toFile(), value);
			log.info("Wrote settings to {}", userConfPath);
		}
		catch(final IOException e){
			log.error("Failed to write settings to {}", userConfPath, e);
		}
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
		objectReader = mapper.readerFor(UserConfiguration.class);
		objectWriter = mapper.writerFor(UserConfiguration.class);
	}
}
