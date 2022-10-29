package fr.rakambda.youtubestatistics.utils;

import fr.rakambda.youtubestatistics.settings.YouTubeChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class YouTubeChannelCollector implements Collector<YouTubeChannel, HashMap<String, YouTubeChannel>, Set<YouTubeChannel>>{
	@Override
	public Supplier<HashMap<String, YouTubeChannel>> supplier(){
		return HashMap::new;
	}
	
	@Override
	public BiConsumer<HashMap<String, YouTubeChannel>, YouTubeChannel> accumulator(){
		return (channels, channelToAdd) -> channels.compute(channelToAdd.getChannelId(), (channelId, channel) -> {
			if(Objects.isNull(channel)){
				return channelToAdd;
			}
			channel.merge(channelToAdd);
			return channel;
		});
	}
	
	@Override
	public BinaryOperator<HashMap<String, YouTubeChannel>> combiner(){
		return (map1, map2) -> {
			final var finalMap = new HashMap<>(map1);
			for(final var map2entry : map2.entrySet()){
				finalMap.compute(map2entry.getKey(), (channelId, channel) -> {
					if(Objects.isNull(channel)){
						return map2entry.getValue();
					}
					channel.merge(map2entry.getValue());
					return channel;
				});
			}
			return finalMap;
		};
	}
	
	@Override
	public Function<HashMap<String, YouTubeChannel>, Set<YouTubeChannel>> finisher(){
		return map -> new HashSet<>(map.values());
	}
	
	@Override
	public Set<Characteristics> characteristics(){
		return Set.of(UNORDERED, CONCURRENT);
	}
}
