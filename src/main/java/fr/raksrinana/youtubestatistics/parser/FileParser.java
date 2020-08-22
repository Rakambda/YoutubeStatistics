package fr.raksrinana.youtubestatistics.parser;

import fr.raksrinana.youtubestatistics.cli.CLIParameters;
import fr.raksrinana.youtubestatistics.interfaces.IYouTubeVideo;
import fr.raksrinana.youtubestatistics.settings.UserConfiguration;
import lombok.NonNull;
import java.util.Optional;
import java.util.Set;

public interface FileParser{
	Set<? extends IYouTubeVideo> getVideos(@NonNull final UserConfiguration configuration, @NonNull final CLIParameters parameters);
	
	Optional<String> getUserId();
}
