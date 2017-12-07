package statistic;

import clock.Clock;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventStatisticImpl implements EventsStatistic {

    private Map<String, Queue<DateTime>> eventRateCountTest;

    private Clock clock;


    public EventStatisticImpl(Clock clock) {
        eventRateCountTest = new ConcurrentHashMap<>();
        this.clock = clock;
    }


    @Override
    public void incEvent(String name) {
        eventRateCountTest.putIfAbsent(name, new ConcurrentLinkedQueue<>());
        eventRateCountTest.get(name).add(new DateTime(clock.now().toEpochMilli()));
    }


    @Override
    public double getEventStatisticByName(String name) {
        DateTime now = new DateTime(clock.now().toEpochMilli());
        List<DateTime> collect = eventRateCountTest.get(name).stream()
                .filter(time -> Hours.hoursBetween(now, time).isLessThan(Hours.ONE)).collect(Collectors.toList());
        eventRateCountTest.replace(name, new ConcurrentLinkedQueue<>(collect));
        return collect.size() / 60.0;
    }


    @Override
    public Map<String, Double> getAllEventStatistic() {
        return eventRateCountTest.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), this::getEventStatisticByName));
    }


    @Override
    public void printStatistic() {
        Map<String, Double> statistic = getAllEventStatistic();
        System.out.println("System statistic:");
        statistic.forEach((k, v) -> System.out.println(String.format("Event %s rpm for the last hour is %.3f", k, v)));
    }
}
