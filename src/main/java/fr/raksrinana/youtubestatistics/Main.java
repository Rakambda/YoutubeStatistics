package fr.raksrinana.youtubestatistics;

import fr.raksrinana.youtubestatistics.cli.CLIParameters;
import fr.raksrinana.youtubestatistics.settings.Settings;
import fr.raksrinana.youtubestatistics.settings.YouTubeChannel;
import fr.raksrinana.youtubestatistics.utils.YouTubeChannelCollector;
import fr.raksrinana.youtubestatistics.utils.YouTubeHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import org.threeten.extra.AmountFormats;
import picocli.CommandLine;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Main class.
 */
@Slf4j
public class Main{
	@Getter
	private static final CLIParameters parameters = new CLIParameters();
	
	public static void main(final String[] args){
		var cli = new CommandLine(parameters);
		cli.registerConverter(Path.class, Paths::get);
		cli.setStopAtUnmatched(true);
		try{
			cli.parseArgs(args);
		}
		catch(final CommandLine.ParameterException e){
			log.error("Failed to parse arguments", e);
			return;
		}
		
		try{
			final var youtube = YouTubeHelper.getInstance();
			final var parser = parameters.getInputType().getFileParserInstance();
			parser.getUserId().or(() -> youtube.getOwner().map(YouTubeChannel::getChannelId)).ifPresentOrElse(channelId -> {
				log.info("Logged in as {}", channelId);
				final var userConfiguration = Settings.get(channelId);
				final var videoIds = parser.getVideos(userConfiguration, parameters);
				final var videosToGet = videoIds.stream().filter(video -> !userConfiguration.contains(video)).collect(Collectors.toList());
				log.info("{} new videos to get", videosToGet.size());
				if(!videosToGet.isEmpty()){
					try(final var progressBar = new ProgressBar("Getting video info", videosToGet.size(), ProgressBarStyle.ASCII)){
						userConfiguration.addChannels(videosToGet.stream().flatMap(video -> {
							progressBar.setExtraMessage("Processing " + video.getVideoId().orElse(null));
							final var info = youtube.getVideoInfo(video).stream();
							progressBar.step();
							return info;
						}).collect(new YouTubeChannelCollector()));
					}
				}
				final var totalChannel = userConfiguration.getChannelCount();
				final var totalVideo = userConfiguration.getVideoCount();
				final var totalDuration = userConfiguration.getTotalPeriodDuration();
				log.info("Found {} channels with a total of {} videos ({} total time)", totalChannel, totalVideo, AmountFormats.wordBased(totalDuration.getPeriod(), totalDuration.getDuration(), Locale.ENGLISH));
				log.info("Top {} channels: ", parameters.getBestChannelCount());
				AtomicInteger position = new AtomicInteger(0);
				userConfiguration.getChannels().values().stream().sorted(parameters.getSorting()).limit(parameters.getBestChannelCount()).forEachOrdered(channel -> {
					final var channelDuration = channel.getTotalPeriodDuration();
					log.info("#{} {}: {} videos ({})", position.incrementAndGet(), channel.getTitle(), channel.getVideoCount(), AmountFormats.wordBased(channelDuration.getPeriod(), channelDuration.getDuration(), Locale.ENGLISH));
				});
			}, () -> log.info("Not connected to any account"));
		}
		catch(Exception e){
			log.error("Failed initialize elements", e);
		}
		finally{
			Settings.close();
		}
	}
}
