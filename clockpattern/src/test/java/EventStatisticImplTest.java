import clock.Clock;
import clock.SettableClock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import statistic.EventStatisticImpl;
import statistic.EventsStatistic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class EventStatisticImplTest {

    private static final double eps = 10e5;

    private List<String> names;

    private EventsStatistic eventsStatistic;

    private Clock clock;


    @Before
    public void setUp() throws Exception {
        Instant instant = Instant.now();
        clock = new SettableClock(instant);
        eventsStatistic = new EventStatisticImpl(clock);

        names = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            names.add("name" + i);
        }
    }


    @Test
    public void testIncEvent() throws Exception {
        eventsStatistic.incEvent(names.get(1));
        double eventStatisticByName = eventsStatistic.getEventStatisticByName(names.get(1));

        Assert.assertTrue(Math.abs(eventStatisticByName - (1 / 60.0)) < eps);

        clock.setNow(clock.now().plus(60, ChronoUnit.MINUTES));
        eventsStatistic.getEventStatisticByName(names.get(1));

        Assert.assertTrue(Math.abs(eventStatisticByName) < eps);
    }


    @Test
    public void testMultipleInc() throws Exception {
        for (int i = 0; i < 60; i++) {
            eventsStatistic.incEvent(names.get(1));
            Instant plus = clock.now().plus(1, ChronoUnit.MINUTES);
            clock.setNow(plus);
        }

        double eventStatisticByName = eventsStatistic.getEventStatisticByName(names.get(1));

        Assert.assertTrue(Math.abs(eventStatisticByName - 1) < eps);
    }


    @Test
    public void testMultipleEventsInc() throws Exception {
        Instant start = clock.now().plus(0, ChronoUnit.MINUTES);


        for (int i = 0; i < 100; i++) {
            Instant currInstant = start.minus(10, ChronoUnit.SECONDS);
            clock.setNow(currInstant);
            eventsStatistic.incEvent(names.get(i));
        }

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Instant currInstant = start;
            for (int j = 0; j < 10; j++) {
                int add = random.nextInt(360);
                currInstant = currInstant.plus(add, ChronoUnit.SECONDS);
                clock.setNow(currInstant);
                eventsStatistic.incEvent(names.get(i));
            }
        }
        clock.setNow(start.plus(60, ChronoUnit.MINUTES));
        Map<String, Double> allEventStatistic = eventsStatistic.getAllEventStatistic();
        allEventStatistic.forEach((k, v) -> Assert.assertTrue(Math.abs(v - (1 / 6)) < eps));

    }
}