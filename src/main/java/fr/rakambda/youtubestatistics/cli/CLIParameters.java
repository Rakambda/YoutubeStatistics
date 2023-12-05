package fr.rakambda.youtubestatistics.cli;

import lombok.Getter;
import lombok.NoArgsConstructor;
import picocli.CommandLine;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
@Getter
public class CLIParameters{
	@CommandLine.Option(names = {"--input"}, description = "The path to the input file", required = true)
	public Path inputFile;
	@CommandLine.Option(names = {"--type"}, description = "The type of the input")
	public InputType inputType = InputType.YOUTUBE_HISTORY_FILE;
	@CommandLine.Option(names = {"--settings"}, description = "The path to the settings folder")
	public Path settingsPath = Paths.get("settings");
	@CommandLine.Option(names = {"--yt-secrets"}, description = "The path to the youtube secrets file", required = true)
	public Path youTubeSecretsPath;
	@CommandLine.Option(names = {"--best-count"}, description = "The number of channel statistics to display")
	public int bestChannelCount = 50;
	@CommandLine.Option(names = {"--sort"}, description = "The way to sort the results")
	public ChannelSorting sorting = ChannelSorting.VIDEO_COUNT;
}

