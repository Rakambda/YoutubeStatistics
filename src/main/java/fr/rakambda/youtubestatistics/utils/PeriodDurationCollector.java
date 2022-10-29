package fr.rakambda.youtubestatistics.utils;

import org.threeten.extra.PeriodDuration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class PeriodDurationCollector implements Collector<PeriodDuration, AtomicReference<PeriodDuration>, PeriodDuration>{
	@Override
	public Supplier<AtomicReference<PeriodDuration>> supplier(){
		return () -> new AtomicReference<>(PeriodDuration.ZERO);
	}
	
	@Override
	public BiConsumer<AtomicReference<PeriodDuration>, PeriodDuration> accumulator(){
		return (currentPeriod, periodToAdd) -> currentPeriod.set(currentPeriod.get().plus(periodToAdd));
	}
	
	@Override
	public BinaryOperator<AtomicReference<PeriodDuration>> combiner(){
		return (period1, period2) -> new AtomicReference<>(period1.get().plus(period2.get()));
	}
	
	@Override
	public Function<AtomicReference<PeriodDuration>, PeriodDuration> finisher(){
		return AtomicReference::get;
	}
	
	@Override
	public Set<Characteristics> characteristics(){
		return Set.of(UNORDERED, CONCURRENT);
	}
}
