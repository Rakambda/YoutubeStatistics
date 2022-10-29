package fr.rakambda.youtubestatistics.parser;

import fr.rakambda.youtubestatistics.cli.CLIParameters;
import fr.rakambda.youtubestatistics.interfaces.IYouTubeVideo;
import fr.rakambda.youtubestatistics.settings.UserConfiguration;
import lombok.NonNull;
import java.util.Optional;
import java.util.Set;

public interface FileParser{
	Set<? extends IYouTubeVideo> getVideos(@NonNull final UserConfiguration configuration, @NonNull final CLIParameters parameters);
	
	Optional<String> getUserId();
}
