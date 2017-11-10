import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TweetsCounterImpl implements TweetsCounter {

    private final Requester requester;


    public TweetsCounterImpl(String bearer, String requestURL) {
        requester = new RequesterImpl(bearer, requestURL);
    }


    @Override
    public List<Long> getTweetsCount(int countOfHours, String hashtag) {
        DateTime dateTime = new DateTime();
        DateTime limit = dateTime.minusHours(countOfHours);

        List<Tweet> tweets = requester.searchWithLimit(dateTime, hashtag);

        Map<Integer, Long> collect = tweets.stream().filter(tweet -> tweet.getCreationDate().isAfter(limit))
                .collect(Collectors.groupingBy((Tweet v) ->
                                Hours.hoursBetween(v.getCreationDate(), dateTime).getHours(),
                        Collectors.counting()));

        List<Long> ans = new ArrayList<>();

        for (int i = 0; i < countOfHours; i++) {
            ans.add(collect.getOrDefault(i, 0L));
        }

        return ans;
    }
}
