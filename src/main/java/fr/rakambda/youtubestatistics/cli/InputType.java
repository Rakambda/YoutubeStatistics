package fr.rakambda.youtubestatistics.cli;

import fr.rakambda.youtubestatistics.parser.FileParser;
import fr.rakambda.youtubestatistics.parser.YouTubeHistoryFileParser;
import lombok.Getter;
import java.lang.reflect.InvocationTargetException;

public enum InputType{
	YOUTUBE_HISTORY_FILE(YouTubeHistoryFileParser.class);
	@Getter
	private final Class<? extends FileParser> fileParser;
	
	InputType(Class<? extends FileParser> fileParser){
		this.fileParser = fileParser;
	}
	
	public FileParser getFileParserInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException{
		return this.getFileParser().getConstructor().newInstance();
	}
}
