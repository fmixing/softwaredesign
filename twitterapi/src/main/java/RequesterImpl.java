import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RequesterImpl implements Requester{

    private static Logger logger = LoggerFactory.getLogger(RequesterImpl.class);

    private Twitter twitter;


    public RequesterImpl(TwitterProperties properties) {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(properties.getApikey(), properties.getApisecret());
        twitter.setOAuthAccessToken(new AccessToken(properties.getToken(), properties.getTokensecret()));
    }


    @Override
    public List<Status> searchWithLimit(Date limit, String hashtag) {
        List<Status> tweets = new ArrayList<>();

        Date lastSeen;

        Query query = createQuery(hashtag);

        do {
            QueryResult search = search(query);

            List<Status> tweetsCurr = search.getTweets();

            Optional<Status> min = getMin(tweetsCurr);

            //может значить, что вернулся пустой лист
            if (!min.isPresent()) {
                break;
            }

            lastSeen = min.get().getCreatedAt();
            tweets.addAll(tweetsCurr);
            query = search.nextQuery();
        }
        while (limit.before(lastSeen));

        return tweets;
    }


    private Query createQuery(String hashtag) {
        Query query = new Query(hashtag);
        query.setCount(100);
        return query;
    }


    private Optional<Status> getMin(List<Status> tweetsCurr) {
        return tweetsCurr.stream().
                min((t1, t2) -> {
                    if (t1.getCreatedAt().before(t2.getCreatedAt()))
                        return 1;
                    if (t1.getCreatedAt().after(t2.getCreatedAt()))
                        return -1;
                    return 0;
                });
    }


    private QueryResult search(Query query) {
        System.out.println(query.getCount());
        try {
            return twitter.search(query);
        } catch (TwitterException e) {
            logger.error("Smth went wrong while getting tweets to a query {}", query, e);
            throw new RuntimeException(e);
        }
    }
}
