package fr.raksrinana.youtubestatistics.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import fr.raksrinana.youtubestatistics.Main;
import fr.raksrinana.youtubestatistics.interfaces.IYouTubeVideo;
import fr.raksrinana.youtubestatistics.settings.YouTubeChannel;
import fr.raksrinana.youtubestatistics.settings.YouTubeVideo;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.threeten.extra.PeriodDuration;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.time.DateTimeException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class YouTubeHelper{
	@Getter
	private static final Collection<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/youtube.readonly");
	@Getter
	private static final String APPLICATION_NAME = "YouTubeStatistics";
	@Getter
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private final YouTube youtube;
	private static YouTube SERVICE;
	@Getter
	@Setter
	private static boolean errored = false;
	
	private YouTubeHelper(YouTube youtube){
		this.youtube = youtube;
	}
	
	public Collection<YouTubeChannel> getVideoInfo(@NonNull IYouTubeVideo video){
		return video.getVideoId().map(this::getVideoInfo).orElseGet(() -> {
			final var channel = YouTubeChannel.getUnknown();
			channel.addVideo(YouTubeVideo.getUnknown(null));
			return List.of(channel);
		});
	}
	
	public Collection<YouTubeChannel> getVideoInfo(@NonNull String videoId){
		if(isErrored()){
			return List.of();
		}
		log.debug("Getting video info for id {}", videoId);
		try{
			final var request = youtube.videos().list(List.of("snippet", "contentDetails"));
			final var response = request.setId(List.of(videoId)).execute();
			if(response.getItems().isEmpty()){
				log.warn("Unknown video {}", videoId);
				final var channel = YouTubeChannel.getUnknown();
				channel.addVideo(YouTubeVideo.getUnknown(videoId));
				return List.of(channel);
			}
			return response.getItems().stream().map(video -> {
				try{
					final var channel = new YouTubeChannel(video.getSnippet().getChannelId(), video.getSnippet().getChannelTitle());
					final var periodDuration = PeriodDuration.parse(video.getContentDetails().getDuration());
					channel.addVideo(new YouTubeVideo(video.getId(), video.getSnippet().getTitle(), periodDuration));
					return channel;
				}
				catch(DateTimeException e){
					log.error("Failed to parse duration: {}", video.getContentDetails().getDuration(), e);
				}
				return null;
			}).filter(Objects::nonNull).collect(Collectors.toList());
		}
		catch(Exception e){
			log.error("Failed to get video info for id {}: {}", videoId, e);
			setErrored(true);
		}
		return List.of();
	}
	
	public static YouTubeHelper getInstance() throws GeneralSecurityException, IOException{
		return new YouTubeHelper(getService());
	}
	
	private static YouTube getService() throws GeneralSecurityException, IOException{
		if(Objects.isNull(SERVICE)){
			final var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			final var credential = authorize(httpTransport);
			SERVICE = new YouTube.Builder(httpTransport, getJSON_FACTORY(), credential).setApplicationName(getAPPLICATION_NAME()).build();
		}
		return SERVICE;
	}
	
	public static Credential authorize(final NetHttpTransport httpTransport) throws IOException{
		final var clientSecrets = GoogleClientSecrets.load(getJSON_FACTORY(), Files.newBufferedReader(Main.getParameters().getYouTubeSecretsPath()));
		final var flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, getJSON_FACTORY(), clientSecrets, getSCOPES()).build();
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
	
	public Optional<YouTubeChannel> getOwner(){
		try{
			final var request = youtube.channels().list(List.of("snippet"));
			final var response = request.setMine(true).execute();
			if(Objects.isNull(response.getItems()) || response.getItems().isEmpty()){
				log.warn("Unknown owner channel");
				return Optional.empty();
			}
			return response.getItems().stream().map(channel -> new YouTubeChannel(channel.getId(), channel.getSnippet().getTitle())).findAny();
		}
		catch(Exception e){
			log.error("Failed to get channel owner", e);
			setErrored(true);
		}
		return Optional.empty();
	}
}
