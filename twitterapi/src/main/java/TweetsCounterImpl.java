import org.joda.time.DateTime;
import org.joda.time.Hours;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TweetsCounterImpl implements TweetsCounter {

    private final Requester requester;


    public TweetsCounterImpl(TwitterProperties properties) {
        requester = new RequesterImpl(properties);
    }


    @Override
    public List<Long> getTweetsCount(int countOfHours, String hashtag) {
        DateTime dateTime = new DateTime();
        DateTime limit = dateTime.minusHours(countOfHours);

        List<Status> tweets = requester.searchWithLimit(dateTime.toDate(), hashtag);

        Map<Integer, Long> collect = tweets.stream().filter(tweet -> tweet.getCreatedAt().after(limit.toDate()))
                .collect(Collectors.groupingBy((Status v) ->
                                Hours.hoursBetween(new DateTime(v.getCreatedAt()), dateTime).getHours(),
                        Collectors.counting()));

        List<Long> ans = new ArrayList<>();

        for (int i = 0; i < countOfHours; i++) {
            ans.add(collect.getOrDefault(i, 0L));
        }

        return ans;
    }
}
