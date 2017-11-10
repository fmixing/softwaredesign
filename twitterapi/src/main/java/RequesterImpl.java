import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequesterImpl implements Requester{

    private TwitterAccessor twitterAccessor;


    public RequesterImpl(String bearer, String requestURL) {
        twitterAccessor = new TwitterAccessorImpl(bearer, requestURL);
    }


    @Override
    public List<Tweet> searchWithLimit(DateTime limit, String hashtag) {
        List<Tweet> tweets = new ArrayList<>();

        DateTime lastSeen;

        do {
            List<Tweet> tweetsCurr = twitterAccessor.getTweets(hashtag);

            Optional<Tweet> min = getMin(tweetsCurr);

            //может значить, что вернулся пустой лист
            if (!min.isPresent()) {
                break;
            }

            lastSeen = min.get().getCreationDate();
            tweets.addAll(tweetsCurr);
        }
        while (limit.isBefore(lastSeen));

        return tweets;
    }


    private Optional<Tweet> getMin(List<Tweet> tweetsCurr) {
        return tweetsCurr.stream().
                min((t1, t2) -> {
                    if (t1.getCreationDate().isBefore(t2.getCreationDate()))
                        return 1;
                    if (t1.getCreationDate().isAfter(t2.getCreationDate()))
                        return -1;
                    return 0;
                });
    }
}
