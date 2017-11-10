import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RequesterImpl.class)
public class RequesterTest {

    private static final int limitHours = 1;

    private Requester requester;

    private List<Tweet> answer = new ArrayList<>();

    private DateTime testDateTime = new DateTime();


    @Before
    public void setUp() throws Exception {
        TwitterAccessorImpl twitterAccessor = mock(TwitterAccessorImpl.class);
        whenNew(TwitterAccessorImpl.class).withArguments(anyString(), anyString()).thenReturn(twitterAccessor);
        requester = new RequesterImpl("bearer", "url");
        when(twitterAccessor.getTweets(anyString()))
                .thenReturn(createFirstAns())
                .thenReturn(createSecondAns());
    }


    @Test
    public void testSearchingWithLimit() {
        List<Tweet> tweets = requester.searchWithLimit(testDateTime.minusHours(limitHours), "hashtag");

        Assert.assertThat(tweets, is(answer));
    }


    private List<Tweet> createFirstAns() {
        Random random = new Random();

        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            int randCount = random.nextInt(3600);

            DateTime tweetDateTime = testDateTime.minusSeconds(randCount);
            tweets.add(new Tweet(tweetDateTime));
        }

        tweets.sort(Comparator.comparing(Tweet::getCreationDate).reversed());
        answer.addAll(tweets);

        return tweets;
    }

    private List<Tweet> createSecondAns() {
        Random random = new Random();

        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            int randCount = random.nextInt(3600);

            DateTime tweetDateTime = testDateTime.minusSeconds(randCount);
            tweets.add(new Tweet(tweetDateTime));
        }

        tweets.sort(Comparator.comparing(Tweet::getCreationDate).reversed());
        answer.addAll(tweets);

        for (int i = 0; i < 50; i++) {
            int randCount = random.nextInt(3600) + 3600;

            DateTime tweetDateTime = testDateTime.minusSeconds(randCount);
            tweets.add(new Tweet(tweetDateTime));
        }
        tweets.sort(Comparator.comparing(Tweet::getCreationDate).reversed());

        return tweets;
    }

}
