package fr.rakambda.youtubestatistics.cli;

import fr.rakambda.youtubestatistics.parser.FileParser;
import fr.rakambda.youtubestatistics.parser.YouTubeHistoryFileParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.lang.reflect.InvocationTargetException;

@Getter
@RequiredArgsConstructor
public enum InputType{
	YOUTUBE_HISTORY_FILE(YouTubeHistoryFileParser.class);
	
	private final Class<? extends FileParser> fileParser;
	
	public FileParser getFileParserInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
		return this.getFileParser().getConstructor().newInstance();
	}
}
